package com.example.apisnewsapp.models;

import com.google.gson.annotations.SerializedName;

public class Sources {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public Sources(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
