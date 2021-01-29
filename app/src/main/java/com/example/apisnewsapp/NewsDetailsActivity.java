package com.example.apisnewsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.apisnewsapp.R;
import com.example.apisnewsapp.models.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.annotations.Until;
import com.google.gson.internal.$Gson$Preconditions;

public class NewsDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private ImageView imageView;
    private TextView appbar_title, appbar_subtitle, date, time, title;
    private boolean isHideTobarView=false;
    private FrameLayout data_behavior;
    private LinearLayout titleAppBar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl, mImg, mTitle, mDate, mSource, mAuthor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        toolbar=findViewById(R.id.toolbar);
        imageView=findViewById(R.id.backdrop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout=findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        data_behavior=findViewById(R.id.date_behavior);
        titleAppBar=findViewById(R.id.title_appbar);
        appbar_title=findViewById(R.id.title_on_appbar);
        appbar_subtitle=findViewById(R.id.subtitle_on_appbar);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        title=findViewById(R.id.title);

        Intent intent=getIntent();
        mUrl=intent.getStringExtra("Url");
        mDate=intent.getStringExtra("date");
        mAuthor=intent.getStringExtra("author");
        mImg=intent.getStringExtra("img");
        mTitle=intent.getStringExtra("title");
        mSource=intent.getStringExtra("source");

        RequestOptions requestOptions=new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this).load(mImg).apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        appbar_title.setText(mSource);
        appbar_subtitle.setText(mUrl);
        date.setText(mDate);
        title.setText(mTitle);
        String author = null;
        if(mAuthor!=null || mAuthor!=""){
            mAuthor="\u2022"+mAuthor;
        }
        else {
            author="";
        }
        time.setText(mSource+author+"\u2022" +Utils.DateToTimeFormat(mDate));
        initWebView(mUrl);
    }

    private void initWebView(String url){
        WebView webView=findViewById(R.id.webView);
        webView.getSettings().getLoadsImagesAutomatically();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_news,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.view_web){

            Intent i= new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mUrl));
            startActivity(i);
            return true;
        }else if (id==R.id.share){

            try {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plan");
                i.putExtra(Intent.EXTRA_SUBJECT, mSource);
                String body= mTitle+"\n"+mUrl+"\n"+"share from the news app"+"\n";
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(i);
            }catch (Exception e){
                Toast.makeText(this, "hmmm... Sorry, \nconnect be share", Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll= appBarLayout.getTotalScrollRange();
        float precentage= (float) Math.abs(i)/(float)maxScroll;

        if(precentage==1f && isHideTobarView){
            data_behavior.setVisibility(View.GONE);
            titleAppBar.setVisibility(View.VISIBLE);
            isHideTobarView=!isHideTobarView;
        }
        else if(precentage <1f && isHideTobarView){
            data_behavior.setVisibility(View.VISIBLE);
            titleAppBar.setVisibility(View.GONE);
            isHideTobarView=!isHideTobarView;
        }
    }
}
