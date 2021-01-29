package com.example.apisnewsapp.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.apisnewsapp.MainActivity;
import com.example.apisnewsapp.NewsDetailsActivity;
import com.example.apisnewsapp.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
    Context c;
//    private ArrayList<Articles> articles=new ArrayList<>();
    List<Articles>articles;

    public Adapter(Context c, ArrayList<Articles> articles) {
        this.c = c;
        this.articles = articles;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(c).inflate(R.layout.item,parent,false));
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {

        Articles model= articles.get(position);

        RequestOptions requestOptions= new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();
        requestOptions.timeout(3000);
        Glide.with(c).load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                       holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.title.setText(articles.get(position).getTitle());
        holder.author.setText(articles.get(position).getAuthor());
        holder.desc.setText(articles.get(position).getDesc());
        holder.publishedAt.setText(Utils.DateFormat(articles.get(position).getPublishedAt()));
        holder.source.setText(articles.get(position).getSources().getName());
        holder.time.setText("\u2020"+Utils.DateToTimeFormat(articles.get(position).getPublishedAt()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               ImageView imageView= holder.imageView;
                Intent intent=new Intent(c, NewsDetailsActivity.class);
                Articles article= articles.get(position);
                intent.putExtra("Url",article.getUrl());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("author",article.getAuthor());
                intent.putExtra("img",article.getUrlToImage());
                intent.putExtra("date",article.getPublishedAt());
                intent.putExtra("source",article.getSources().getName());
//                Pair<View,String> pair=Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));
//                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(c,pair);
//
//                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN) {
//                    c.startActivity(intent, optionsCompat.toBundle());
//                }
//                else {
//                    c.startActivity(intent);
//                }

                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    class viewHolder extends RecyclerView.ViewHolder{

        TextView title, desc, author, time, source, publishedAt;
        ImageView imageView;
        ProgressBar progressBar;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            author=itemView.findViewById(R.id.author);
            time=itemView.findViewById(R.id.time);
            source=itemView.findViewById(R.id.source);
            publishedAt=itemView.findViewById(R.id.publishedAt);
            imageView=itemView.findViewById(R.id.img);
            progressBar=itemView.findViewById(R.id.progressBar_load_photo);


        }

    }
}
