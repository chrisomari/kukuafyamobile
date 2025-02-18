package com.example.kukuafya;

public class rem_item {
    public  static String title;
    private String flock;
    private String date;
    private String about;

    public rem_item(String title, String about, String flock, String date) {
        this.title = title;
        this.about = about;
        this.flock = flock;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFlock() {
        return flock;
    }

    public void setFlock(String flock) {
        this.flock = flock;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public rem_item() {
    }
}
