package com.musthave0145.mochelins.maps;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.MapApi;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.StoreApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.MapData;
import com.musthave0145.mochelins.model.MapListRes;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.model.Store;
import com.musthave0145.mochelins.model.StoreRes;
import com.musthave0145.mochelins.review.ReviewDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsFragment extends Fragment implements OnMapReadyCallback{
    GoogleMap mMap;
    MapView mapView;

    ImageView imgMenu;

    double lat = 37.5518911;
    double lng = 126.9917937;
    boolean isLocationReady;
    double setDis = 1000/2.0;
    HashMap<Integer, Double> mapZoomDis = new HashMap<Integer, Double>(){{
        put(14, 3000/setDis);
        put(15, 1500/setDis);
        put(16, 750/setDis);
        put(17, 375/setDis);
        put(18, 188/setDis);
        put(19, 94/setDis);
        put(20, 47/setDis);
        put(21, 23/setDis);
    }};
    int zoom = 16;

    Marker choiceMarker;

    ArrayList<MapData> customMapArrayList = new ArrayList<>();

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    RatingBar ratingBar;
    TextView txtName;
    TextView txtVicinity;
    TextView txtView;
    TextView txtLike;
    TextView txtContent;
    ImageView photo;
    CardView cardView;
    CardView researchCardView;
    Integer[] cardViews = {R.id.cardMe, R.id.cardReview, R.id.cardMeeting,
            R.id.cardMap, R.id.cardPlanner, R.id.cardLogout};

    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        ratingBar = rootView.findViewById(R.id.ratingBar);
        txtName = rootView.findViewById(R.id.txtMapStoreName);
        txtVicinity = rootView.findViewById(R.id.txtMapAddr);
        txtView = rootView.findViewById(R.id.txtMapView);
        txtLike = rootView.findViewById(R.id.txtMapLike);
        txtContent = rootView.findViewById(R.id.txtContent);
        photo = rootView.findViewById(R.id.storePhoto);
        cardView = rootView.findViewById(R.id.cardView);
        researchCardView  = rootView.findViewById(R.id.researchCardView);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        cardView.setVisibility(View.GONE);
        researchCardView.setVisibility(View.GONE);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        return rootView;
    }


    private Bitmap viewToBitmap(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    private void setDefaultMarker(){
        // 해당 마커를 누를시 리뷰 1개를 가져옴
        MapData clickedMapData = customMapArrayList.get((int) choiceMarker.getTag());


        View customMarkerView = LayoutInflater.from(getActivity()).inflate(R.layout.marker_layout, null);
        LinearLayout linearLayout = customMarkerView.findViewById(R.id.linearLayout);
        TextView textView = customMarkerView.findViewById(R.id.txtMoney1);
        textView.setTextColor(Color.BLACK);
        textView.setText(clickedMapData.getRating() + "");
        linearLayout.setBackgroundResource(R.drawable.corner1);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(viewToBitmap(customMarkerView));

        choiceMarker.setIcon(icon);

        choiceMarker=null;
    }

    private void setMapCenter(){
        LatLng latLng = new LatLng(lat, lng);
        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.target(latLng);
        builder.zoom(zoom);
        CameraPosition position = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }



    private void getStoreList(){

        // 위치를 이동할때마다 가게를 불러오니, 마커와 배열의 값을 초기화 시킴
        customMapArrayList.clear();
        mMap.clear();

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        MapApi api = retrofit.create(MapApi.class);

        Call<MapListRes> call = api.getPlaceList("Bearer " + token, lat, lng, mapZoomDis.get(zoom));
        call.enqueue(new Callback<MapListRes>() {

            @Override
            public void onResponse(Call<MapListRes> call, Response<MapListRes> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dismissProgress();
                    MapListRes mapList = response.body();
                    Log.d("안녕", "안녕");
                    customMapArrayList.addAll(mapList.items);

                    for (int i = 0; i < customMapArrayList.size(); i++) {
                        MapData mapData = customMapArrayList.get(i);

                        LatLng location = new LatLng(mapData.storeLat, mapData.storeLng);
                        // 커스텀 마커 레이아웃 설정
                        View customMarkerView = LayoutInflater.from(getActivity()).inflate(R.layout.marker_layout, null);
                        ImageView imageView = customMarkerView.findViewById(R.id.imageView);
                        TextView textView = customMarkerView.findViewById(R.id.txtMoney1);

                        imageView.setImageResource(R.drawable.baseline_star_24);

                        String strRating = mapData.getRating() + "";
                        textView.setText(strRating);
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(viewToBitmap(customMarkerView));
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(location)
                                .title(mapData.storeName)
                                .icon(icon);

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(i);
                        marker.showInfoWindow();
                    }

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            cardView.setVisibility(View.VISIBLE);
                            Integer markerIndexInteger = (Integer) marker.getTag();

                            if (markerIndexInteger != null) {
                                int markerIndex = markerIndexInteger.intValue();

                                // 선택되었던 마커가 존재하면 원래대로 돌림
                                if (choiceMarker != null) {
                                    setDefaultMarker();
                                }

                                Log.i("숫자", markerIndex + "");
                                if (markerIndex >= 0 && markerIndex < customMapArrayList.size()) {
                                    // 해당 마커를 누를시 리뷰 1개를 가져옴
                                    MapData clickedMapData = customMapArrayList.get(markerIndex);


                                    // 커스텀 마커 레이아웃 설정
                                    // 마커가 선택되었을때 이미지를 변경
                                    View customMarkerView = LayoutInflater.from(getActivity()).inflate(R.layout.marker_layout, null);
                                    LinearLayout linearLayout = customMarkerView.findViewById(R.id.linearLayout);
                                    TextView textView = customMarkerView.findViewById(R.id.txtMoney1);
                                    textView.setTextColor(Color.WHITE);

                                    textView.setText(clickedMapData.getRating() + "");
                                    linearLayout.setBackgroundResource(R.drawable.corner_select);

                                    BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(viewToBitmap(customMarkerView));

                                    marker.setIcon(icon);

                                    choiceMarker = marker;


                                    Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
                                    StoreApi api = retrofit.create(StoreApi.class);
                                    Call<StoreRes> call = api.getStoreList("Bearer " + token, clickedMapData.storeId);
                                    Log.d("storeName", "인사");
                                    call.enqueue(new Callback<StoreRes>() {
                                        @Override
                                        public void onResponse(Call<StoreRes> call, Response<StoreRes> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                StoreRes storeList = response.body();
                                                Store store = storeList.item;
                                                Log.d("storeName", "성공했어");
                                                int isLikeValue = store.likeCnt;
                                                String isLikeString = String.valueOf(isLikeValue);
                                                int isviewValue = (int) store.view;
                                                String isViewString = String.valueOf(isviewValue);

                                                double isRatingValue = store.rating;
                                                int isStringInt = (int) isRatingValue;
                                                //가게이름
                                                txtName.setText(store.storeName);
                                                //가게주소
                                                txtVicinity.setText(store.storeAddr);
                                                // 게시글 조회수
                                                txtView.setText(isViewString);
                                                // 게시글 좋아요 수
                                                txtLike.setText(isLikeString);
                                                // 게시글 내용
                                                txtContent.setText(store.content);
                                                //가게 사진
                                                Glide.with(MapsFragment.this).load(store.photo)
                                                        .error(R.drawable.not_image).into(photo);
                                                //별점
                                                ratingBar.setRating((float) isRatingValue);

                                                // 선택된 가게의 리뷰 클릭 시
                                                cardView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(getActivity(), ReviewDetailActivity.class);
                                                        Review review = new Review(store.id, store.storeId);
                                                        intent.putExtra("review", review);
                                                        startActivity(intent);

                                                    }
                                                });

                                            } else {
                                                Log.d("ApiResponse", "storeList is null");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<StoreRes> call, Throwable t) {
                                            Log.e("API Call Error", "Error fetching store details", t);
                                        }
                                    });

                                }
                            }
                            return false;
                        }
                    });

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(@NonNull LatLng latLng) {
                            cardView.setVisibility(View.GONE);

                            // 선택되었던 마커가 존재할 경우 이미지 설정을 원래대로 돌림
                            if (choiceMarker != null) {
                                setDefaultMarker();
                            }

                        }
                    });
                }
            }


            @Override
            public void onFailure(Call<MapListRes> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        showProgress();

        MapsFragment.this.mMap = googleMap;

        // 확대 최대 비율 설정
        mMap.setMinZoomPreference(14.f);

        setMapCenter();
        researchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                researchCardView.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);

                choiceMarker=null;

                getStoreList();
            }
        });


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                // 내 위치를 가져올때 딱 한번만 카메라를 이동함
                if(isLocationReady==false){

                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    setMapCenter();

                    getStoreList();



                    // 카메라 이동이 끝났을때 사용
                    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            // 위도, 경도, 줌 배율을 가져옴
                            LatLng latLng = mMap.getCameraPosition().target;
//                    Log.i("확인", latLng.toString());

                            lat = latLng.latitude;
                            lng = latLng.longitude;

                            zoom = ((int) mMap.getCameraPosition().zoom ) -1;

                            if(isLocationReady == true){
                                researchCardView.setVisibility(View.VISIBLE);
                            }
                            isLocationReady = true;
                        }
                    });


                }


            }
        };

        if( ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION} ,
                    100);
        }
        // 위치기반 허용하였으므로,
        // 로케이션 매니저에, 리스너를 연결한다. 그러면 동작한다.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000,
                3,
                locationListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        zoom=16;
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        isLocationReady=false;
    }


    Dialog dialog;

    void showProgress(){
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(new ProgressBar(getActivity()));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    void dismissProgress(){
        dialog.dismiss();
    }
}