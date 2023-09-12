package com.musthave0145.mochelins.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.SearchAdapter;
import com.musthave0145.mochelins.adapter.SearchRecentAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.SearchApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.SearchRecent;
import com.musthave0145.mochelins.model.SearchRecentRes;
import com.musthave0145.mochelins.model.SearchRel;
import com.musthave0145.mochelins.model.SearchRelRes;
import com.musthave0145.mochelins.model.SearchStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements TextWatcher {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private SearchAdapter searchAdapter;
    public SearchRecentAdapter searchRecentAdpater;
    EditText searchQuery;
    ImageView btn_search;
    TextView txtHistory;
    String sword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        EditText editText = rootView.findViewById(R.id.searchQuery);

        editText.addTextChangedListener(this);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView4);
        searchAdapter = new SearchAdapter(this, getList(), sword);

        searchQuery =rootView.findViewById(R.id.searchQuery);
        btn_search = rootView.findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sword = searchQuery.getText().toString().trim();
                searchAdapter.setSearchQuery(sword);
                Bundle bundle = new Bundle();
                bundle.putString("keyword",sword);
                ResultFragment resultFragment = new ResultFragment();
                resultFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView,resultFragment);
                transaction.commit();
            }
        });

        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                SpannableString spannableString = new SpannableString(keyword);
                // 검색어가 비어 있으면 최근 검색어(txtHistory)를 보이도록 설정
                searchAdapter.setSearchQuery(keyword);
                sword = searchQuery.getText().toString().trim();
                getList();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchAdapter.getFilter().filter(s.toString());
        sword = searchQuery.getText().toString().trim();
        getList();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    ArrayList <SearchRecent> recentArrayList = new ArrayList<>();


    ArrayList<SearchRel> RelArrayList = new ArrayList<>();
    List<String> searchList = new ArrayList<>() ;


    private List<String> getList(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        SearchApi api = retrofit.create(SearchApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String token = sp.getString(Config.ACCESS_TOKEN, "");

        Call<SearchRelRes> call = api.getRelList("Bearer " + token,sword);
        final List<String> resultList = new ArrayList<>();
        call.enqueue(new Callback<SearchRelRes>() {
            @Override
            public void onResponse(Call<SearchRelRes> call, Response<SearchRelRes> response) {
                if(response.isSuccessful()){

                    SearchRelRes searchRelRes = response.body();
                    List<SearchRel> searchRelList = searchRelRes.items;

                    for (SearchRel searchRel : searchRelList) {
                        resultList.add(searchRel.search);
                    }



                }else {

                }
            }

            @Override
            public void onFailure(Call<SearchRelRes> call, Throwable t) {
            }
        });

        return resultList;

    }


}
