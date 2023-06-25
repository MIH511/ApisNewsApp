package com.example.apisnewsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apisnewsapp.Apis.JsonPlaceHolderAPI;
import com.example.apisnewsapp.models.Adapter;
import com.example.apisnewsapp.models.Articles;
import com.example.apisnewsapp.models.News;
import com.example.apisnewsapp.models.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String API_KEY="";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
     public ArrayList<Articles> articles=new ArrayList<>();
    private Adapter adapter;
    JsonPlaceHolderAPI jpha;
    private TextView topHeadline;
    private ConstraintLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button retryBtn;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topHeadline=findViewById(R.id.topHeadline);
        refreshLayout=findViewById(R.id.swap_refresh_layout);
        refreshLayout.setColorSchemeColors(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        recyclerView=findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        jpha=retrofit.create(JsonPlaceHolderAPI.class);
        onLoadingSwipeRefreash("");

        errorLayout=findViewById(R.id.errorLayout);
        errorImage=findViewById(R.id.error_image);
        errorTitle=findViewById(R.id.error_title);
        errorMessage=findViewById(R.id.error_message);
        retryBtn=findViewById(R.id.error_button);
    }

    private void LoadJson(final String keyword) {
        errorLayout.setVisibility(View.GONE);
        topHeadline.setVisibility(View.INVISIBLE);
        refreshLayout.setRefreshing(true);
        String country= Utils.getCountry();
        String language=Utils.getLanguage();
        Call<News> call ;

        if(keyword.length()>0)
        {
            call = jpha.getNewsSearch(keyword,language,"publishedAt",API_KEY);
        }else {
            call=jpha.getnews(country,API_KEY);
        }
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {

                if (response.isSuccessful() && response.body().getArticles() !=null) {
                    if (!articles.isEmpty()) {
                        articles.clear();
                    }
                    articles = response.body().getArticles();
                    Log.d("articals", response.body().getStatus());
                    adapter = new Adapter(MainActivity.this, articles);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    topHeadline.setVisibility(View.VISIBLE);
                    refreshLayout.setRefreshing(false);

                }
                else {
                    refreshLayout.setRefreshing(false);
                    topHeadline.setVisibility(View.INVISIBLE);
                    String errorCode;
                    switch (response.code()){
                        case 400:
                            errorCode="404 not found";
                            break;
                        case 500:
                            errorCode="500 server broken";
                            break;
                        default:
                            errorCode="unknown error";
                            break;
                    }
                    showMessageError(R.drawable.no_result, "NO RESULT","please try again\n"+errorCode);
                }

            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                topHeadline.setVisibility(View.INVISIBLE);
                refreshLayout.setRefreshing(false);
                showMessageError(R.drawable.no_result, "Oops...","Network failure, please try again\n"+t.toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView=(SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem= menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News.....");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2){
                    onLoadingSwipeRefreash(s);
            }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                onLoadingSwipeRefreash(s);
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false,false);
        return true;
    }

    @Override
    public void onRefresh() {
        LoadJson("");
    }
    private void onLoadingSwipeRefreash(final String keyword){
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadJson(keyword);
            }
        });
    }

    private void showMessageError(int imageView,String title, String message){
        if(errorLayout.getVisibility()==View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoadingSwipeRefreash("");
            }
        });
    }
}
