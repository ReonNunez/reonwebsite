package com.sample.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.models.BlogFeed;
import com.sample.models.CreatePost;
import com.sample.models.LoginCredentials;

import jakarta.servlet.http.HttpSession;

public interface WebContentITF {
    void readyMainPage(Model model, RedirectAttributes attributes, HttpSession session);
    void readyAboutMe(Model model, HttpSession session);
    void registerUser(LoginCredentials loginCred);
    String loginUser(LoginCredentials loginCred, RedirectAttributes attributes, HttpSession session);
    void logOut(Model model, RedirectAttributes attributes);
    void verifylogin(Model model, HttpSession session);
    void updateProfileimg(Model model, MultipartFile file, HttpSession session);
    void createNewPost(Model model, HttpSession session, CreatePost createPost);
    Page<BlogFeed> readyBlogFeed(int size, int page, Model model, HttpSession session, String sortMethod);
    int getTotalPosts();
}
