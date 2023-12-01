package com.sample.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.sample.interfaces.BlogContentITF;

import jakarta.servlet.http.HttpSession;

@Repository
public class BlogContent implements BlogContentITF{

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public void viewBlogContent(Model model, Long postId, HttpSession session) {
        try{
            String mySQLQuery = "SELECT blogtitle, blogcontent, username, uploaddate, isenabled FROM userpost where postid = ?";
            SqlRowSet result = jdbcTemplate.queryForRowSet(mySQLQuery, postId);
            while (result.next()) {
                    model.addAttribute("blogTitle", result.getString("blogtitle"));
                    model.addAttribute("blogContent", result.getString("blogcontent"));
                    model.addAttribute("userName", result.getString("username"));
                    model.addAttribute("blogDate", result.getString("uploaddate"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
