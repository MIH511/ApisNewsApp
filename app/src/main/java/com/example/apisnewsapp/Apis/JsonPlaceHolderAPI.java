package com.example.apisnewsapp.Apis;


import com.example.apisnewsapp.models.News;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderAPI {



    @GET("top-headlines")
    Call<News> getnews(@Query("country") String country,
                       @Query("apiKey") String apiKey);

    @GET("everything")
    Call<News> getNewsSearch(@Query("q") String keyword,
                             @Query("language") String language,
                             @Query("sortBy") String sortBy,
                             @Query("apiKey") String apiKey);



}
