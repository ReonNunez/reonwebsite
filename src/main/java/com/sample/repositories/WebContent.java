package com.sample.repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.RowSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.interfaces.WebContentITF;
import com.sample.models.BlogFeed;
import com.sample.models.CreatePost;
import com.sample.models.LoginCredentials;
import com.sample.utils.Hasher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Repository
public class WebContent implements WebContentITF{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    HttpServletRequest request;
    
    @Override
    public void readyMainPage(Model model, RedirectAttributes attributes, HttpSession session){
        try{
            String mySQLQuery = "SELECT blogtitle, blogcontent, username, uploaddate, isenabled FROM userpost WHERE isenabled = 1 order by uploaddate desc limit 5";
            SqlRowSet result = jdbcTemplate.queryForRowSet(mySQLQuery);
            while (result.next()) {
                if(result.getBoolean("isenabled")){
                    model.addAttribute("blogTitle", result.getString("blogtitle"));
                    model.addAttribute("blogContent", result.getString("blogcontent"));
                    model.addAttribute("userName", result.getString("username"));
                    model.addAttribute("blogDate", result.getString("uploaddate"));
                    break;
                }
            }

            if(attributes.containsAttribute("email")){
                model.addAttribute("email", attributes.getAttribute("email"));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void registerUser(LoginCredentials loginCred){
        String salt = Hasher.generateRandomSalt();
        String passwordHashed = Hasher.hashPassword(loginCred.getPassWord(), salt);
        String sqlQuery = "INSERT INTO usercred(`username`, `userpass`, `email`, `userenabled`, `isuserusing`, `salt`) VALUES (?,?,?,?,?,?)";
        try{
            jdbcTemplate.update(sqlQuery, loginCred.getUserName(), passwordHashed, loginCred.getEMail(), true, false, salt);
            sqlQuery = "SELECT stockimg FROM reference";
            byte[] image = jdbcTemplate.queryForObject(sqlQuery, byte[].class);
            sqlQuery = "INSERT INTO `aboutme`(`username`, `content`, `lastupdate`, `profileimg`, `datecreated`, `totalblog`, `totalmediarepo`)VALUES (?,?,CURDATE(),?,CURDATE(),?,?)";
            jdbcTemplate.update(sqlQuery, loginCred.getUserName(), "", image, 0, 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String loginUser(LoginCredentials loginCred, RedirectAttributes attributes, HttpSession session){
        String mySQLquery = "SELECT userpass FROM usercred WHERE email LIKE '" + loginCred.getEMail() + "' ";

        String result = jdbcTemplate.queryForObject(mySQLquery, String.class);
        if(result != null){
            mySQLquery = "SELECT salt FROM usercred WHERE email LIKE '" + loginCred.getEMail() + "' ";
            String salt = jdbcTemplate.queryForObject(mySQLquery, String.class);
            boolean passwordCheck = Hasher.checkPassword(loginCred.getPassWord(), result, salt);
            if(passwordCheck){
                String email = loginCred.getEMail();
                mySQLquery = "UPDATE usercred SET isuserusing = 1 WHERE email LIKE '" + email + "' ";
                jdbcTemplate.update(mySQLquery);
                mySQLquery = "SELECT username FROM usercred WHERE email LIKE '" + email + "' ";
                String username = jdbcTemplate.queryForObject(mySQLquery, String.class);
                mySQLquery = "SELECT userid FROM usercred WHERE email LIKE '" + email + "' ";
                int userid = jdbcTemplate.queryForObject(mySQLquery, Integer.class);
                session.setAttribute("userid", userid);
                session.setAttribute("username", username);
                session.setAttribute("isAuthenticated", true);
                session.setAttribute("email", email);
                return "redirect:/index";
            } else {
            attributes.addFlashAttribute("isAuthenticated", false);
            attributes.addFlashAttribute("msg", "Incorrect Username/Password");
            return "redirect:/login";
            }
            
        }
        attributes.addFlashAttribute("isAuthenticated", false);
        attributes.addFlashAttribute("msg", "Incorrect Username/Password");
        return "redirect:/login";
        
    }

    @Override
    public void logOut(Model model, RedirectAttributes attributes){
        String email = (String) request.getSession().getAttribute("email");
        String mySQLQuery = "UPDATE usercred SET isuserusing = 0 WHERE email LIKE '" + email + "' ";
        jdbcTemplate.update(mySQLQuery);
    }

    @Override
    public void verifylogin(Model model, HttpSession session){
         if(session.getAttribute("isAuthenticated") != null){
            model.addAttribute("isAuthenticated", session.getAttribute("isAuthenticated"));
        } else {
            model.addAttribute("isAuthenticated", false);
        }
    }

    @Override
    public void readyAboutMe(Model model, HttpSession session){
        
        if(session.getAttribute("isAuthenticated") != null){
            String email = (String) request.getSession().getAttribute("email");
            String username = (String) request.getSession().getAttribute("username");
            String mySQLQuery = "SELECT username, lastupdate, datecreated, totalblog, totalmediarepo FROM aboutme WHERE username LIKE '" + username + "'";
            SqlRowSet result = jdbcTemplate.queryForRowSet(mySQLQuery);
            model.addAttribute("eMail", email);
            while (result.next()) {
                model.addAttribute("userName", result.getString("username"));
                model.addAttribute("lastActive", "Last Active: " + result.getDate("lastupdate"));
                model.addAttribute("dateCreated", "Date Created: " + result.getDate("datecreated"));
                model.addAttribute("totalBlog", "Total Blog Posts: " + result.getInt("totalblog"));
                model.addAttribute("totalMedia", "Total Media Repo Posts: " + result.getInt("totalmediarepo"));
            }
            mySQLQuery = "SELECT profileimg FROM aboutme WHERE username LIKE '" + username + "'";
            byte[] image = jdbcTemplate.queryForObject(mySQLQuery, byte[].class);
            if (image != null && image.length > 0) {
                // Convert the byte array to a Base64-encoded string for embedding in HTML
                String image64 = Base64.getEncoder().encodeToString(image);
                model.addAttribute("profileImg", image64);
            }
            mySQLQuery = "SELECT isuserusing FROM usercred WHERE username LIKE '" + username + "'";
            boolean isActive = jdbcTemplate.queryForObject(mySQLQuery, boolean.class);
            String isActivetext = isActive ? "Yes" : "No"; 
            model.addAttribute("isActive", "Active: " + isActivetext);
        } else {
            model.addAttribute("userName", "");
            model.addAttribute("lastActive", "");
            model.addAttribute("profileImg", "");
            model.addAttribute("dateCreated", "");
            model.addAttribute("totalBlog", "");
            model.addAttribute("totalMedia", "");
        }
    }

    @Override
    public void updateProfileimg(Model model, MultipartFile file, HttpSession session){
        try {
                String username = (String) request.getSession().getAttribute("username");
                byte[] image = file.getBytes();
                String mySQLQuery = "UPDATE aboutme SET profileimg = ? WHERE username LIKE ?";
                jdbcTemplate.update(mySQLQuery, image, username);
                model.addAttribute("message", "File uploaded successfully!");
            } catch (IOException e) {
                model.addAttribute("message", "File upload failed: " + e.getMessage());
            }
    }

    @Override
    public void createNewPost(Model model, HttpSession session, CreatePost createPost){
        String username = (String) request.getSession().getAttribute("username");
        int userid = (int) request.getSession().getAttribute("userid");
        int privacy;
        try {
            if(createPost.getPrivacy().equals("public")){
                privacy = 1;
            } else {
                privacy = 0;
            }
            String mySQLQuery = "INSERT INTO userpost (userid, username, blogcontent, blogtitle, uploaddate, isenabled) VALUES (?,?,?,?,CURDATE(), ?)";
            jdbcTemplate.update(mySQLQuery, userid, username, createPost.getContent(), createPost.getTitle(), privacy);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Page<BlogFeed> readyBlogFeed(int size, int page, Model model, HttpSession session, String sortMethod){
        
        if (session.getAttribute("sortMethod") == null){
            session.setAttribute("sortMethod", sortMethod);
        }
        sortMethod = (String) session.getAttribute("sortMethod");
        System.out.println(sortMethod);
        // Calculate the offset based on the page and size
        int offset = (page) * size;

        // SQL query to fetch paginated blog posts
        String sql = "SELECT * FROM userpost WHERE isenabled = 1 ORDER BY " + sortMethod + " DESC LIMIT ? OFFSET ?";

        // Execute the query and retrieve the results as a RowSet
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, size, offset);
        System.out.println(sortMethod + size + offset);

        // Map the RowSet to BlogPost objects
        List<BlogFeed> posts = new ArrayList<>();
        while (rowSet.next()) {
            BlogFeed post = new BlogFeed();
            post.setUsername(rowSet.getString("username"));
            post.setTitle(rowSet.getString("blogtitle"));
            post.setSummary(rowSet.getString("blogcontent"));
            post.setPostId(rowSet.getInt("postid"));
            // Map other fields as needed
            posts.add(post);
        }

        // Create a Page object (you may need to implement this)
        // This is a simplified example; you may want to handle total count, etc.
        return new PageImpl<>(posts, PageRequest.of(page, size), posts.size());
    }

    @Override
    public int getTotalPosts(){
        try{
            String mySQLQuery = "SELECT COUNT(*) FROM userpost WHERE isenabled = 1";
            Integer count = jdbcTemplate.queryForObject(mySQLQuery, Integer.class);
            if(count != null){
                return count;
            } else {
                return 0;
            }
        } catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
