package com.musthave0145.mochelins.meeting;

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

import android.util.Log;
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
import com.musthave0145.mochelins.adapter.MeetingAdapter;
import com.musthave0145.mochelins.api.MeetingApi;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Meeting;
import com.musthave0145.mochelins.model.MeetingListRes;
import com.musthave0145.mochelins.model.UserRes;
import com.musthave0145.mochelins.review.ReviewFragment;
import com.musthave0145.mochelins.user.InfoActivity;
import com.musthave0145.mochelins.user.LoginActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingFragment newInstance(String param1, String param2) {
        MeetingFragment fragment = new MeetingFragment();
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


//    ImageView imgMenu;
//    ImageView imgAdd;
//    DrawerLayout meetingDrawer;
    RecyclerView recyclerView;
    ProgressBar progressBar;
//    ImageView imgMenuClear;
//    Integer[] cardViews = {R.id.cardMe, R.id.cardReview, R.id.cardMeeting,
//                            R.id.cardMap, R.id.cardPlanner, R.id.cardLogout};
//    CardView[] cardViewList = new CardView[cardViews.length];
    MeetingAdapter adapter;
    ArrayList<Meeting> meetingArrayList = new ArrayList<>();


    int offset = 0;
    int limit = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_meeting, container, false);
//        imgMenu = rootView.findViewById(R.id.imgMenu);
//        imgMenuClear = rootView.findViewById(R.id.imgMenuClear);
//        meetingDrawer = rootView.findViewById(R.id.meetingDrawer);
        progressBar = rootView.findViewById(R.id.progressBar);
//        imgAdd = rootView.findViewById(R.id.imgAdd);


//        // 사이드 메뉴바를 열고 닫는 코드
//        imgMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                meetingDrawer.openDrawer(GravityCompat.END);
//            }
//        });
//
//        imgMenuClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                meetingDrawer.closeDrawer(GravityCompat.END);
//            }
//        });
//
//        for(int i = 0; i < cardViews.length; i++) {
//            cardViewList[i] = rootView.findViewById(cardViews[i]);
//        }
//        imgAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), MeetingCreateActivity.class);
//                startActivity(intent);
//            }
//        });

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

//        reviewFragment = new ReviewFragment();
//        meetingFragment = new MeetingFragment();
//        mapFragment = new MapsFragment();
//        plannerFragment = new PlannerFragment();
//        cardViewList[0].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                meetingDrawer.closeDrawer(GravityCompat.END);
//
//                Intent intent = new Intent(getActivity(), InfoActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        cardViewList[1].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectBottomNavigationItem(R.id.reviewFragment);
//                loadFragment(reviewFragment);
//                meetingDrawer.closeDrawer(GravityCompat.END);
//            }
//        });
//        cardViewList[2].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectBottomNavigationItem(R.id.meetingFragment);
//                loadFragment(meetingFragment);
//                meetingDrawer.closeDrawer(GravityCompat.END);
//            }
//        });
//        cardViewList[3].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectBottomNavigationItem(R.id.mapsFragment);
//                loadFragment(mapFragment);
//                meetingDrawer.closeDrawer(GravityCompat.END);
//            }
//        });
//        cardViewList[4].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectBottomNavigationItem(R.id.plannerFragment);
//                loadFragment(plannerFragment);
//                meetingDrawer.closeDrawer(GravityCompat.END);
//
//            }
//        });
//
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

        adapter = new MeetingAdapter(getActivity(), meetingArrayList);
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 중복으로 겹치지 않게 비워주쟈!
        meetingArrayList.clear();
        offset = 0;

        getNetworkData();
    }


    private void getNetworkData() {

        // 프로그레스바를 보이게 하자
        progressBar.setVisibility(View.VISIBLE);

        // 헤더를 준비하자
        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String token = sp.getString(Config.ACCESS_TOKEN, "");

        // 레트로핏 가져오기
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        // API 만들기
        MeetingApi api = retrofit.create(MeetingApi.class);


        // TODO: 쿼리파라미터 초기값을 어떻게 셋팅할지 고민
        // 쿼리파라미터 셋팅 //필터에서 다 받아와야 한드아~~~!!
        // Lat , Lng 값을 서구청으로...
        Call<MeetingListRes> call = api.getMeetingList("Bearer " + token, offset,limit,37.5453703,126.6759947,5.0);

        call.enqueue(new Callback<MeetingListRes>() {
            @Override
            public void onResponse(Call<MeetingListRes> call, Response<MeetingListRes> response) {
                progressBar.setVisibility(View.GONE);
//                Log.i("성공", response.body().items.get(0).content);


                if(response.isSuccessful()){
                    // 200 OK
                    MeetingListRes meetingListRes = response.body();
                    meetingArrayList.addAll(meetingListRes.items);

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
            public void onFailure(Call<MeetingListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("에러", t.getMessage());
                // 서버에 문제있는겨
                Toast.makeText(getActivity(), "서버에 문제있음",Toast.LENGTH_SHORT).show();

            }
        });


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


    private void selectBottomNavigationItem(int itemId) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView); // 바텀 네비게이션 뷰의 ID를 사용합니다.

        // 바텀 네비게이션 뷰에서 선택한 아이템을 찾아 선택합니다.
        MenuItem item = bottomNavigationView.getMenu().findItem(itemId);

        // 아이템을 선택한 것처럼 처리합니다.
        if (item != null) {
            item.setChecked(true);
        }
    }
}