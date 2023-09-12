package com.musthave0145.mochelins.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.meeting.MeetingDetailActivity;
import com.musthave0145.mochelins.model.Meeting;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.review.ReviewDetailActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class InfoMeetingAdapter extends RecyclerView.Adapter<InfoMeetingAdapter.ViewHolder> {
    Context context;
    ArrayList<Meeting> meetingArrayList;

    public InfoMeetingAdapter(Context context, ArrayList<Meeting> meetingArrayList) {
        this.context = context;
        this.meetingArrayList = meetingArrayList;
    }

    @NonNull
    @Override
    public InfoMeetingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_meeting_photo_row,
                parent, false);
        return new InfoMeetingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting meeting = meetingArrayList.get(position);

        Glide.with(context).load(meeting.photo)
                .error(R.drawable.not_image)
                .diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.imgPhoto);

        String[] date = meeting.date.split("T");
        String month_day = date[0].replace("-", "/").substring(date[0].length()-5);
        String hour_min = date[1].substring(0, 5);

        holder.txtDate.setText(month_day + " " + hour_min);

        Integer attend = meeting.attend;
        Integer maximum =  meeting.maximum;

        holder.txtAttend.setText(attend + "");
        holder.txtMaximum.setText(" / " + maximum);


        if(attend == maximum){
            holder.txtAttend.setTextColor(ContextCompat.getColor(context, R.color.maximum));
        }
    }

    @Override
    public int getItemCount() {
        return meetingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;

        TextView txtDate;
        TextView txtAttend;
        TextView txtMaximum;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtAttend = itemView.findViewById(R.id.txtAttend);
            cardView = itemView.findViewById(R.id.cardView);
            txtMaximum = itemView.findViewById(R.id.txtMaximum);

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
}
