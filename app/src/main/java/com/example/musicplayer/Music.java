package com.example.musicplayer;

public class Music {
    String title;
    String author;
    String url;

    public Music(String title, String author, String url) {
        this.title = title;
        this.author = author;
        this.url = url;
    }

    public Music() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
