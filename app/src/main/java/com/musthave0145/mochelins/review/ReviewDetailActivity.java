package com.musthave0145.mochelins.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.meeting.MeetingDetailActivity;
import com.musthave0145.mochelins.meeting.MeetingUpdateActivity;
import com.musthave0145.mochelins.model.ReviewListRes;
import com.musthave0145.mochelins.store.StoreDetailActivity;
import com.musthave0145.mochelins.adapter.ItemAdapter;
import com.musthave0145.mochelins.adapter.ReviewCommentAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.ReviewApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Comment;
import com.musthave0145.mochelins.model.CommentRes;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.model.ReviewRes;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewDetailActivity extends AppCompatActivity {

    // 사진 여러장 넘기면서 보여주는 라이브러리
    ViewPager2 viewPager2;
    ArrayList<String> imgUrls = new ArrayList<>();
    ItemAdapter adapter;
    LinearLayout indicatorLayout;


    MapView mapView;
    GoogleMap googleMap;

    CircleImageView imgProfile;

    Integer[] imgViews = {R.id.imgBack, R.id.imgLike,
            R.id.imgStar1, R.id.imgStar2, R.id.imgStar3, R.id.imgStar4, R.id.imgStar5};

    ImageView[] imgViewList = new ImageView[imgViews.length];

    ImageView imgMyButton;

    Integer[] txtViews = {R.id.txtStoreName, R.id.txtLike, R.id.txtStar,
                        R.id.txtPersonName, R.id.txtCreatedAt, R.id.txtContent, R.id.txtTag1,
                        R.id.txtTag2, R.id.txtTag3, R.id.txtViews, R.id.txtAddress, R.id.txtDetailStore};
    TextView[] txtViewList = new TextView[txtViews.length];
    String token;

    int reviewId = 0;

    RecyclerView recyclerView;
    ReviewCommentAdapter commentAdapter;
    ArrayList<Comment> commentArrayList = new ArrayList<>();
    Button btnComment;
    SlidingUpPanelLayout slidingUpPanel;
    EditText editContent;
    String comment ="";
    RelativeLayout btnLayout;
    TextView txtDetailStore;
    int storeId =0;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        viewPager2 = findViewById(R.id.viewPager2);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                ReviewDetailActivity.this.googleMap = googleMap;
            }
        });

        imgProfile = findViewById(R.id.imgProfile);
        indicatorLayout = findViewById(R.id.indicatorLayout);
        imgMyButton = findViewById(R.id.imgMyButton);
        btnComment = findViewById(R.id.btnComment);
        slidingUpPanel = (SlidingUpPanelLayout) findViewById(R.id.slidingUpPanel);
        editContent = findViewById(R.id.editContent);
        btnLayout = findViewById(R.id.btnLayout);
        txtDetailStore = findViewById(R.id.txtDetailStore);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewDetailActivity.this));



        for(int i = 0; i < imgViews.length; i++) {
            imgViewList[i] = findViewById(imgViews[i]);
        }
        for(int i = 0; i < txtViews.length; i++) {
            txtViewList[i] = findViewById(txtViews[i]);
        }


        // 가게상세정보에서 리뷰리스트를 눌렀을 때, 넘어오는 거랑 메인화면 리뷰리스트에서 넘어오는거랑 구분지어야 ₩
        Review review = (Review) getIntent().getSerializableExtra("review");
        reviewId = review.id;
        storeId = review.storeId;

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        getReviewComment();

        // 댓글 버튼으로 댓글창 열고닫기
            // 버튼 클릭시 버튼이 사라짐
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComment.setVisibility(View.INVISIBLE);
                slidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });

        // 위로 레이아웃이 나타났을때 버튼을 누를 수 있음
            // 버튼이 다시 나타남
        btnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
                    btnComment.setVisibility(View.VISIBLE);
                    slidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });




        
        // 해당 레이아웃의 위치가 변경되었을때 발동시킴
        slidingUpPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    btnComment.setVisibility(View.VISIBLE);

                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){

                    btnComment.setVisibility(View.INVISIBLE);
                }
            }
        });


        // 댓글을 입력받고 작성까지~~!!
        editContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                    //키패드 내리기
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editContent.getWindowToken(), 0);

                    comment = editContent.getText().toString();
                    Comment comment1 = new Comment(comment);

                    Retrofit retrofit1 = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
                    ReviewApi api1 = retrofit1.create(ReviewApi.class);

                    Call<CommentRes> call = api1.reviewCommentAdd("Bearer " + token, reviewId, comment1);
                    call.enqueue(new Callback<CommentRes>() {
                        @Override
                        public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                            if (response.isSuccessful()) {
                                getReviewComment();
                                editContent.setText("");
                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<CommentRes> call, Throwable t) {

                        }
                    });

                    return true;
                }
                return false;
            }
        });


        // 가게 정보 보기를 누르면, 가게의 상세페이지로 이동한다.
        txtDetailStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewDetailActivity.this, StoreDetailActivity.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });

        // 가게 이름을 눌러도 가게의 상세페이지로 이동한다.
        txtViewList[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewDetailActivity.this, StoreDetailActivity.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });
    }

    private void getReviewDetail(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<ReviewRes> call = api.getReviewDetail("Bearer " + token, reviewId);
        call.enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                Review review1 = response.body().item;

                if(review1.isMine == 0){
                    // 메뉴를 내가 쓴 리뷰면 보여주자!!
                    imgMyButton.setVisibility(View.VISIBLE);


                    imgMyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PopupMenu popupMenu = new PopupMenu(ReviewDetailActivity.this, imgMyButton);
                            MenuInflater inf = popupMenu.getMenuInflater();
                            inf.inflate(R.menu.update_delete_menu, popupMenu.getMenu());

                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    if (menuItem.getItemId() == R.id.menuUpdate){
                                        Intent intent = new Intent(ReviewDetailActivity.this, ReviewUpdateActivity.class);
                                        intent.putExtra("review", review1);
                                        startActivity(intent);

                                    } else if (menuItem.getItemId() == R.id.menuDelete) {
                                        Retrofit retrofit1 = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
                                        ReviewApi api1 = retrofit1.create(ReviewApi.class);

                                        Call<ReviewListRes> call1 = api1.deleteReview("Bearer " + token, review1.id);
                                        call1.enqueue(new Callback<ReviewListRes>() {
                                            @Override
                                            public void onResponse(Call<ReviewListRes> call, Response<ReviewListRes> response) {
                                                if (response.isSuccessful()){
                                                    setResult(Config.RESTART_NUM);

                                                    finish();
                                                } else {

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ReviewListRes> call, Throwable t) {

                                            }
                                        });

                                    }

                                    return false;
                                }

                            });


                            popupMenu.show();


                        }
                    });
                }


                imgViewList[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                imgUrls.clear();
                // 사진 여러장 보여주기
                for (int i = 0; i < review1.photos.size(); i++) {
                    imgUrls.add(review1.photos.get(i).photo);
                }
                adapter = new ItemAdapter(ReviewDetailActivity.this, imgUrls);
                viewPager2.setAdapter(adapter);
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        updateIndicator(position);
                    }
                });

                // 가게 이름
                txtViewList[0].setText(review1.storeName);

                // 내가 좋아요 한 것 처리
                if (review1.isLike == 1) {
                    imgViewList[1].setImageResource(R.drawable.baseline_favorite_24);
                } else {
                    imgViewList[1].setImageResource(R.drawable.baseline_favorite_border_24_2);
                }

                // 좋아요 수
                txtViewList[1].setText(review1.likeCnt+"");

                // 별점 수 대로 별 보여주기
                for (int i = 0; i < 5; i++) {
                    if (i < review1.rating) {
                        imgViewList[i+2].setImageResource(R.drawable.baseline_star_24);
                    }else{
                        imgViewList[i+2].setImageResource(R.drawable.baseline_star_border_24);
                    }
                }
                // 별점 처리
                txtViewList[2].setText(review1.rating+"");

                // 작성자 프로필 사진
                Glide.with(ReviewDetailActivity.this).load(review1.profile)
                        .fallback(R.drawable.default_profile).error(R.drawable.default_profile).into(imgProfile);
                txtViewList[3].setText(review1.nickname);

                //  작성시간 가공해서 보여주기
                // 표준 시간 형식을 SimpleDateFormat을 사용하여 변환
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yy.MM.dd HH:mm");

                // 시간대를 한국 시간대로 변경
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");

                outputFormat.setTimeZone(timeZone);

                String createdAt = "";
                try {
                    Date date = inputFormat.parse(review1.createdAt);
                    createdAt = outputFormat.format(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txtViewList[4].setText(createdAt);

                txtViewList[5].setText(review1.content);

                // 태그가 달린 만큼 보여주자.
                for(int i = 0; i < review1.tags.size(); i++){
                    if(i >= 3){
                        break;
                    }
                    txtViewList[i+6].setVisibility(View.VISIBLE);
                    txtViewList[i+6].setText("#" + review1.tags.get(i).name);
                }

                // 조회수
                txtViewList[9].setText("조회수 "+ review1.view);

                // 맵 세팅
                LatLng storeLatLng = new LatLng(review1.storeLat, review1.storeLng);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLatLng, 18));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(storeLatLng).title(review1.storeName);
                googleMap.addMarker(markerOptions).setTag(0);

                // 가게 주소
                txtViewList[10].setText(review1.storeAddr);

                imgViewList[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(review1.isLike == 0 ){
                            likeReview();
                        } else {
                            likeDeleteReview();
                        }
                    }
                });


            }
            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {

            }
        });
    }

    // 인디케이터 업데이트 메서드
    private void updateIndicator(int position) {
        indicatorLayout.removeAllViews(); // 기존 인디케이터 제거

        int indicatorSize = getResources().getDimensionPixelSize(R.dimen.indicator_size); // 인디케이터 크기
        int indicatorMargin = getResources().getDimensionPixelSize(R.dimen.indicator_margin); // 인디케이터 간격

        // 이미지 URL 목록 크기에 따라 인디케이터를 추가합니다.
        for (int i = 0; i < adapter.getItemCount(); i++) {
            ImageView indicator = new ImageView(this);
            indicator.setImageResource(
                    i == position ? R.drawable.ic_indicator_selected : R.drawable.ic_indicator_unselected
            );

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    indicatorSize,
                    indicatorSize
            );

            if (i > 0) {
                // 첫 번째 인디케이터에는 간격을 주지 않습니다.
                layoutParams.leftMargin = indicatorMargin; // 왼쪽 여백 설정
            }

            indicator.setLayoutParams(layoutParams);
            indicatorLayout.addView(indicator);
        }
    }

    void likeReview() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<ReviewRes> call = api.likeReview("Bearer "+ token, reviewId);
        call.enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if(response.isSuccessful()){
                    finish();
                    overridePendingTransition(0, 0);
                    Intent intent = getIntent();
                    startActivity(intent);
                    overridePendingTransition(0,0);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {

            }
        });
    }

    //좋아요 삭제 메서드
    void likeDeleteReview() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<ReviewRes> call = api.likeDeleteReview("Bearer "+ token, reviewId);
        call.enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if(response.isSuccessful()){
                    finish();
                    overridePendingTransition(0, 0);
                    Intent intent = getIntent();
                    startActivity(intent);
                    overridePendingTransition(0,0);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {

            }
        });
    }

    void getReviewComment() {
        commentArrayList.clear();

        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<CommentRes> call = api.reviewCommentList("Bearer " + token, reviewId, 0, 10);
        call.enqueue(new Callback<CommentRes>() {
            @Override
            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                if (response.isSuccessful()){
                    CommentRes commentRes = response.body();
                    commentArrayList.addAll(commentRes.items);
                    if(commentRes.count == 0){
                        btnComment.setText("댓글 (0)");
                    } else {
                        btnComment.setText("댓글 (" + commentRes.count + ")");
                    }

                    commentAdapter = new ReviewCommentAdapter(ReviewDetailActivity.this, commentArrayList);
                    recyclerView.setAdapter(commentAdapter);


                } else {

                }
            }

            @Override
            public void onFailure(Call<CommentRes> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReviewDetail();
    }
}
