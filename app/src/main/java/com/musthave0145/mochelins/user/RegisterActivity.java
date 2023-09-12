package com.musthave0145.mochelins.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.model.User;
import com.musthave0145.mochelins.model.UserRes;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editNickName;
    EditText editName;
    EditText editPassword1;

    TextInputLayout layoutEmail;
    TextInputLayout layoutNickname;
    TextInputLayout layoutName;
    TextInputLayout layoutPassword1;
    TextInputLayout layoutPassword2;


    EditText editPassword2;
    Button btnRegister;
    TextView txtLogin;

    ImageView btnClose;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        editEmail = findViewById(R.id.editEmail);
        editNickName = findViewById(R.id.editNickName);
        editName = findViewById(R.id.editName);
        editPassword1 = findViewById(R.id.editPassword1);
        editPassword2 = findViewById(R.id.editPassword2);
        btnClose = findViewById(R.id.btnClose);

        editEmail.setFocusableInTouchMode(true);
        editNickName.setFocusableInTouchMode(true);
        editName.setFocusableInTouchMode(true);
        editPassword1.setFocusableInTouchMode(true);
        editPassword2.setFocusableInTouchMode(true);

        layoutEmail = findViewById(R.id.layoutEmail);
        layoutNickname = findViewById(R.id.layoutNickname);
        layoutName = findViewById(R.id.layoutName);
        layoutPassword1 = findViewById(R.id.layoutPassword1);
        layoutPassword2 = findViewById(R.id.layoutPassword2);

        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                String nickname = editNickName.getText().toString().trim();
                String name = editName.getText().toString().trim();
                String password1 = editPassword1.getText().toString().trim();
                String password2 = editPassword2.getText().toString().trim();

                layoutEmail.setError(null);
                layoutPassword1.setError(null);
                layoutPassword2.setError(null);
                layoutNickname.setError(null);
                layoutName.setError(null);


                Pattern pattern = Patterns.EMAIL_ADDRESS; // 여러 패턴 중에 하나의 패턴 꺼집어낸다.
                if (pattern.matcher(email).matches() == false) {

                    layoutEmail.setError("정상적인 이메일의 형식이 아닙니다.");
                    editEmail.requestFocus();
                    return;
                }

                if(nickname.isEmpty()){
                    layoutNickname.setError("닉네임을 입력하셔야 합니다.");
                    editNickName.requestFocus();
                    return;
                }

                if(name.isEmpty()){
                    layoutName.setError("이름을 입력하셔야 합니다.");
                    editName.requestFocus();
                    return;
                }

                if (password1.length() < 6) {
                    layoutPassword1.setError("비밀번호는 최소 6자리 이상입니다.");
                    editPassword1.requestFocus();
                    return;
                }

                if(  !password1.equals(password2)  ){
                    layoutPassword2.setError("비밀번호가 서로 다릅니다.");
                    editPassword2.requestFocus();
                    return;
                }

                showProgress();  // 프로그래스바 실행

                Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterActivity.this);
                UserApi api = retrofit.create(UserApi.class);
                User user = new User(email, password1,name, nickname);

                Call<UserRes> call = api.register(user);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        dismissProgress();
                        if (response.isSuccessful()) {

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();


                        }else if(response.code() == 401) {
                            layoutEmail.setError("이메일의 형식이 잘못되었습니다.");
                            editEmail.requestFocus();
                        }else if(response.code()== 402) {
                            layoutPassword1.setError("비밀번호는 최소 6자리 이상입니다.");
                            editPassword1.requestFocus();
                        }else if (response.code() == 403) {
                            layoutEmail.setError("이미 사용중인 이메일입니다.");
                            editEmail.requestFocus();
                        }else if (response.code() == 405) {
                            layoutNickname.setError("이미 사용중인 닉네임 입니다.");
                            editNickName.requestFocus();
                        }else if (response.code() == 405) {
                            Snackbar.make(btnRegister,
                                    "서버에 알수 없는 오류가 생겼습니다.",
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        dismissProgress();
                        Snackbar.make(btnRegister,
                                "서버에 접근할 수가 없습니다.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });


            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    Dialog dialog;

    void showProgress(){
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(new ProgressBar(this));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    void dismissProgress(){
        dialog.dismiss();
    }
}