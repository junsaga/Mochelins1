package com.musthave0145.mochelins.store;

import static android.widget.RelativeLayout.ALIGN_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.CENTER_HORIZONTAL;
import static android.widget.RelativeLayout.CENTER_VERTICAL;
import static android.widget.RelativeLayout.TRUE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.StoreApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Store;
import com.musthave0145.mochelins.model.StoreRes;
import com.musthave0145.mochelins.review.ReviewDetailActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StoreDetailActivity extends AppCompatActivity {

    ImageView imgBack;
    ImageView imgStore;
    TextView txtStoreSum;
    TextView txtStoreName;
    TextView txtStar;
    Integer[] imgStars = {R.id.imgStar1, R.id.imgStar2, R.id.imgStar3, R.id.imgStar4, R.id.imgStar5};
    ImageView[] imgStarList = new ImageView[imgStars.length];
    BottomNavigationView bottomNavigationView;
    Fragment storeMapFragment;
    Fragment storeReviewFragment;
    Fragment storeMeetingFragment;
    View lineView;
    String token;
    Store store;
    int storeId;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        imgBack = findViewById(R.id.imgBack);
        imgStore = findViewById(R.id.imgStore);
        txtStoreSum = findViewById(R.id.txtStoreSum);
        txtStoreName = findViewById(R.id.txtStoreName);
        txtStar = findViewById(R.id.txtStar);
        lineView = findViewById(R.id.lineView);

        //
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 프래그먼트 객체 생성
        storeMapFragment = new StoreMapFragment();
        storeReviewFragment = new StoreReviewFragment();
        storeMeetingFragment = new StoreMeetingFragment();


        for (int i = 0 ; i < imgStars.length; i++){
            imgStarList[i] = findViewById(imgStars[i]);
        }

        storeId = getIntent().getIntExtra("storeId", 0);
        Log.i("테스트", storeId + "");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        showProgress();
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        Retrofit retrofit = NetworkClient.getRetrofitClient(StoreDetailActivity.this);
        StoreApi api = retrofit.create(StoreApi.class);

        // 가게의 기본정보를 셋팅한다.
        Call<StoreRes> call = api.getStoreDetail("Bearer " + token, storeId);
        call.enqueue(new Callback<StoreRes>() {
            @Override
            public void onResponse(Call<StoreRes> call, Response<StoreRes> response) {
                dismissProgress();
                if (response.isSuccessful()){

                    store = response.body().item;
                    Log.i("액티비티 테스트", response.body().toString());


                    // 대표 이미지 설정
                    Glide.with(StoreDetailActivity.this).load(store.photo).error(R.drawable.not_image).into(imgStore);
                    // 리뷰요약 설정
                    if (store.summary.isEmpty()){
                        txtStoreSum.setVisibility(View.GONE);
                    } else {
                        txtStoreSum.setText(store.summary);
                    }
//                    Log.i("테스트", store.storeName);
                    txtStoreName.setText(store.storeName);
                    txtStar.setText(store.getRating()+"");

                    // 별점 수 대로 별 보여주기
                    for (int i = 0; i < 5; i++) {
                        if (i < store.rating) {
                            imgStarList[i].setImageResource(R.drawable.baseline_star_24);
                        }
                    }
                    if(bottomNavigationView.getSelectedItemId() == R.id.storeMeeting){
                        bottomNavigationView.setSelectedItemId(R.id.storeMeeting);
                    }else if(bottomNavigationView.getSelectedItemId() == R.id.storeReview){
                        bottomNavigationView.setSelectedItemId(R.id.storeReview);
                    }

                } else {
                    Log.i("에러 테스트", response.body().toString());


                }
            }

            @Override
            public void onFailure(Call<StoreRes> call, Throwable t) {
                dismissProgress();
                Log.i("실패 테스트", t.getMessage());

            }
        });

        // 프래그먼트와 바텀네비게이션 버튼 연결
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                bundle.putInt("storeId", storeId);

                if (itemId == R.id.storeMeeting){

                    storeMeetingFragment.setArguments(bundle);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(lineView.getLayoutParams());
                    layoutParams.addRule(ALIGN_PARENT_LEFT, TRUE);
                    layoutParams.addRule(CENTER_VERTICAL);
                    layoutParams.addRule(ALIGN_BOTTOM, R.id.bottomNavigationView);
                    lineView.setLayoutParams(layoutParams);

                    fragment = storeMeetingFragment;

                } else if (itemId == R.id.storeReview) {
                    storeReviewFragment.setArguments(bundle);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(lineView.getLayoutParams());
                    layoutParams.addRule(CENTER_HORIZONTAL, TRUE);
                    layoutParams.addRule(CENTER_VERTICAL);
                    layoutParams.addRule(ALIGN_BOTTOM, R.id.bottomNavigationView);
                    lineView.setLayoutParams(layoutParams);

                    fragment = storeReviewFragment;

                } else if (itemId == R.id.storeMap) {
                    bundle.putSerializable("store", store);
                    storeMapFragment.setArguments(bundle);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(lineView.getLayoutParams());
                    layoutParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
                    layoutParams.addRule(CENTER_VERTICAL);
                    layoutParams.addRule(ALIGN_BOTTOM, R.id.bottomNavigationView);
                    lineView.setLayoutParams(layoutParams);


                    fragment = storeMapFragment;
                }
                return loadFragment(fragment);
            }
        });
        //

    }
    boolean loadFragment(Fragment fragment){
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();// 화면 전환 코드
            return true;
        } else {
            return false;
        }

    }


    Dialog dialog;

    void showProgress(){
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(new ProgressBar(this));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    void dismissProgress(){
        dialog.dismiss();
    }
}