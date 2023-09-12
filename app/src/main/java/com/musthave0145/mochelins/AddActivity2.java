package com.musthave0145.mochelins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.musthave0145.mochelins.api.AccountBookApi;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddActivity2 extends AppCompatActivity {
    EditText txtday;
    EditText txtMoney;
    TextView btnCash;
    TextView btnCard;
    EditText txtStore1;
    EditText txtMenu1;
    TextView btnInput;
    String date;
    String money;
    String payment;
    String store;
    String menu;
    int money1;
    private boolean isRedBackgroundCash = false;
    private boolean isRedBackgroundCard = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add2);
    txtday = findViewById(R.id.txtday);
    txtMoney = findViewById(R.id.txtMoney1);
    btnCash = findViewById(R.id.btnCash);
    btnCard = findViewById(R.id.btnCard);
    txtStore1 = findViewById(R.id.txtStore1);
    txtMenu1 = findViewById(R.id.txtMenu1);
    btnInput = findViewById(R.id.btnInput);




        if (money != null) {
            try {
                money1 = Integer.parseInt(money);
            } catch (NumberFormatException e) {
                // money가 숫자로 변환할 수 없는 경우 처리
            }
        }else{
            int money1 = 0;
        }
        final GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.bubble);


        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRedBackgroundCash) {
                    // 현재 빨간색 배경인 경우, 원래 색으로 변경
                    // 투명한 배경으로 설정
                    btnCard.setBackgroundResource(R.drawable.bubble); // Card 버튼도 투명한 배경으로 설정
                } else  {
                    // 현재 원래 색인 경우, 빨간색으로 변경
                    btnCash.setBackgroundResource(R.drawable.bubble1);
                    btnCard.setBackgroundResource(R.drawable.bubble);// 빨간색 배경으로 설정
                    payment = "현금";


                }
                isRedBackgroundCash = !isRedBackgroundCash;
                 // 클릭 상태를 토
                // Card 버튼의 배경 색상을 원래 색으로 설정

            }
        });
        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRedBackgroundCard) {
                    // 현재 빨간색 배경인 경우, 원래 색으로 변경
                    // 투명한 배경으로 설정
                    btnCash.setBackgroundResource(R.drawable.bubble); // Cash 버튼도 투명한 배경으로 설정
                } else {
                    // 현재 원래 색인 경우, 빨간색으로 변경
                    btnCard.setBackgroundResource(R.drawable.bubble1); // 빨간색 배경으로 설정
                    btnCash.setBackgroundResource(R.drawable.bubble);
                    payment = "카드";
                }

                isRedBackgroundCard = !isRedBackgroundCard; // 클릭 상태를 토
                // Cash 버튼의 배경 색상을 원래 색으로 설정

            }
        });




    store =txtStore1.getText().toString().trim();
    menu = txtMenu1.getText().toString().trim();


        btnInput.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            date = txtday.getText().toString().trim();

            money = txtMoney.getText().toString().trim();
            store = txtStore1.getText().toString().trim();
            menu = txtMenu1.getText().toString().trim();
            if (!money.isEmpty()) {
                try {
                    money1 = Integer.parseInt(money);
                } catch (NumberFormatException e) {
                    // money가 숫자로 변환할 수 없는 경우 처리
                    money1 = 0; // 기본값으로 0 설정
                }
            }

            Retrofit retrofit = NetworkClient.getRetrofitClient(AddActivity2.this);
            AccountBookApi api = retrofit.create(AccountBookApi.class);
            Account account = new Account(store, money1,payment,menu,date,5);
            SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
            String token = sp.getString(Config.ACCESS_TOKEN, "");


            Log.d("시작","자 이제 시작이야");
            Call<Void> call =api.addAccount("Bearer " + token,account);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(AddActivity2.this,"성공했습니다", Toast.LENGTH_SHORT);
                        Log.d("성공","세계야 성공했다 축하한다");
                    }else if(response.code() ==500){
                        Log.d("실패","세계야 더 노력해라");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AddActivity2.this,"실패했습니다", Toast.LENGTH_SHORT);
                    Log.d("실패","좀 더 노력해라 짜샤");
                }
            });

        }
    });

    }
}