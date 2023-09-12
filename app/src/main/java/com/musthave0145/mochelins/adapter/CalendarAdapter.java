package com.musthave0145.mochelins.adapter;

import static android.graphics.Color.LTGRAY;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.CalendarUtil;
import com.musthave0145.mochelins.model.OnItemListener;

import java.time.LocalDate;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    ArrayList<LocalDate> dayList;

    OnItemListener onItemListener;

    // 유저가 선택한 날짜를 담는 멤버변수 selectedDate!
    public LocalDate selectedDate = null;

    // 오늘 날짜를 담는 멤버변수
    public LocalDate todayDate = LocalDate.now();

    public CalendarAdapter(ArrayList<LocalDate> dayList){
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public CalendarAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calendar_cell,parent,false);

        return new CalendarViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        //날짜 변수에 담기
        LocalDate day = dayList.get(position);

        if (day == null) {
            holder.dayText.setText("");
            holder.parentView.setBackgroundResource(0); // 배경 제거
        } else {
            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));

            // 오늘 날짜 배경 색상 칠하기
            if (day.equals(todayDate) && !day.equals(selectedDate)) {
                holder.parentView.setBackgroundColor(LTGRAY);
            } else {
                holder.parentView.setBackgroundColor(Color.WHITE); // 다른 날짜의 배경을 흰색으로 설정
            }

            // 선택된 날짜에 테두리 적용
            if (day.equals(selectedDate)) {
                holder.parentView.setBackgroundResource(R.drawable.corner); // 테두리 리소스를 지정하세요.
            }
        }
        //날짜 적용

        if ((position + 1) % 7 == 0) { //토요일 파랑
            holder.dayText.setTextColor(Color.BLUE);
        } else if (position == 0 || position % 7 == 0) { //일요일 빨강
            holder.dayText.setTextColor(Color.RED);
        }
        //날짜 클릭 이벤트
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = day; // 선택된 날짜 업데이트
                notifyDataSetChanged(); // 선택된 날짜가 변경되었으므로 RecyclerView를 업데이트합니다.
            }
        });

    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder{
        //초기화
        TextView dayText;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            dayText =itemView.findViewById(R.id.dayText);

            parentView = itemView.findViewById(R.id.parentView);



        }
    }
}