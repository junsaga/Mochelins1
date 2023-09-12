package com.musthave0145.mochelins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.maps.MapsFragment;
import com.musthave0145.mochelins.meeting.MeetingCreateActivity;
import com.musthave0145.mochelins.meeting.MeetingFragment;
import com.musthave0145.mochelins.model.UserRes;
import com.musthave0145.mochelins.review.ReviewCreateActivity;
import com.musthave0145.mochelins.review.ReviewFragment;
import com.musthave0145.mochelins.search.HistoryFragment;
import com.musthave0145.mochelins.search.SearchActivity;
import com.musthave0145.mochelins.user.InfoActivity;
import com.musthave0145.mochelins.user.LoginActivity;
import com.musthave0145.mochelins.user.RegisterActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment reviewFragment;
    Fragment meetingFragment;
    Fragment mapsFragment;
    Fragment plannerFragment;


    ImageView imgMenu;
    ImageView imgAdd;
    ImageView imgSearch;

    DrawerLayout mainDrawer;
    ImageView imgMenuClear;
    Integer[] sideView = { R.id.cardMe, R.id.cardReview, R.id.cardMeeting,
            R.id.cardMap, R.id.cardPlanner, R.id.cardLogout};
    CardView[] sideViewList = new CardView[sideView.length];

//    Toolbar toolbar;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgMenu = findViewById(R.id.imgMenu);
        imgAdd = findViewById(R.id.imgAdd);
        imgSearch = findViewById(R.id.imgSearch);
        imgMenuClear = findViewById(R.id.imgMenuClear);
        mainDrawer = findViewById(R.id.mainDrawer);



//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);

//        // 액티비티 시작 시 첫 번째 프래그먼트를 표시
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.reviewFragment, new ReviewFragment())
//                    .commit();
//        }

        // 회원가입이나 로그인이 되어있는 유저인지 체크해야 한다.
        // 억세스토큰이 있는지를 확인하는 코드로 작성.
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        if(token.isEmpty()){
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);

            finish();
            return;
        }
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        // 탭바 화면이랑 연결
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 프레그먼트 객체 생성
        reviewFragment = new ReviewFragment();
        meetingFragment = new MeetingFragment();
        mapsFragment = new MapsFragment();
        plannerFragment = new PlannerFragment();

        // 탭바가 눌렸을 때 프레그먼트 이동
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                Fragment fragment = null;
                if(itemId == R.id.reviewFragment) {
                    imgAdd.setVisibility(View.VISIBLE);
                    fragment = reviewFragment;
                } else if (itemId == R.id.meetingFragment ) {
                    imgAdd.setVisibility(View.VISIBLE);
                    fragment = meetingFragment;
                } else if (itemId == R.id.mapsFragment) {
                    imgAdd.setVisibility(View.GONE);
                    fragment = mapsFragment;
                } else if (itemId == R.id.plannerFragment) {
                    fragment = plannerFragment;
                }
                return loadFragment(fragment);
            }
        });


        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomNavigationView.getSelectedItemId() == R.id.reviewFragment){
                    Intent intent = new Intent(MainActivity.this, ReviewCreateActivity.class);
                    startActivity(intent);
                }else if(bottomNavigationView.getSelectedItemId() == R.id.meetingFragment){
                    Intent intent = new Intent(MainActivity.this, MeetingCreateActivity.class);
                    startActivity(intent);
                }
            }
        });

        // 사이드 메뉴바를 열고 닫는 코드
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainDrawer.openDrawer(GravityCompat.END);
            }
        });

        imgMenuClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainDrawer.closeDrawer(GravityCompat.END);
            }
        });

        // 사이드 메뉴바 안에 카드뷰 연결코드
        for(int i = 0; i < sideView.length; i++) {
            sideViewList[i] = findViewById(sideView[i]);
        }

        sideViewList[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainDrawer.closeDrawer(GravityCompat.END);

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        sideViewList[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.reviewFragment);
                loadFragment(reviewFragment);
                mainDrawer.closeDrawer(GravityCompat.END);
            }
        });

        sideViewList[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.meetingFragment);
                loadFragment(meetingFragment);
                mainDrawer.closeDrawer(GravityCompat.END);
            }
        });

        sideViewList[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.mapsFragment);
                loadFragment(mapsFragment);
                mainDrawer.closeDrawer(GravityCompat.END);
            }
        });

        sideViewList[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.plannerFragment);
                loadFragment(plannerFragment);
                mainDrawer.closeDrawer(GravityCompat.END);

            }
        });

        sideViewList[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
                UserApi api = retrofit.create(UserApi.class);

                Call<UserRes> call = api.logout("Bearer " + token);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        if (response.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);

                            finish();
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });
            }
        });
    }

    boolean loadFragment(Fragment fragment){
        if(fragment != null) {
            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();


            fragmentManager.replace(R.id.fragment, fragment).commit();


            return true;
        } else {
            return false;
        }

    }
}