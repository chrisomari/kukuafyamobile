package com.example.kukuafya;

import java.util.Date;

public class Bookmark {
    private long id;
    private String userId;
    private long questionId;
    private long answerId; // -1 if bookmarking a question
    private Date createdAt;

    public Bookmark() {
        this.createdAt = new Date();
        this.answerId = -1; // Default to question bookmark
    }

    public Bookmark(long id, String userId, long questionId, long answerId) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.createdAt = new Date();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isQuestionBookmark() {
        return answerId == -1;
    }
}
