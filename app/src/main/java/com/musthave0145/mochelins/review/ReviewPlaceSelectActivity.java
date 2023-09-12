package com.musthave0145.mochelins.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.PlaceSelectAdapter2;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.PlaceSelectApi;
import com.musthave0145.mochelins.model.PlaceSelect;
import com.musthave0145.mochelins.model.PlaceSelectList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewPlaceSelectActivity extends AppCompatActivity {
    ImageView imgBack;
    EditText editKeyword;
    ImageView imgSearch;
    RecyclerView recyclerView;
    Button btnSelect;
    ProgressBar progressBar;

    PlaceSelectAdapter2 adapter;
    ArrayList<PlaceSelect> placeSelectArrayList = new ArrayList<>();
    LocationManager locationManager;
    LocationListener locationListener;
    double lat;
    double lng;

    int radius = 5000; // 미터 단위

    String keyword;

    boolean isLocationReady;
    String pagetoken;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_place_select);


        editKeyword = findViewById(R.id.editKeyword);
        imgSearch = findViewById(R.id.imgSearch);
        btnSelect = findViewById(R.id.btnSelect);
        progressBar = findViewById(R.id.progressBar);
        imgBack = findViewById(R.id.imgBack);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewPlaceSelectActivity.this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCont = recyclerView.getAdapter().getItemCount();

                if(lastPosition + 1 == totalCont){

                    if(pagetoken != null){
                        addNetworkData();
                    }

                }

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceSelect placeSelect = placeSelectArrayList.get(adapter.selectedItem);
                Intent intent = new Intent();
                intent.putExtra("placeSelect", placeSelect);
                setResult(1004,intent);
                finish();

            }
        });


        // 폰의 위치를 가져오기 위해서는, 시스템서비스로부터 로케이션 매니져를
        // 받아온다.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 로케이션 리스터를 만든다.
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // 여러분들의 로직 작성.

                // 위도 가져오는 코드.
                // location.getLatitude();
                // 경도 가져오는 코드.
                // location.getLongitude();

                lat = location.getLatitude();
                lng = location.getLongitude();

                isLocationReady = true;
            }
        };

        if( ActivityCompat.checkSelfPermission(ReviewPlaceSelectActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(ReviewPlaceSelectActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION} ,
                    100);
            return;
        }

        // 위치기반 허용하였으므로,
        // 로케이션 매니저에, 리스너를 연결한다. 그러면 동작한다.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000,
                -1,
                locationListener);


        // 돋보기 모양을 눌러도, 검색!
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchPlace();

            }
        });

        // 엔터키를 눌러도, 검색!
        editKeyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    Log.d("MY_TAG", "KeyEvent.KEYCODE_ENTER");
                    InputMethodManager imm = (InputMethodManager) getSystemService(ReviewPlaceSelectActivity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editKeyword.getWindowToken(), 0);//hide keyboard
                    searchPlace();

                    return true;
                }
                return false;
            }
        });

    }

    private void addNetworkData() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewPlaceSelectActivity.this);
        PlaceSelectApi api = retrofit.create(PlaceSelectApi.class);

        Call<PlaceSelectList> call = api.getPlaceSelectList(lat,lng,keyword);

        call.enqueue(new Callback<PlaceSelectList>() {
            @Override
            public void onResponse(Call<PlaceSelectList> call, Response<PlaceSelectList> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){

                    PlaceSelectList placeSelectList = response.body();

                    pagetoken = placeSelectList.next_page_token;

                    placeSelectArrayList.addAll( placeSelectList.items );

                    adapter.notifyDataSetChanged();

                }else{

                }

            }

            @Override
            public void onFailure(Call<PlaceSelectList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void getNetworkData() {

        Log.i("AAA", "getNetworkData");

        progressBar.setVisibility(View.VISIBLE);

        placeSelectArrayList.clear();

        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewPlaceSelectActivity.this);
        PlaceSelectApi api = retrofit.create(PlaceSelectApi.class);

        Call<PlaceSelectList> call = api.getPlaceSelectList(lat, lng, keyword);

        call.enqueue(new Callback<PlaceSelectList>() {
            @Override
            public void onResponse(Call<PlaceSelectList> call, Response<PlaceSelectList> response) {

                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){

                    PlaceSelectList placeSelectList = response.body();

                    pagetoken = placeSelectList.next_page_token;

                    placeSelectArrayList.addAll(placeSelectList.items);

                    adapter = new PlaceSelectAdapter2(ReviewPlaceSelectActivity.this, placeSelectArrayList);
                    recyclerView.setAdapter(adapter);

                }{

                }
            }

            @Override
            public void onFailure(Call<PlaceSelectList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){

            if( ActivityCompat.checkSelfPermission(ReviewPlaceSelectActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ){

                ActivityCompat.requestPermissions(ReviewPlaceSelectActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION} ,
                        100);
                return;
            }

            // 위치기반 허용하였으므로,
            // 로케이션 매니저에, 리스너를 연결한다. 그러면 동작한다.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,
                    -1,
                    locationListener);

        }

    }
    void searchPlace() {
        if(isLocationReady == false){
            Toast.makeText(ReviewPlaceSelectActivity.this,
                    "현재 위치를 수신중입니다. 잠시만 기다려 주세요.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        keyword = editKeyword.getText().toString().trim();

        Log.i("AAA", keyword);

        if(keyword.isEmpty()){
            Log.i("AAA", "isEmpty");
            return;
        }

        getNetworkData();
    }


}