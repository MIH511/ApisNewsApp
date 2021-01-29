package com.example.apisnewsapp.models;

import com.google.gson.annotations.SerializedName;

public class Articles {

    @SerializedName("source")
    private Sources sources;

    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String desc;

    @SerializedName("url")
    private String url;

    @SerializedName("urlToImage")
    private String urlToImage;

    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("content")
    private String content;

    public Articles(Sources sources, String author, String title, String desc, String url, String urlToImage, String publishedAt, String content) {
        this.sources = sources;
        this.author = author;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public Sources getSources() {
        return sources;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getContent() {
        return content;
    }
}
