package com.example.socialx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    Context context;
    List<NewsModel> newsModelArrayList;

    public NewsAdapter(Context context, List<NewsModel> newsModelArrayList) {

        this.context = context;
        this.newsModelArrayList = newsModelArrayList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout,parent,false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {

        holder.title.setText(newsModelArrayList.get(position).getTitle());
        holder.description.setText(newsModelArrayList.get(position).getDescription());
        holder.publishedAt.setText(newsModelArrayList.get(position).getPublishedAt());
        holder.source.setText(newsModelArrayList.get(position).getSource());

        //holder.image.setImageResource(newsModelArrayList.get(position).getImage());
        Glide
                .with(context)
                .load(newsModelArrayList.get(position).image)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return newsModelArrayList.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        TextView title, description, publishedAt, source;
        ImageView image;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            publishedAt = itemView.findViewById(R.id.published_at);
            source = itemView.findViewById(R.id.source);
            image = itemView.findViewById(R.id.image);
        }
    }
}
