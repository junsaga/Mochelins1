package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.review.ReviewDetailActivity;

import java.util.ArrayList;

public class InfoReviewAdapter extends RecyclerView.Adapter<InfoReviewAdapter.ViewHolder> {
    Context context;
    ArrayList<Review> reviewArrayList;

    public InfoReviewAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public InfoReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_photo_row,
                parent, false);
        return new InfoReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewArrayList.get(position);

        Glide.with(context).load(review.photo)
                .error(R.drawable.not_image)
                .diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Review review = reviewArrayList.get(index);

                    Intent intent = new Intent(context, ReviewDetailActivity.class);
                    intent.putExtra("review", review);

                    context.startActivity(intent);
                }
            });
        }
    }
}
