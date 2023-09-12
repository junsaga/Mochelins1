package com.musthave0145.mochelins.review;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.musthave0145.mochelins.maps.MapsFragment;
import com.musthave0145.mochelins.PlannerFragment;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.ReviewAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.ReviewApi;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.meeting.MeetingFragment;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.model.ReviewRes;
import com.musthave0145.mochelins.model.UserRes;
import com.musthave0145.mochelins.user.InfoActivity;
import com.musthave0145.mochelins.user.LoginActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recyclerView;
    ReviewAdapter adapter;
    ProgressBar progressBar;

    ArrayList<Review> reviewArrayList = new ArrayList<>();

    int offset = 0;
    int limit = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review, container, false);

        progressBar = rootView.findViewById(R.id.progressBar);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition(); //맨 마지막 위치
                int totalCount = recyclerView.getAdapter().getItemCount(); //arrayList값의 크기를 가져온다.

                if(lastPosition + 1 == totalCount){ //스크롤을 데이터 맨 끝까지 한 상태이므로
                    //네트워크를 통해서 데이터를 추가로 받아오면 된다.
                    getNetworkData();
                }
            }
        });


//        cardViewList[3].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectBottomNavigationItem(R.id.mapsFragment);
//                loadFragment(mapFragment);
//                reviewDrawer.closeDrawer(GravityCompat.END);
//            }
//        });
//        cardViewList[4].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectBottomNavigationItem(R.id.plannerFragment);
//                loadFragment(plannerFragment);
//                reviewDrawer.closeDrawer(GravityCompat.END);
//
//            }
//        });

//        cardViewList[5].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
//                UserApi api = retrofit.create(UserApi.class);
//
//                SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
//                String token = sp.getString(Config.ACCESS_TOKEN, "");
//
//                Call<UserRes> call = api.logout("Bearer " + token);
//                call.enqueue(new Callback<UserRes>() {
//                    @Override
//                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
//                        if (response.isSuccessful()){
//                            Intent intent = new Intent(getActivity(), LoginActivity.class);
//                            startActivity(intent);
//
//                            getActivity().finish();
//                        } else {
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserRes> call, Throwable t) {
//
//                    }
//                });
//            }
//        });


        adapter = new ReviewAdapter(getActivity(),reviewArrayList);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    boolean loadFragment(Fragment fragment){
        if(fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();// 화면 전환 코드
            return true;
        } else {
            return false;
        }

    }


//    private void selectBottomNavigationItem(int itemId) {
//        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView); // 바텀 네비게이션 뷰의 ID를 사용합니다.
//
//        // 바텀 네비게이션 뷰에서 선택한 아이템을 찾아 선택합니다.
//        MenuItem item = bottomNavigationView.getMenu().findItem(itemId);
//
//        // 아이템을 선택한 것처럼 처리합니다.
//        if (item != null) {
//            item.setChecked(true);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();

        reviewArrayList.clear();
        offset = 0;

        getNetworkData();
    }

    private void getNetworkData() {

        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String token = sp.getString(Config.ACCESS_TOKEN, "");

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<ReviewRes> call = api.getReviewList("Bearer " + token, offset,limit,37.5416541,126.6778043,3.0);
        call.enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()){

                    ReviewRes reviewRes = response.body();
                    reviewArrayList.addAll(reviewRes.items);

                    adapter.notifyDataSetChanged();

                    offset += limit;

                } else if(response.code() == 401){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);

                    getActivity().finish();
                }else {

                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {
                Toast.makeText(getActivity(), "서버에 문제있음",Toast.LENGTH_SHORT).show();
            }
        });
    }





}