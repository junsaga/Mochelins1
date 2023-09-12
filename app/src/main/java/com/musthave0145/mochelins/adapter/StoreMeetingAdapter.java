package com.musthave0145.mochelins.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.MeetingApi;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.ReviewApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.meeting.MeetingDetailActivity;
import com.musthave0145.mochelins.meeting.MeetingUpdateActivity;
import com.musthave0145.mochelins.model.Meeting;
import com.musthave0145.mochelins.model.MeetingRes;
import com.musthave0145.mochelins.model.ReviewListRes;
import com.musthave0145.mochelins.model.Store;
import com.musthave0145.mochelins.review.ReviewUpdateActivity;
import com.musthave0145.mochelins.store.StoreDetailActivity;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StoreMeetingAdapter extends RecyclerView.Adapter<StoreMeetingAdapter.ViewHolder> {

    Context context;
    ArrayList<Meeting> meetingArrayList;

    public StoreMeetingAdapter(Context context, ArrayList<Meeting> meetingArrayList) {
        this.context = context;
        this.meetingArrayList = meetingArrayList;
    }

    @NonNull
    @Override
    public StoreMeetingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_meeting_row,
                parent, false);
        return new StoreMeetingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting meeting = meetingArrayList.get(position);
        // 모임 대표사진 셋팅
        Glide.with(context).load(meeting.photo).error(R.drawable.not_image).into(holder.storePhoto);
        holder.storePhoto.setClipToOutline(true);
        // 모일날짜 셋팅
        String newDate = "";
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = inputDateFormat.parse(meeting.date);

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
        holder.txtMeetingDate.setText(newDate);

        // 내 글인지도 알 수 없다.
        if (meeting.isMine == 1) {
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
                                MeetingApi api1 = retrofit1.create(MeetingApi.class);

                                Call<MeetingRes> call1 = api1.deleteMeeting("Bearer " + token, meeting.id);
                                call1.enqueue(new Callback<MeetingRes>() {
                                    @Override
                                    public void onResponse(Call<MeetingRes> call, Response<MeetingRes> response) {
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
                                    public void onFailure(Call<MeetingRes> call, Throwable t) {
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

        // 작성자 프로필사진,이름 셋팅
        Glide.with(context).load(meeting.profile).error(R.drawable.default_profile).into(holder.imgProfile);
        holder.txtName.setText(meeting.nickname);

        // 내용 셋팅
        holder.txtContent.setText(meeting.content);

        // 참가한 인원만큼 프로필사진을 보여주자!
        for(int i = 0; i < holder.imgProfileList.length; i++){
            if(i < meeting.profiles.size()){
                holder.imgProfileList[i].setVisibility(View.VISIBLE);
                Glide.with(context).load(meeting.profiles.get(i).profile).
                        error(R.drawable.default_profile)
                        .diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.imgProfileList[i]);
            }else{
                holder.imgProfileList[i].setVisibility(View.INVISIBLE);
            }

        }

        // 총 정원과 현재 참가한 인원수를 붙여서 출력하자
        String strCurrentPerson = meeting.attend + " / " + meeting.maximum;
        holder.txtMeetCount.setText(strCurrentPerson);

        if(meeting.attend != meeting.maximum){
            holder.txtMeetCount.setTextColor(ContextCompat.getColor(context, R.color.attend));
        }else {
            holder.txtMeetCount.setTextColor(ContextCompat.getColor(context, R.color.maximum));
        }


    }

    @Override
    public int getItemCount() {
        return meetingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView storePhoto;
        TextView txtMeetingDate;
        CircleImageView imgProfile;
        TextView txtName;
        TextView txtContent;
        TextView txtMeetCount;
        CardView cardView;
        ImageView imgMyMenu;

        Integer[] imgProfiles = {R.id.imgProfile1, R.id.imgProfile2, R.id.imgProfile3,
                                 R.id.imgProfile4, R.id.imgProfile5, R.id.imgProfile6,
                                 R.id.imgProfile7};
        CircleImageView[] imgProfileList = new CircleImageView[imgProfiles.length];

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storePhoto = itemView.findViewById(R.id.storePhoto);
            txtMeetingDate = itemView.findViewById(R.id.txtMeetingDate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtName = itemView.findViewById(R.id.txtName);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtMeetCount = itemView.findViewById(R.id.txtMeetCount);
            cardView = itemView.findViewById(R.id.cardView);
            imgMyMenu = itemView.findViewById(R.id.imgMyMenu);

            for (int i = 0; i < imgProfiles.length; i++) {
                imgProfileList[i] = itemView.findViewById(imgProfiles[i]);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Meeting meeting = meetingArrayList.get(index);

                    Intent intent = new Intent(context, MeetingDetailActivity.class);
                    intent.putExtra("meetingId", meeting.id);

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
