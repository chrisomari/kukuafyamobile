package com.example.kukuafya.models;

public class forumPost {
    private String forum_name;
    private String message;
    private String senderName;

    public forumPost() {
    }

    public String getForum_name() {
        return forum_name;
    }

    public void setForum_name(String forum_name) {
        this.forum_name = forum_name;
    }

    public forumPost(String message, String senderName, String forum_name) {
        this.message = message;
        this.senderName = senderName;
        this.forum_name=forum_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
