package com.example.kukuafya;

import java.util.Date;

public class Question {
    private long id;
    private String title;
    private String content;
    private String userId;
    private String userName;
    private Date createdAt;
    private int upvotes;
    private int downvotes;
    private String category;
    private boolean isSolved;
    private int reportCount;

    public Question() {
        this.createdAt = new Date();
        this.upvotes = 0;
        this.downvotes = 0;
        this.isSolved = false;
        this.reportCount = 0;
    }

    public Question(long id, String title, String content, String userId, String userName, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.category = category;
        this.createdAt = new Date();
        this.upvotes = 0;
        this.downvotes = 0;
        this.isSolved = false;
        this.reportCount = 0;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public void incrementUpvote() {
        this.upvotes++;
    }

    public void decrementUpvote() {
        if (this.upvotes > 0) {
            this.upvotes--;
        }
    }

    public void incrementDownvote() {
        this.downvotes++;
    }

    public void decrementDownvote() {
        if (this.downvotes > 0) {
            this.downvotes--;
        }
    }

    public void incrementReportCount() {
        this.reportCount++;
    }
}
