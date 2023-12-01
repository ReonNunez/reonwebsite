package com.sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.interfaces.BlogContentITF;
import com.sample.interfaces.WebContentITF;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/blogpost")
public class BlogController {

    @Autowired
    BlogContentITF blogContent;

    @Autowired
    WebContentITF webContent;
    
    @GetMapping("/{postId}")
    public String viewBlogPost(@PathVariable Long postId, Model model, HttpSession session){
        webContent.verifylogin(model, session);
        if(model.getAttribute("isAuthenticated").equals(true)){
            blogContent.viewBlogContent(model, postId, session);
            return "viewblog";
        } else {
            return "redirect:/login";
        }
        
    }

    @PostMapping("/sort")
    public String sortListBy(@RequestParam("sortby") String sortMethod, RedirectAttributes attributes){
        System.out.println(sortMethod);
        attributes.addFlashAttribute("sortMethod", sortMethod);
        return "redirect:/blogfeed";
    }

}
