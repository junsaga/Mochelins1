package com.musthave0145.mochelins.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.SearchMeeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchMeetingAdapter extends RecyclerView.Adapter<SearchMeetingAdapter.ViewHolder>{

    Context context;
    ArrayList<SearchMeeting> searchMeetingArrayList;

    public SearchMeetingAdapter(Context context, ArrayList<SearchMeeting> searchMeetingArrayList) {
        this.context = context;
        this.searchMeetingArrayList = searchMeetingArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searchmeeting_row, parent, false);

        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchMeeting searchMeeting = searchMeetingArrayList.get(position);

//        Log.i("확인", meeting.content);


        // 미팅 사진이 없으면, 기본 이미지 표시 / 있으면, 그 사진을 표시하는 로직.
        // 원래는 null !!
        holder.imgPhoto.setClipToOutline(true);
        Glide.with(context).load(searchMeeting.photoURL).into(holder.imgPhoto);

        holder.txtSMContent.setText("  "+searchMeeting.content+"  ");
        holder.txtMeetName.setText(searchMeeting.content);



        String newDate = "";

        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = inputDateFormat.parse(searchMeeting.date);

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
        holder.txSMeetingDate .setText(newDate);

        for(int i = 0; i < searchMeeting.profiles.size(); i++){
            if(i >= holder.imgProfileInteger.length){
                break;
            }
            holder.imgProfiles[i].setVisibility(View.VISIBLE);
            Glide.with(context).load(searchMeeting.profiles.get(i).profile).
                    fallback(R.drawable.default_profile).error(R.drawable.default_profile).into(holder.imgProfiles[i]);
        }

        // 총 정원과 현재 참가한 인원수를 붙여서 출력하자
        String strCurrentPerson = searchMeeting.attend + " / " + searchMeeting.maximum;
        holder.txtSMeetCount.setText(strCurrentPerson);


    }

    @Override
    public int getItemCount() {
        return searchMeetingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imgPhoto;
        TextView txtDistance;
        TextView txtSMContent;
        TextView txtMeetName;
        TextView txtStoreAddress;
        TextView txSMeetingDate;

        Integer[] imgProfileInteger = {R.id.imgSMProfile1, R.id.imgSMProfile2, R.id.imgSMProfile3,
                R.id.imgSMProfile4, R.id.imgSMProfile5, R.id.imgSMProfile6,
                R.id.imgSMProfile7};
        CircleImageView[] imgProfiles = new CircleImageView[imgProfileInteger.length];
                CircleImageView imgSMProfile1;
        CircleImageView imgSMProfile2;
        CircleImageView imgSMProfile3;
        CircleImageView imgSMProfile4;
        CircleImageView imgSMProfile5;
        CircleImageView imgSMProfile6;
        CircleImageView imgSMProfile7;
        TextView txtSMeetCount;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.SMphoto);

            txtSMContent = itemView.findViewById(R.id.txtSMContent);


            txtStoreAddress = itemView.findViewById(R.id.txtStoreAddress);
            txSMeetingDate = itemView.findViewById(R.id.txtSMeetingDate);
            cardView = itemView.findViewById(R.id.cardView);

            for (int i = 0; i < imgProfileInteger.length ; i++) {
                imgProfiles[i] = itemView.findViewById(imgProfileInteger[i]);
            }
           imgSMProfile1 = itemView.findViewById(R.id.imgSMProfile1);
            imgSMProfile2 = itemView.findViewById(R.id.imgSMProfile2);
            imgSMProfile3 = itemView.findViewById(R.id.imgSMProfile3);
            imgSMProfile4 = itemView.findViewById(R.id.imgSMProfile4);
            imgSMProfile5 = itemView.findViewById(R.id.imgSMProfile5);
            imgSMProfile6 = itemView.findViewById(R.id.imgSMProfile6);
            imgSMProfile7 = itemView.findViewById(R.id.imgSMProfile7);
            txtSMeetCount = itemView.findViewById(R.id.txtSMeetCount);
        }
    }
}
