package com.example.kukuafya;

public class Vote {
    private long id;
    private String userId;
    private long questionId;
    private long answerId; // -1 if voting on a question
    private boolean isUpvote;

    public Vote() {
        this.answerId = -1; // Default to question vote
    }

    public Vote(long id, String userId, long questionId, long answerId, boolean isUpvote) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.isUpvote = isUpvote;
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

    public boolean isUpvote() {
        return isUpvote;
    }

    public void setUpvote(boolean upvote) {
        isUpvote = upvote;
    }

    public boolean isQuestionVote() {
        return answerId == -1;
    }
}
