package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.review.ReviewDetailActivity;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.Util;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    Context context;
    ArrayList<Review> reviewArrayList;

    public ReviewAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row, parent, false);

        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewArrayList.get(position);



        // 작성자의 프로필 사진 표시(없으면 기본 이미지, 있으면 올린 사진으로), 이름과 작성 시간을 표시하자!

        Glide.with(context).load(review.profile).error(R.drawable.default_profile)
                .diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.imgPerson);


        holder.txtPerson.setText(review.nickname);

        try {
            String getTimes = new Utils().getDateCalculate(review.createdAt);
            holder.txtTime.setText(getTimes);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        Glide.with(context).load(review.photo).error(R.drawable.not_image).into(holder.imgPhoto);
        holder.imgPhoto.setClipToOutline(true);


        // 좋아요 표시 (내가 좋아요를 눌렀을 때는 빨간 하트로, 내가 좋아요를 누르지 않았을 때는 빈하트 반영)
        if( review.isLike == 1){
            holder.imgLike.setImageResource(R.drawable.baseline_favorite_24);
        }else {
            holder.imgLike.setImageResource(R.drawable.outline_favorite_border_24);
        }

        String strDis = String.format("%.2f",review.distance) + " km";
        holder.txtDistance.setText( strDis);

        // 평점과 상호명을 보여주자!!
        String strRating = review.rating + "";
        holder.txtRating.setText(strRating);
        holder.txtStoreName.setText(review.storeName);

        // 태그를 보여주는데, 선택된 태그가 없으면 가리고 보여주자!


        for(int i = 0; i < holder.txtTagList.length; i++){
            if(i < review.tags.size()){
                holder.txtTagList[i].setVisibility(View.VISIBLE);
                holder.txtTagList[i].setText("#"+review.tags.get(i).name);
            }else {
                holder.txtTagList[i].setVisibility(View.INVISIBLE);
                holder.txtTagList[i].setText("");
            }
        }


        // 숫자들 형변환을 시켜주자!
        String strViews = "조회수 " + review.view;
        String strComments = "댓글수 " + review.commentCnt;
        String strLikes = "좋아요 " + review.likeCnt;
        Log.i("확인", "" + review.likeCnt);



        // 조회수, 댓글수, 좋아요수를 보여주자!!
        holder.txtViews.setText(strViews);
        holder.txtComments.setText(strComments);
        holder.txtLikes.setText(strLikes);


    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        CardView cardView;
        CircleImageView imgPerson;
        TextView txtPerson;
        TextView txtTime;
        ImageView imgPhoto;

        ImageView imgLike;

        TextView txtDistance;

        TextView txtRating;
        TextView txtStoreName;

        Integer[] txtTags = {R.id.txtTag1, R.id.txtTag2, R.id.txtTag3};
        TextView[] txtTagList = new TextView[txtTags.length];

        TextView txtViews;
        TextView txtComments;
        TextView txtLikes;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imgPhoto = itemView.findViewById(R.id.storePhoto);

            imgPerson = itemView.findViewById(R.id.imgPerson);
            txtPerson = itemView.findViewById(R.id.txtPerson);
            txtTime = itemView.findViewById(R.id.txtTime);

            imgLike = itemView.findViewById(R.id.imgLike);

            txtDistance = itemView.findViewById(R.id.txtDistance);

            txtRating = itemView.findViewById(R.id.txtRating);
            txtStoreName = itemView.findViewById(R.id.txtStoreName);

            for (int i = 0; i < txtTagList.length; i++){
                txtTagList[i] = itemView.findViewById(txtTags[i]);
            }
            txtViews = itemView.findViewById(R.id.txtViews);
            txtComments = itemView.findViewById(R.id.txtComments);
            txtLikes = itemView.findViewById(R.id.txtLikes);


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
