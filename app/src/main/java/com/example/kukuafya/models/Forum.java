package com.example.kukuafya.models;

public class Forum {

    private String user_query;
    private String category;

    public Forum() {
    }

    public Forum(String user_query, String category) {
        this.user_query = user_query;
        this.category = category;
    }



    public String getUser_query() {
        return user_query;
    }

    public void setUser_query(String user_query) {
        this.user_query = user_query;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
