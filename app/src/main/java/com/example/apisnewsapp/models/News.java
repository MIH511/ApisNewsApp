package com.example.apisnewsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class News {
    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResult;

    @SerializedName("articles")
    private ArrayList<Articles> articles;

    public News(String status, int totalResult, ArrayList<Articles> articles) {
        this.status = status;
        this.totalResult = totalResult;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public ArrayList<Articles> getArticles() {
        return articles;
    }
}
