package com.sample.interfaces;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

public interface BlogContentITF {
    void viewBlogContent(Model model, Long postId, HttpSession session);
}
