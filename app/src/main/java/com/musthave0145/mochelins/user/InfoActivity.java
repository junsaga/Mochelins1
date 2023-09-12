package com.musthave0145.mochelins.user;

import static android.widget.RelativeLayout.ALIGN_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.CENTER_HORIZONTAL;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.CENTER_VERTICAL;
import static android.widget.RelativeLayout.TRUE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.user.fragment.InfoMeetingFragment;
import com.musthave0145.mochelins.model.User;
import com.musthave0145.mochelins.model.UserInfoRes;
import com.musthave0145.mochelins.user.fragment.InfoLikesFragment;
import com.musthave0145.mochelins.user.fragment.InfoReviewsFragment;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoActivity extends AppCompatActivity {

    ImageView btnBackspace;
    ImageView btnEdit;

    CircleImageView imgProfile;
    TextView txtNickname;
    TextView txtName;
    TextView txtEmail;

    User user;
    String token;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //데이터를 받아오는 코드는 여기에 작성.
                    if ( result.getResultCode() == Config.RESTART_NUM){
                        finish();
                        overridePendingTransition(0, 0);
                        Intent intent = getIntent();
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                }
            });

    BottomNavigationView buttonNavigationView;
    View lineView;

    Fragment infoLikesFragment;
    Fragment infoReviewsFragment;
    Fragment infoMeetingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        btnBackspace = findViewById(R.id.btnBackspace);
        btnEdit = findViewById(R.id.btnEdit);
        imgProfile = findViewById(R.id.imgProfile);
        txtNickname = findViewById(R.id.txtNickname);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        lineView = findViewById(R.id.lineView);

        buttonNavigationView = findViewById(R.id.buttonNavigationView);

        // 프레그먼트 객체 생성
        infoLikesFragment = new InfoLikesFragment();
        infoReviewsFragment = new InfoReviewsFragment();
        infoMeetingFragment = new InfoMeetingFragment();


        buttonNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);

                if(itemId == R.id.btnInfoReviews){

                    infoReviewsFragment.setArguments(bundle);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(lineView.getLayoutParams());
                    layoutParams.addRule(ALIGN_PARENT_LEFT, TRUE);
                    layoutParams.addRule(CENTER_VERTICAL);
                    layoutParams.addRule(ALIGN_BOTTOM, R.id.buttonNavigationView);
                    lineView.setLayoutParams(layoutParams);

                    fragment = infoReviewsFragment;

                }else if(itemId == R.id.btnInfoLikes){
                    infoLikesFragment.setArguments(bundle);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(lineView.getLayoutParams());
                    layoutParams.addRule(CENTER_HORIZONTAL, TRUE);
                    layoutParams.addRule(CENTER_VERTICAL);
                    layoutParams.addRule(ALIGN_BOTTOM, R.id.buttonNavigationView);
                    lineView.setLayoutParams(layoutParams);

                    fragment = infoLikesFragment;
                }else if(itemId == R.id.btnInfoMeeting){
                    infoMeetingFragment.setArguments(bundle);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(lineView.getLayoutParams());
                    layoutParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
                    layoutParams.addRule(CENTER_VERTICAL);
                    layoutParams.addRule(ALIGN_BOTTOM, R.id.buttonNavigationView);
                    lineView.setLayoutParams(layoutParams);

                    fragment = infoMeetingFragment;
                }


                return loadFragment(fragment);
            }
        });

        btnEdit.setVisibility(View.INVISIBLE);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        Retrofit retrofit = NetworkClient.getRetrofitClient(InfoActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        Call<UserInfoRes> call = null;
        if((getIntent().getIntExtra("userId", -1)) != -1){
            int userId = getIntent().getIntExtra("userId", -1);
            call = api.user_info("Bearer " + token, userId);
        }else {
            call = api.my_page("Bearer " + token);
        }

        call.enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                if(response.isSuccessful()){
                    user = response.body().item;

                    txtEmail.setText(user.email);
                    txtName.setText(user.name);
                    txtNickname.setText(user.nickname);

                    Glide.with(InfoActivity.this).load(user.profile)
                            .error(R.drawable.default_profile)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(imgProfile);

                    if(user.isMine == 1){
                        btnEdit.setVisibility(View.VISIBLE);
                    }


                    if(buttonNavigationView.getSelectedItemId() == R.id.btnInfoReviews){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", user);

                        buttonNavigationView.setSelectedItemId(R.id.btnInfoReviews);
                    }else if(buttonNavigationView.getSelectedItemId() == R.id.btnInfoLikes){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", user);

                        buttonNavigationView.setSelectedItemId(R.id.btnInfoReviews);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoRes> call, Throwable t) {

            }
        });
        


        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, EditActivity.class);
                intent.putExtra("user", user);

                launcher.launch(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

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
}