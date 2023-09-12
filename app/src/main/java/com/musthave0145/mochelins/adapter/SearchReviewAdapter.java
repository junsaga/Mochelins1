package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.SearchReview;

import java.util.ArrayList;

public class SearchReviewAdapter extends RecyclerView.Adapter<SearchReviewAdapter.ViewHolder> {
    Context context;
    ArrayList<SearchReview> searchReviewArrayList;

    public SearchReviewAdapter(Context context, ArrayList<SearchReview> searchReviewArrayList) {
        this.context = context;
        this.searchReviewArrayList = searchReviewArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.searchreview_row,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchReview searchReview = searchReviewArrayList.get(position);

        Glide.with(context)
                .load(searchReview.photo)
                .into(holder.SRphoto);
        if(searchReview.content == null) {
            holder.txtSRContent.setText("내용 없음");
        }else{
            holder.txtSRContent.setText(searchReview.content);
        }
        if(searchReview.storeAddr == null){
            holder.txtSRAddr.setText("주소 없음");
        }else {
            holder.txtSRAddr.setText(searchReview.storeAddr);
        }
        String Like = searchReview.likeCnt+"";
        String View = searchReview.view+"";
        holder.txtSRLike.setText(Like);
        holder.txtSRView.setText(View);
        float Rating = (float)searchReview.rating;
        holder.SRatingBar.setRating(Rating);
        Log.d("좋아요",Like);
        Log.d("조회수",View);
        Log.d("별점",Rating+"");

    }

    @Override
    public int getItemCount() {
        return searchReviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtSRStoreName;
        TextView txtSRContent;
        TextView txtSRAddr;
        TextView txtSRLike;
        TextView txtSRView;
        RatingBar SRatingBar;
        ImageView SRphoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSRStoreName = itemView.findViewById(R.id.txtSRtoreName);
            txtSRContent = itemView.findViewById(R.id.txtSRContent);
            txtSRAddr = itemView.findViewById(R.id.txtSRAddr);
            txtSRLike =itemView.findViewById(R.id.txtSRLike);
            txtSRView =itemView.findViewById(R.id.txtSRView);
            SRatingBar =itemView.findViewById(R.id.SRratingBar);
            SRphoto =itemView.findViewById(R.id.SRphoto);
        }
    }
}
