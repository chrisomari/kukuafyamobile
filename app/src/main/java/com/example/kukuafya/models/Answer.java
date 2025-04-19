package com.example.kukuafya;

import java.util.Date;

public class Answer {
    private long id;
    private long questionId;
    private String content;
    private String userId;
    private String userName;
    private Date createdAt;
    private int upvotes;
    private int downvotes;
    private boolean isAccepted;
    private int reportCount;

    public Answer() {
        this.createdAt = new Date();
        this.upvotes = 0;
        this.downvotes = 0;
        this.isAccepted = false;
        this.reportCount = 0;
    }

    public Answer(long id, long questionId, String content, String userId, String userName) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = new Date();
        this.upvotes = 0;
        this.downvotes = 0;
        this.isAccepted = false;
        this.reportCount = 0;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
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

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
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
