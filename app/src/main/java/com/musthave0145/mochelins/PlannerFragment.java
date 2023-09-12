package com.musthave0145.mochelins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.musthave0145.mochelins.adapter.CalendarAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.maps.MapsFragment;
import com.musthave0145.mochelins.meeting.MeetingFragment;
import com.musthave0145.mochelins.model.CalendarUtil;
import com.musthave0145.mochelins.model.UserRes;
import com.musthave0145.mochelins.review.ReviewFragment;
import com.musthave0145.mochelins.user.InfoActivity;
import com.musthave0145.mochelins.user.LoginActivity;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlannerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlannerFragment newInstance(String param1, String param2) {
        PlannerFragment fragment = new PlannerFragment();
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



    TextView monthYearText;
    LocalDate selectedDate;
    ImageView pre_btn;
    ImageView next_btn;
    RecyclerView recyclerView;
    ImageButton btnPre;
    ImageButton btnNext;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_planner, container, false);


        monthYearText = rootView.findViewById(R.id.monthYearText);



        monthYearText = rootView.findViewById(R.id.monthYearText);
        btnPre = rootView.findViewById(R.id.btnPre);
        btnNext = rootView.findViewById(R.id.btnNext);

       recyclerView = rootView.findViewById(R.id.recyclerView);
        //현재 날짜
        CalendarUtil.selectedDate =LocalDate.now();

        //화면 설정
        setMonthView();


        // 전달로 넘어가는 버튼
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재 월-1 변수에 담기
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        // 다음달로 넘어가는 버튼.

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //현재 월+1 변수에 담기
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1);
                setMonthView();

            }
        });


        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FAB 클릭 시 AddActivity 시작
                Intent intent = new Intent(getActivity(),AddActivity2.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O) //이것 역시 코드가 api 버전이 안맞아서 맞춤

    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월");
        return date.format(formatter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String yearMonthFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월");

        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {

        //년월 텍스트뷰 셋팅
        monthYearText.setText(monthYearFromDate(CalendarUtil.selectedDate));
        //해당 월 날짜 가져오기
        ArrayList<LocalDate> dayList = daysInMonthArray(CalendarUtil.selectedDate);
        CalendarAdapter adapter = new CalendarAdapter(dayList);

        //레이아웃 설정(열 7개)
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(),7);

        //레이아웃 적용
        recyclerView.setLayoutManager(manager);

        //어뎁터 적용
        recyclerView.setAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<LocalDate> daysInMonthArray(LocalDate date){

        ArrayList<LocalDate> dayList = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(date);

        //해당 월 마지막 날짜 가져오기
        int lastDay = yearMonth.lengthOfMonth();

        // 해당 월의 첫번째 날 가져오기
        LocalDate firstDay = CalendarUtil.selectedDate.withDayOfMonth(1);

        //첫번째 날 요일 가져오기 (월:1,일:7)
        int dayOfWeek = firstDay.getDayOfWeek().getValue();

        for(int i=1;i<42;i++){
            if(i<=dayOfWeek || i>lastDay+dayOfWeek){
                dayList.add(null);
            }else{
                dayList.add(LocalDate.of(CalendarUtil.selectedDate.getYear(),CalendarUtil.selectedDate.getMonth(),i-dayOfWeek));
            }
        }
        return dayList;

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