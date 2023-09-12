package com.musthave0145.mochelins.adapter;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.ReviewApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Comment;
import com.musthave0145.mochelins.model.CommentRes;
import com.musthave0145.mochelins.review.ReviewDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewCommentAdapter extends RecyclerView.Adapter<ReviewCommentAdapter.ViewHolder>  {

    Context context;
    ArrayList<Comment> commentArrayList;

    public ReviewCommentAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @NonNull
    @Override
    public ReviewCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_comment_row, parent, false);

        return new ReviewCommentAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentArrayList.get(position);

        // 프로필 사진 넣기
        Glide.with(context).load(comment.profile).fallback(R.drawable.default_profile).error(R.drawable.default_profile).into(holder.imgProfile);

        // 이름과, 시간, 코멘트 나오게 하기

        String newDate = "";

        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = inputDateFormat.parse(comment.createdAt);

            // 날짜 형식을 변경
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("M월 d일 (E) HH:mm", Locale.KOREA);

            // Calendar 객체를 사용하여 요일을 얻음
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREA);

            // 변경된 날짜 형식 출력
            String formattedDate = outputDateFormat.format(date);
            newDate = formattedDate.replace("요일", dayOfWeek);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        holder.txtName.setText(comment.nickname + " · " + newDate);
        holder.txtComment.setText(comment.content);

        // 댓글 작성자가 나라면, 버튼을 보여준다.
        if (comment.isMine == 0) {
            holder.imgMenu.setVisibility(View.INVISIBLE);
        } else if (comment.isMine == 1) {
            holder.imgMenu.setVisibility(View.VISIBLE);
        }
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.imgMenu);
                MenuInflater inf = popupMenu.getMenuInflater();
                inf.inflate(R.menu.update_delete_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menuUpdate){
                            // 댓글을 수정하는 코드
                            holder.txtComment.setVisibility(View.GONE);
                            holder.editContent.setVisibility(View.VISIBLE);

                            holder.editContent.setText(comment.content);
                            holder.editContent.requestFocus();

                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            holder.editContent.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                    if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                                        InputMethodManager immhide = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                        SharedPreferences sp = context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
                                        String token = sp.getString(Config.ACCESS_TOKEN, "");

                                        String content = holder.editContent.getText().toString();
                                        Comment comment1 = new Comment(content);

                                        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                                        ReviewApi api = retrofit.create(ReviewApi.class);

                                        Call<CommentRes> call = api.reviewCommentUpdate("Bearer " + token, comment.id, comment1 );
                                        call.enqueue(new Callback<CommentRes>() {
                                            @Override
                                            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                                                if (response.isSuccessful()){
                                                    holder.txtComment.setVisibility(View.VISIBLE);
                                                    holder.editContent.setVisibility(View.GONE);

                                                    // TODO: 수정된 댓글이 자동으로 바뀌게끔 나중에 수정해보자
                                                    ((Activity)context).finish();
                                                    ((Activity)context).overridePendingTransition(0, 0);
                                                    Intent intent = ((Activity)context).getIntent();
                                                    ((Activity)context).startActivity(intent);
                                                    ((Activity)context).overridePendingTransition(0,0);

                                                } else {

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<CommentRes> call, Throwable t) {

                                            }
                                        });
                                    } else {

                                    }

                                    return false;
                                }
                            });

                        } else if (menuItem.getItemId() == R.id.menuDelete) {
                            // 댓글을 삭제하는 코드
                            SharedPreferences sp = context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
                            String token = sp.getString(Config.ACCESS_TOKEN, "");

                            Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                            ReviewApi api = retrofit.create(ReviewApi.class);

                            Call<CommentRes> call = api.reviewCommentDelete("Bearer " + token, comment.id);
                            call.enqueue(new Callback<CommentRes>() {
                                @Override
                                public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                                    if (response.isSuccessful()){
                                        commentArrayList.remove(holder.getAdapterPosition());
                                        notifyDataSetChanged();

                                        // TODO: 댓글만 없어지고, 댓글 수도 자동으로 바뀌게끔 나중에 수정해보자
                                        ((Activity)context).finish();
                                        ((Activity)context).overridePendingTransition(0, 0);
                                        Intent intent = ((Activity)context).getIntent();
                                        ((Activity)context).startActivity(intent);
                                        ((Activity)context).overridePendingTransition(0,0);


                                    } else {

                                    }
                                }

                                @Override
                                public void onFailure(Call<CommentRes> call, Throwable t) {

                                }
                            });



                        }

                        return false;
                    }

                });


                popupMenu.show();

            }


        });






    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgProfile;
        TextView txtName;
        TextView txtComment;
        ImageButton imgMenu;
        EditText editContent;

        

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtName = itemView.findViewById(R.id.txtName);
            txtComment = itemView.findViewById(R.id.txtComment);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            editContent = itemView.findViewById(R.id.editContent);

        }
    }



}
