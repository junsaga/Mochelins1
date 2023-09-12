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

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.HistoryAdapter;
import com.musthave0145.mochelins.adapter.SearchMeetingAdapter;
import com.musthave0145.mochelins.adapter.SearchRecentAdapter;
import com.musthave0145.mochelins.adapter.SearchStoreAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.SearchApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.SearchMeeting;
import com.musthave0145.mochelins.model.SearchRecent;
import com.musthave0145.mochelins.model.SearchRecentRes;
import com.musthave0145.mochelins.model.SearchRes;
import com.musthave0145.mochelins.model.SearchStoreRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
    SearchRecentAdapter adapter;

    EditText searchQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_history,container,false);
        recyclerView =rootView.findViewById(R.id.recyclerView4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchQuery = rootView.findViewById(R.id.searchQuery3);

        searchQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchFragment searchFragment = new SearchFragment();
                transaction.replace(R.id.fragmentContainerView,searchFragment);
                transaction.commit();
            }
        });

        addNetworkData();

        return rootView;
    }
    ArrayList<SearchRecent> searchRecentArrayList = new ArrayList<>();
    private void addNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        SearchApi api = retrofit.create(SearchApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String token = sp.getString(Config.ACCESS_TOKEN, "");

        Call<SearchRecentRes> call = api.getResentList("Bearer " + token);

        call.enqueue(new Callback<SearchRecentRes>() {
            @Override
            public void onResponse(Call<SearchRecentRes> call, Response<SearchRecentRes> response) {
                if(response.isSuccessful()){

                    SearchRecentRes searchRecentRes = response.body();

                    searchRecentArrayList.addAll( searchRecentRes.items );

                    adapter = new SearchRecentAdapter(getActivity(),searchRecentArrayList);

                    recyclerView.setAdapter(adapter);

                }else {

                }
            }

            @Override
            public void onFailure(Call<SearchRecentRes> call, Throwable t) {
            }
        });
    }
}
