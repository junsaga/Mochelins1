package com.musthave0145.mochelins.meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.MeetingApi;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Meeting;
import com.musthave0145.mochelins.model.MeetingRes;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MeetingDetailActivity extends AppCompatActivity {


//    ViewPager2 viewPager2;
//    int[] images= {R.drawable.avatar1,R.drawable.avatar2,R.drawable.smile2,R.drawable.balloon};
//    ItemAdapter itemAdapter;

    Integer[] imgProfiles = {R.id.imgProfile, R.id.imgProfile1, R.id.imgProfile2, R.id.imgProfile3,
            R.id.imgProfile4, R.id.imgProfile5, R.id.imgProfile6, R.id.imgProfile7};

    CircleImageView[] imgProfileList = new CircleImageView[imgProfiles.length];

    ImageView imgPhoto;
    ImageView imgBack;
    ImageView imgMyMenu;


    Integer[] textViews = {R.id.txtPersonName, R.id.txtContent, R.id.txtMeetCount, R.id.txtStoreName,
                            R.id.txtMaximum, R.id.txtDutch, R.id.txtMeetingDate, R.id.txtStoreAddress};

    TextView[] textViewsList = new TextView[textViews.length];

    Meeting meeting;

    MapView mapView;
    GoogleMap googleMap;
    CardView btnApply;
    TextView txtApply;
    String MEETING_ATTEND  = "모임 신청";
    String MEETING_ATTEND_CANCLE = "모임 취소";

    CardView btnMeetingChat;
    TextView txtMeetingChat;

    int meetingId;
    String token;




    // TODO: 구글맵을 셋팅하자 (마커를 찍게 했지만, 완전한 것 아님)
    // TODO: 본인이 올린 모임글이면, 프로필 사진 오른쪽에 3점메뉴버튼을 표시하고, 수정과 삭제 메뉴를 표시해야 한다.


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                MeetingDetailActivity.this.googleMap = googleMap;
            }
        });

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");


        meetingId = getIntent().getIntExtra("meetingId",0);
