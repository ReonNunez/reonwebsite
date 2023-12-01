package com.sample.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.interfaces.WebContentITF;
import com.sample.models.BlogFeed;
import com.sample.models.CreatePost;
import com.sample.models.LoginCredentials;

import jakarta.servlet.http.HttpSession;




@Controller
public class WebController {


    @Autowired
    WebContentITF webContent;

    @GetMapping(value = {"/", "/index"})
    public String mainPage(Model model, RedirectAttributes attributes, HttpSession session){
        webContent.verifylogin(model, session);
        webContent.readyMainPage(model, attributes, session);
        return "index";
    }
    
    @GetMapping("/aboutme")
    public String aboutMe(Model model, HttpSession session){
        webContent.verifylogin(model, session);
        webContent.readyAboutMe(model, session);
        return "aboutme";
    }

    @GetMapping("/signup")
    public String signUp(Model model){
        model.addAttribute("credentials", new LoginCredentials());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpConfirmation(@ModelAttribute("credentials") LoginCredentials loginCred, BindingResult result, RedirectAttributes attributes){
        try{
            webContent.registerUser(loginCred);
            attributes.addFlashAttribute("signUpSuccess", true);
            attributes.addFlashAttribute("msg", "Sign up was successful.");
            return "redirect:/login";
        } catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("signUpSuccess", false);
            attributes.addFlashAttribute("msg", "Sign up was unsuccessful.");
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String login(Model model, RedirectAttributes attributes){
        if(attributes.containsAttribute("msg")){
            model.addAttribute("isAuthenticated", attributes.getAttribute("isAuthenticated"));
            model.addAttribute("msg", attributes.getAttribute("msg"));
        }
        model.addAttribute("credentials", new LoginCredentials());      
        return "login";
    }

    @PostMapping("/login")
    public String loginConfirmation(@ModelAttribute("credentials") LoginCredentials loginCred, BindingResult result, 
                                    RedirectAttributes attributes, HttpSession session){ 
        try{
            return webContent.loginUser(loginCred, attributes, session);
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("isAuthenticated", false);
            attributes.addFlashAttribute("msg", "An Unknown Error Occured.");
            return "login";
        }       
    }

    @GetMapping("/createpost")
    public String createPost(Model model, HttpSession session){
        webContent.verifylogin(model, session);
        return "createpost";
    }

    @GetMapping("/mediarepo")
    public String mediaRepo(Model model, HttpSession session){
        webContent.verifylogin(model, session);
        return "mediarepo";
    }

    //under construction
    @GetMapping("/blogfeed")
    public String blogFeed(Model model, HttpSession session, @RequestParam(name = "sortMethod", defaultValue = "postid") String sortMethod, @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size, RedirectAttributes attributes){
        webContent.verifylogin(model, session);
        // Fetch paginated data from the service
        int totalPosts = webContent.getTotalPosts();
        int totalPages = (int) Math.ceil((double) totalPosts / size);
        Page<BlogFeed> postsPage = webContent.readyBlogFeed(size, page, model, session, sortMethod);
        model.addAttribute("postsPage", postsPage);
        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPages", totalPages);
        return "blogfeed";
    }

    //under construction
    @PostMapping("/blogfeed")
    public String postBlogFeed(Model model, @RequestParam(name = "sortMethod", defaultValue = "postid") String sortMethod, HttpSession session, @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size, RedirectAttributes attributes){
        webContent.verifylogin(model, session);
        // Fetch paginated data from the service
        int totalPosts = webContent.getTotalPosts();
        int totalPages = (int) Math.ceil((double) totalPosts / size);
        // Remove the leading comma from the sortMethod parameter, if it exists.
        if (sortMethod != null && sortMethod.startsWith(",")) {
            sortMethod = sortMethod.substring(1);
        }
        session.setAttribute("sortMethod", sortMethod);
        System.out.println(sortMethod + "123");
        Page<BlogFeed> postsPage = webContent.readyBlogFeed(size, page, model, session, sortMethod);
        model.addAttribute("postsPage", postsPage);
        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPages", totalPages);
        return "blogfeed";
    }

    @GetMapping("/logout")
    public String logOut(Model model, RedirectAttributes attributes, HttpSession session){
        webContent.logOut(model, attributes);
        session.invalidate();
        return "redirect:/index";
    }

    @PostMapping("/changeprofileimg")
    public String changeProfileImg(@RequestParam("file") MultipartFile file, Model model, HttpSession session){
        if(!file.getOriginalFilename().equals("")){
            webContent.updateProfileimg(model, file, session);
        }
        return "redirect:/aboutme";
    }

    @PostMapping("/CreateNewPost")
    public String processBlogPost(@RequestParam("blogtitle") String title, @RequestParam("html") String htmlContent, @RequestParam("radioprivacy") String privacyOption, 
                                Model model, HttpSession session) {
        CreatePost createPost = new CreatePost(title, htmlContent, privacyOption);
        webContent.createNewPost(model, session, createPost);
        return "redirect:/index";
    }
}
