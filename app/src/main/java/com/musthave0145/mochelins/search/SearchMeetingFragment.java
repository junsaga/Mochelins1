package com.musthave0145.mochelins.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.SearchMeetingAdapter;
import com.musthave0145.mochelins.adapter.SearchReviewAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.SearchApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.SearchMeeting;
import com.musthave0145.mochelins.model.SearchMeetingRes;
import com.musthave0145.mochelins.model.SearchReview;
import com.musthave0145.mochelins.model.SearchReviewRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchMeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchMeetingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchMeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchMeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchMeetingFragment newInstance(String param1, String param2) {
        SearchMeetingFragment fragment = new SearchMeetingFragment();
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
    int offset =0;
    int limit =5;
    int count;
    SearchMeetingAdapter adapter;
    String pagetoken;
    ImageView imgback;
    EditText searchQuery;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment  return inflater.inflate(R.layout.fragment_search_meeting, container, false);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search_review,container,false);
        recyclerView = rootView.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        imgback = rootView.findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ResultFragment resultFragment = new ResultFragment();
                transaction.replace(R.id.fragmentContainerView,resultFragment);
                transaction.commit();
            }
        });
        Bundle bundle = new Bundle();
        String keyword = bundle.getString("keyword");
        searchQuery.setText(keyword);
        // 어댑터 초기화 및 설정
        addNetworkData();
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchFragment searchFragment = new SearchFragment();
                transaction.replace(R.id.fragmentContainerView,searchFragment);
                transaction.commit();
            }
        });
        return rootView;
    }
    ArrayList<SearchMeeting> searchMeetingArrayList = new ArrayList<>();
    private void addNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        SearchApi api = retrofit.create(SearchApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String token = sp.getString(Config.ACCESS_TOKEN, "");
        Bundle bundle = new Bundle();
        String keyword = bundle.getString("keyword");
        Call<SearchMeetingRes> call = api.getSMList("Bearer " + token,offset,limit,keyword);

        call.enqueue(new Callback<SearchMeetingRes>() {
            @Override
            public void onResponse(Call<SearchMeetingRes> call, Response<SearchMeetingRes> response) {
                SearchMeetingRes searchMeetingRes = response.body();

                // 페이징 위한 변수 처리
                count = searchMeetingRes.count;
                offset = offset + count;

                searchMeetingArrayList.addAll( searchMeetingRes.items );

                adapter = new SearchMeetingAdapter(getActivity(), searchMeetingArrayList);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<SearchMeetingRes> call, Throwable t) {

            }
        });

    }
}