//        meeting = (Meeting) getIntent().getSerializableExtra("meeting");
//        int meetingId = meeting.id;
        for (int i = 0; i < imgProfiles.length ; i++) {
            imgProfileList[i] = findViewById(imgProfiles[i]);}

        for (int i = 0; i < textViews.length ; i++) {
            textViewsList[i] = findViewById(textViews[i]);
        }
        imgPhoto = findViewById(R.id.storePhoto);
        imgBack = findViewById(R.id.imgBack);
        btnApply = findViewById(R.id.btnApply);
        txtApply = findViewById(R.id.txtApply);
        imgMyMenu = findViewById(R.id.imgMyButton);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //
        imgMyMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MeetingDetailActivity.this, imgMyMenu);
                MenuInflater inf = popupMenu.getMenuInflater();
                inf.inflate(R.menu.update_delete_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menuUpdate){
                            Intent intent = new Intent(MeetingDetailActivity.this, MeetingUpdateActivity.class);
                            intent.putExtra("meeting", meeting);
                            startActivity(intent);

                        } else if (menuItem.getItemId() == R.id.menuDelete) {
                            deleteMeeting();
                        }

                        return false;
                    }

                });


                popupMenu.show();

            }


        });






        }

        // 처음에 진입했을 때 상세모임 가져오는 메서드
    void getNetworkData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(MeetingDetailActivity.this);
        MeetingApi api = retrofit.create(MeetingApi.class);

        Call<MeetingRes> call = api.getMeetingDetail("Bearer " + token, meetingId);
        call.enqueue(new Callback<MeetingRes>() {
            @Override
            public void onResponse(Call<MeetingRes> call, Response<MeetingRes> response) {
                if (response.isSuccessful()){
                    meeting = response.body().item;
                    Log.i("디테일액티비티", meeting.content);



                    // TODO :구글맵 개선하가,

                    textViewsList[0].setText(meeting.nickname);

                    textViewsList[1].setText(meeting.content);


                    String count = meeting.attend + "/" + meeting.maximum;

                    Glide.with(MeetingDetailActivity.this).load(meeting.photo).error(R.drawable.not_image).into(imgPhoto);

                    Glide.with(MeetingDetailActivity.this).load(meeting.profile).error(R.drawable.default_profile).into(imgProfileList[0]);

                    // 내가 작성한 모임글이면, 수정삭제 버튼을 보여주자!
                    if(meeting.isMine == 1){
                        imgMyMenu.setVisibility(View.VISIBLE);
                    }

                    textViewsList[2].setText(count);
                    if(meeting.attend != meeting.maximum){
                        textViewsList[2].setTextColor(ContextCompat.getColor(MeetingDetailActivity.this, R.color.attend));
                    }else {
                        textViewsList[2].setTextColor(ContextCompat.getColor(MeetingDetailActivity.this, R.color.maximum));
                    }

                    textViewsList[3].setText("  "+meeting.storeName+"  ");

                    textViewsList[4].setText("최대 " + meeting.maximum + "명 참여 가능");

                    // 구글맵 셋팅
                    LatLng storeLatLng = new LatLng(meeting.storeLat, meeting.storeLng);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLatLng, 18));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(storeLatLng).title(meeting.storeName);
                    googleMap.addMarker(markerOptions).setTag(0);



                    //  약속 일정 가공해서 보여주기
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
                    textViewsList[6].setText(newDate);

                    // 주소 세팅
                    textViewsList[7].setText(meeting.storeAddr);

                    // 모임참가 인원의 프로필사진 세팅
                    for(int i = 0; i < meeting.profiles.size(); i++){
                        if(i >= imgProfiles.length - 1){
                            break;
                        }
                        imgProfileList[i+1].setVisibility(View.VISIBLE);
                        Glide.with(MeetingDetailActivity.this).load(meeting.profiles.get(i).profile)
                                .fallback(R.drawable.default_profile).error(R.drawable.default_profile).into(imgProfileList[i+1]);
                    }

                    // 각자계산인지 회비인지 알려주기 ( 0 = 각자 계산, 1 이상부터는 회비)
                    String strPay = "";
                    if (meeting.pay == 0) {
                        strPay = "각자 계산" ;
                    } else {
                        strPay = "1인당 " + meeting.pay + "원씩 계산";
                    }

                    textViewsList[5].setText(strPay);

                    // 모임참가 버튼 셋팅 (내가 참가하지 않았을때는 "모임 참가 신청 & 모임참가 API", 아니면 그 반대)
                    if (meeting.isAttend == 0) {
                        attendMeeting();
                    } else if (meeting.isAttend == 1) {
                        cancleMeeting();
                    }




                } else {

                }
            }

            @Override
            public void onFailure(Call<MeetingRes> call, Throwable t) {

            }
        });
    }

    // 모임 참가 신청 API 실행 메서드
    void attendMeeting() {
        txtApply.setText(MEETING_ATTEND);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = NetworkClient.getRetrofitClient(MeetingDetailActivity.this);
                MeetingApi api = retrofit.create(MeetingApi.class);

                Call<MeetingRes> call = api.attendMeeting("Bearer "+token, meetingId);
                call.enqueue(new Callback<MeetingRes>() {
                    @Override
                    public void onResponse(Call<MeetingRes> call, Response<MeetingRes> response) {
                        if (response.isSuccessful()){
                            refreshdata();
                        } else if (response.code() == 400){
                            Snackbar.make(btnApply,"이미 참가한 모임입니다.", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 402){
                            Snackbar.make(btnApply,"모집 인원이 다 찼습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }



                    @Override
                    public void onFailure(Call<MeetingRes> call, Throwable t) {

                    }
                });

            }
        });
    }

    // 모임참가 취소 API 실행 메서드
    void cancleMeeting() {
        txtApply.setText(MEETING_ATTEND_CANCLE);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = NetworkClient.getRetrofitClient(MeetingDetailActivity.this);
                MeetingApi api = retrofit.create(MeetingApi.class);

                Call<MeetingRes> call = api.cancleMeeting("Bearer "+token, meetingId);
                call.enqueue(new Callback<MeetingRes>() {
                    @Override
                    public void onResponse(Call<MeetingRes> call, Response<MeetingRes> response) {
                        if (response.isSuccessful()){
                            refreshdata();
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<MeetingRes> call, Throwable t) {

                    }
                });
            }
        });
    }
    // 모임 삭제하는 메서드
    void deleteMeeting() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(MeetingDetailActivity.this);
        MeetingApi api = retrofit.create(MeetingApi.class);

        Call<MeetingRes> call = api.deleteMeeting("Bearer " + token, meetingId);
        call.enqueue(new Callback<MeetingRes>() {
            @Override
            public void onResponse(Call<MeetingRes> call, Response<MeetingRes> response) {
                if(response.isSuccessful()){
                    setResult(Config.RESTART_NUM);

                    finish();
                } else {
                }
            }

            @Override
            public void onFailure(Call<MeetingRes> call, Throwable t) {

            }
        });
    }

    void refreshdata() {
        finish();
        overridePendingTransition(0, 0);
        Intent intent = getIntent();
        startActivity(intent);
        overridePendingTransition(0,0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
    }
}

