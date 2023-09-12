package com.musthave0145.mochelins.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.ReviewApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.model.ReviewListRes;
import com.musthave0145.mochelins.review.ReviewDetailActivity;
import com.musthave0145.mochelins.review.ReviewUpdateActivity;
import com.musthave0145.mochelins.store.StoreDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StoreReviewAdapter extends RecyclerView.Adapter<StoreReviewAdapter.ViewHolder>{

    Context context;
    ArrayList<Review> reviewArrayList;

    public StoreReviewAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public StoreReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_review_row,
                parent, false);
        return new StoreReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Review review = reviewArrayList.get(position);

        Glide.with(context).load(review.profile).error(R.drawable.default_profile).into(holder.imgProfile);
        holder.txtName.setText(review.nickname);
//        holder.txtReview.setText(review.);

//        Log.i("테스트", review.view+" "+review.reviewCnt);

        holder.txtReview.setText("리뷰 "+review.reviewCnt);
        // 내 글인지도 알 수 없다.
        if (review.isMine == 1) {
            holder.imgMyMenu.setVisibility(View.VISIBLE);

            holder.imgMyMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.imgMyMenu);
                    MenuInflater inf = popupMenu.getMenuInflater();
                    inf.inflate(R.menu.delete_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.menuDelete) {
                                showProgress();
                                SharedPreferences sp = context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
                                String token = sp.getString(Config.ACCESS_TOKEN, "");

                                Retrofit retrofit1 = NetworkClient.getRetrofitClient(context);
                                ReviewApi api1 = retrofit1.create(ReviewApi.class);

                                Call<ReviewListRes> call1 = api1.deleteReview("Bearer " + token, review.id);
                                call1.enqueue(new Callback<ReviewListRes>() {
                                    @Override
                                    public void onResponse(Call<ReviewListRes> call, Response<ReviewListRes> response) {
                                        dismissProgress();
                                        if (response.isSuccessful()){
                                            ((StoreDetailActivity) context).finish();
                                            ((StoreDetailActivity) context).overridePendingTransition(0, 0);
                                            Intent intent = ((StoreDetailActivity) context).getIntent();
                                            context.startActivity(intent);
                                            ((StoreDetailActivity) context).overridePendingTransition(0,0);
                                        } else {

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ReviewListRes> call, Throwable t) {
                                        dismissProgress();
                                    }
                                });

                            }

                            return false;
                        }

                    });


                    popupMenu.show();


                }
            });
        }else {
            holder.imgMyMenu.setVisibility(View.INVISIBLE);
        }

        // 별점 수 대로 별 보여주기
        for (int i = 0; i < 5; i++) {
            if (i < review.rating) {
                holder.imgStarList[i].setImageResource(R.drawable.baseline_star_24);
            }else{
                holder.imgStarList[i].setImageResource(R.drawable.baseline_star_border_24);
            }
        }

        //  작성시간 가공해서 보여주기
        // 표준 시간 형식을 SimpleDateFormat을 사용하여 변환
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy.MM.dd");

        // 시간대를 한국 시간대로 변경
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        inputFormat.setTimeZone(timeZone);
        outputFormat.setTimeZone(timeZone);

        String createdAt = "";
        try {
            Date date = inputFormat.parse(review.createdAt);
            createdAt = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtDate.setText(createdAt);

        // 사진 보여주기
        Glide.with(context).load(review.photo).error(R.drawable.not_image).into(holder.reviewPhoto);

        // 작성 글 세팅하기
        holder.txtContent.setText(review.content);


        // 태그가 달린 만큼 보여주자.
        for(int i = 0; i < 3; i++){
            if(i < review.tags.size()){
                holder.txtTagList[i].setVisibility(View.VISIBLE);
                holder.txtTagList[i].setText("#" + review.tags.get(i).name);
            }else{
                holder.txtTagList[i].setVisibility(View.INVISIBLE);
            }

        }

        // 내가 좋아요 한 게시물은 꽉 찬 하트로 보여주자.
        if(review.isLike == 1){
            holder.imgLike.setImageResource(R.drawable.baseline_favorite_24);
        }else {
            holder.imgLike.setImageResource(R.drawable.baseline_favorite_border_24_2);
        }

        // 좋아요 수도 보여주자!
        holder.txtLike.setText(review.likesCnt+"");

        // 조회수를 보여주자!
        holder.txtView.setText("조회수 "+review.view);
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        CircleImageView imgProfile;
        TextView txtName;
        TextView txtReview;
        ImageView imgMyMenu;
        Integer[] imgStars = {R.id.imgStar1,R.id.imgStar2,R.id.imgStar3,R.id.imgStar4,R.id.imgStar5};
        ImageView[] imgStarList = new ImageView[imgStars.length];
        TextView txtDate;
        ImageView reviewPhoto;
        TextView txtContent;

        Integer[] txtTags = {R.id.txtTag1, R.id.txtTag2, R.id.txtTag3};
        TextView[] txtTagList = new TextView[txtTags.length];

        ImageView imgLike;
        TextView txtLike;
        TextView txtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtName = itemView.findViewById(R.id.txtName);
            txtReview = itemView.findViewById(R.id.txtReview);
            imgMyMenu = itemView.findViewById(R.id.imgMyMenu);

            for (int i = 0 ; i < imgStars.length; i++) {
                imgStarList[i] = itemView.findViewById(imgStars[i]);
            }
            txtDate = itemView.findViewById(R.id.txtDate);
            reviewPhoto = itemView.findViewById(R.id.reviewPhoto);
            txtContent = itemView.findViewById(R.id.txtContent);

            for(int i = 0 ; i < txtTags.length; i++){
                txtTagList[i] = itemView.findViewById(txtTags[i]);
            }

            imgLike = itemView.findViewById(R.id.imgLike);
            txtLike = itemView.findViewById(R.id.txtLike);
            txtView = itemView.findViewById(R.id.txtView);

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

    Dialog dialog;

    void showProgress(){
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(new ProgressBar(context));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    void dismissProgress(){
        dialog.dismiss();
    }
}
