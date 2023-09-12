package com.musthave0145.mochelins.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.SearchApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.SearchResultRes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
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


    TextView txtPerson;
    TextView txtCloser4;
    TextView txtCloser3;
    TextView txtCloser1;

    TextView txtCloser2;

    TextView txtSearchStore;

    TextView txtSearchStoreAddr;

    ImageView SUphoto;
    TextView txtSUName;
    TextView txtSURating;
    Integer[] imgSUProfiles = {R.id.imgSUProfile1, R.id.imgSUProfile2, R.id.imgSUProfile3, R.id.imgSUProfile4,
            R.id.imgSUProfile5, R.id.imgSUProfile6, R.id.imgSUProfile7};
    ImageView SRphoto;
    RatingBar SRratingBar;
    TextView txtSRStoreName;
    TextView txtSRContent;
    TextView txtSRAddr;
    TextView txtSRLike;
    TextView txtSRView;

    ImageView SMphoto;
    TextView txtSMStoreName;
    TextView txtSMContent;
    TextView SMeetingDate;
    Integer[] imgSMProfiles = {R.id.imgSMProfile1, R.id.imgSMProfile2, R.id.imgSMProfile3, R.id.imgSMProfile4,
            R.id.imgSMProfile5, R.id.imgSMProfile6, R.id.imgSMProfile7};
    CardView SScardView;
    CardView SUcardView;
    CardView SRcardView;
    CardView SMcardView;

    ImageView imgback;
    EditText searchQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_result,container,false);
        imgback = rootView.findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                HistoryFragment historyFragment = new HistoryFragment();
                transaction.replace(R.id.fragmentContainerView,historyFragment);
                transaction.commit();
            }
        });
        txtSearchStore = rootView.findViewById(R.id.txtSearchStore);
        txtSearchStoreAddr=rootView.findViewById(R.id.txtSearchStoreAddr);
        SScardView = rootView.findViewById(R.id.SScardView);
        SUcardView = rootView.findViewById(R.id.SUcardView);
        SRcardView = rootView.findViewById(R.id.SRcardView);
        SMcardView = rootView.findViewById(R.id.SMcardView);


        txtCloser4 = rootView.findViewById(R.id.txtCloser4);
        txtCloser4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchMeetingFragment searchMeetingFragment = new SearchMeetingFragment();
                transaction.replace(R.id.fragmentContainerView,searchMeetingFragment);
                transaction.commit();
                Bundle bundle =getArguments();
                if (bundle != null) {
                    String data = bundle.getString("key");
                    bundle.putString("key",data);
                    searchMeetingFragment.setArguments(bundle);
                }

            }
        });

        txtCloser3 = rootView.findViewById(R.id.txtCloser3);
        txtCloser3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchReviewFragment searchReviewFragment = new SearchReviewFragment();
                transaction.replace(R.id.fragmentContainerView,searchReviewFragment);
                Bundle bundle =getArguments();
                if (bundle != null) {
                    String data = bundle.getString("key");
                    bundle.putString("key",data);
                    searchReviewFragment.setArguments(bundle);
                }

                transaction.commit();
            }
        });

        txtCloser2 = rootView.findViewById(R.id.txtCloser2);
        txtCloser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchUserFragment searchUserFragment = new SearchUserFragment();
                transaction.replace(R.id.fragmentContainerView,searchUserFragment);
                transaction.commit();

            }
        });
        txtCloser1 =rootView.findViewById(R.id.txtCloser1);
        txtCloser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchStoreFragment searchstoreFragment = new SearchStoreFragment();
                transaction.replace(R.id.fragmentContainerView,searchstoreFragment);
                transaction.commit();
            }
        });

        SUphoto = rootView.findViewById(R.id.SUphoto);
        txtSUName = rootView.findViewById(R.id.txtSUName1);
        txtSURating = rootView.findViewById(R.id.txtSURating);
        CircleImageView[] imgProfileList = new CircleImageView[imgSUProfiles.length];
        for (int i = 0; i < imgSUProfiles.length ; i++) {
            imgProfileList[i] = rootView.findViewById(imgSUProfiles[i]);}
        SRphoto =rootView.findViewById(R.id.SRphoto);
        SRratingBar = rootView.findViewById(R.id.SRratingBar);
        txtSRStoreName = rootView.findViewById(R.id.txtSRStoreName);
        txtSRContent = rootView.findViewById(R.id.txtSRContent);
        txtSRAddr = rootView.findViewById(R.id.txtSRAddr);
        txtSRLike = rootView.findViewById(R.id.txtSRLike);
        txtSRView = rootView.findViewById(R.id.txtSRView);
        CircleImageView[] imgSMProfileList = new CircleImageView[imgSMProfiles.length];
        for (int i = 0; i <  imgSMProfiles.length ; i++) {
            imgSMProfileList[i] = rootView.findViewById(imgSMProfiles[i]);
        }
        SMphoto = rootView.findViewById(R.id.SMphoto);
        txtSMStoreName = rootView.findViewById(R.id.txtSMStoreName);
        txtSMContent = rootView.findViewById(R.id.txtSMContent);
        SMeetingDate = rootView.findViewById(R.id.txtSMeetingDate);
        txtSearchStoreAddr=rootView.findViewById(R.id.txtSearchStoreAddr);

        txtPerson = rootView.findViewById(R.id.txtPerson);
        searchQuery = rootView.findViewById(R.id.searchQuery);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String token = sp.getString(Config.ACCESS_TOKEN, "");

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        SearchApi api = retrofit.create(SearchApi.class);
        Bundle bundle = getArguments();
        if(bundle != null) {
            String data = bundle.getString("keyword");
            searchQuery.setText(data);

            Call<SearchResultRes> call = api.getResultList("Bearer " + token, data);
            call.enqueue(new Callback<SearchResultRes>() {
                @Override
                public void onResponse(Call<SearchResultRes> call, Response<SearchResultRes> response) {
                    SearchResultRes searchResultRes = response.body();
                    if (searchResultRes != null && searchResultRes.item != null) {
                        if (searchResultRes.item.store != null) {
                            // 데이터를 TextView에 설정
                            txtSearchStore.setText(searchResultRes.item.store.getStoreName());
                            txtSearchStoreAddr.setText(searchResultRes.item.store.getStoreAddr());
                        } else if (searchResultRes.item.store == null) {
                            SScardView.setVisibility(View.GONE);
                        }
                        if (searchResultRes.item.user != null) {
                            txtSUName.setText(searchResultRes.item.user.name);
                            txtSURating.setText(String.valueOf((int) searchResultRes.item.user.rating));
                            Glide.with(getContext()).load(searchResultRes.item.user.profile).into(SUphoto);
                            txtSURating.setText((int) searchResultRes.item.user.rating);
                        } else if (searchResultRes.item.user == null) {
                            SUcardView.setVisibility(View.GONE);
                        }
                        if (searchResultRes.item.review != null) {
                            Glide.with(getContext()).load(searchResultRes.item.review.photo).into(SRphoto);
                            SRratingBar.setRating((float) searchResultRes.item.review.rating);
                            txtSRStoreName.setText(searchResultRes.item.review.storeName);
                            txtSRContent.setText(searchResultRes.item.review.content);
                            txtSRAddr.setText(searchResultRes.item.review.storeAddr);
                            txtSRLike.setText(String.valueOf(searchResultRes.item.review.likeCnt));
                            txtSRView.setText(String.valueOf(searchResultRes.item.review.view));
                        } else if (searchResultRes.item.review == null) {
                            SRcardView.setVisibility(View.GONE);
                        }
                        if (searchResultRes.item.meeting != null) {
                            Glide.with(getContext()).load(searchResultRes.item.meeting.profiles).into(SMphoto);
                            txtSMContent.setText(searchResultRes.item.meeting.content);
                            String newDate = "";

                            try {
                                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date date = inputDateFormat.parse(searchResultRes.item.meeting.date);

                                // 날짜 형식을 변경
                                SimpleDateFormat outputDateFormat = new SimpleDateFormat("M월 d일 (E) HH:mm", Locale.KOREA);

                                // Calendar 객체를 사용하여 요일을 얻음
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREA);

                                // 변경된 날짜 형식 출력
                                String formattedDate = outputDateFormat.format(date);
                                newDate = formattedDate.replace("요일", dayOfWeek);
                            } catch (ParseException e) {
                                e.printStackTrace();

                            }

                            SMeetingDate.setText(newDate);


                        } else if (searchResultRes.item.meeting == null) {
                            SMcardView.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchResultRes> call, Throwable t) {

                }
            });
        }


        return rootView;
    }
}