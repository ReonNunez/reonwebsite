package com.sample.models;

public class CreatePost {
    String title;
    String content;
    String privacy;

    public CreatePost(String title, String content, String privacy) {
        this.title = title;
        this.content = content;
        this.privacy = privacy;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrivacy() {
        return this.privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

}
