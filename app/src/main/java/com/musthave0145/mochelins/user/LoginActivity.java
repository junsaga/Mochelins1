package com.musthave0145.mochelins.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;
import com.musthave0145.mochelins.MainActivity;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.User;
import com.musthave0145.mochelins.model.UserRes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.BuildConfig;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout layoutEmail;
    TextInputLayout layoutPassword;

    EditText editEmail;
    EditText editPassword;
    Button btnLogin;
    TextView txtRegister;

    Switch autoLogin;
    Boolean isAutoLogin;
    ImageView btnClose;

    ImageButton btnKakaoLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        btnClose = findViewById(R.id.btnClose);

        btnKakaoLogin = findViewById(R.id.btnKakaoLogin);

        editEmail.setFocusableInTouchMode(true);
        editPassword.setFocusableInTouchMode(true);

        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);

        // 카카오 로그인
        String kakaoKey = getApplicationContext().getResources().getString(R.string.KAKAO_APP_KEY);
        KakaoSdk.init(this, kakaoKey);
        Log.e("getKeyHash", ""+getKeyHash(LoginActivity.this));

        btnKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(AuthApiClient.getInstance().hasToken()) {
                    UserApiClient.getInstance().accessTokenInfo(new Function2<AccessTokenInfo, Throwable, Unit>() {
                        @Override
                        public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {
                            if (throwable != null) {
                                UserApiClient.getInstance().unlink(new Function1<Throwable, Unit>() {
                                    @Override
                                    public Unit invoke(Throwable throwable) {
                                        kakao_login_api();
                                        return null;
                                    }
                                });
                            }
                            else {
                                //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                                kakoLogin();
                            }
                            return null;
                        }
                    });
                }else {
                    kakao_login_api();
                }
            }
        });

        // 자동 로그인 활성화/비활성화
        autoLogin = findViewById(R.id.autoLogin);
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAutoLogin = b;
                if(isAutoLogin){
                    autoLogin.setTextColor(Color.GREEN);
                }else {
                    autoLogin.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorUnpressed));
                }
            }
        });

        // 자동 로그인 후 저장된 부분 확인
            // 있다면 불러오자.
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        isAutoLogin = sp.getBoolean(Config.SAVE_AUTO, false);

        if(isAutoLogin){
            String email = sp.getString(Config.SAVE_EMAIL, "");
            String password = sp.getString(Config.SAVE_PASSWORD, "");

            editEmail.setText(email);
            editPassword.setText(password);
            autoLogin.setChecked(isAutoLogin);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim(); // 비번 데이터 가져오기

                layoutEmail.setError(null);
                layoutPassword.setError(null);

                Pattern pattern = Patterns.EMAIL_ADDRESS; // 여러 패턴 중에 하나의 패턴 꺼집어낸다.
                if (pattern.matcher(email).matches() == false) {
                    layoutEmail.setError("정상적인 이메일 형식을 작성해야합니다.");
                    editEmail.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    layoutPassword.setError("비밀번호는 최소 6자리 이상입니다.");
                    editPassword.requestFocus();
                    return;
                }

                showProgress();

                Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
                UserApi api = retrofit.create(UserApi.class);
                User user = new User(email, password);
                Call<UserRes> call = api.login(user);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        dismissProgress();
                        if (response.isSuccessful()) {
                            // 200 OK
                            UserRes res = response.body();
                            SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            // 파싱할때 철자 똑바로 보자. 무조건 복붙!
                            editor.putString(Config.ACCESS_TOKEN, res.accessToken);

                            // 자동 로그인 활성화 했을시, 저장한다.
                            if(isAutoLogin){
                                editor.putString(Config.SAVE_EMAIL, email);
                                editor.putString(Config.SAVE_PASSWORD, password);
                                editor.putBoolean(Config.SAVE_AUTO, isAutoLogin);
                            }

                            editor.apply();


                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            finish();


                        } else if (response.code() == 400){

                            layoutEmail.setError("등록된 이메일이 아닙니다.");
                            editEmail.requestFocus();

                        } else if (response.code() == 401){

                            layoutPassword.setError("비밀번호가 다릅니다.");
                            editPassword.requestFocus();

                        } else {
                            Snackbar.make(btnLogin,
                                    "서버에 알 수 없는 오류가 발생했습니다.",
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        dismissProgress();
                        Snackbar.make(btnLogin,
                                "서버 통신에 문제가 발생했습니다.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();

        if(isAutoLogin){
            btnLogin.performClick();
        }
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


    private void kakoLogin(){

        UserApiClient.getInstance().me(new Function2<com.kakao.sdk.user.model.User, Throwable, Unit>() {
            @Override
            public Unit invoke(com.kakao.sdk.user.model.User kakao_user, Throwable throwable) {
                if(kakao_user != null){

                    showProgress();

                    long kakaoId = kakao_user.getId();
                    String email = kakao_user.getKakaoAccount().getEmail();
                    String name = kakao_user.getKakaoAccount().getProfile().getNickname();
                    String profile = kakao_user.getKakaoAccount().getProfile().getProfileImageUrl();


                    User user = new User(email,name, profile, kakaoId);

                    Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
                    UserApi api = retrofit.create(UserApi.class);
                    Call<UserRes> call = api.login_kakao(user);

                    call.enqueue(new Callback<UserRes>() {
                        @Override
                        public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                            dismissProgress();

                            if(response.isSuccessful()){

                                String token = response.body().accessToken;

                                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(Config.ACCESS_TOKEN, token);
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserRes> call, Throwable t) {
                            dismissProgress();
                        }
                    });


                }else {

                }

                if(throwable != null){

                }

                return null;
            }
        });
    }

    private void kakao_login_api(){
        Function2<OAuthToken, Throwable, Unit> function = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null){
                    kakoLogin();
                }

                if(throwable != null ){
//                    Log.i("카카오", throwable.toString());
                }

                return null;
            }
        };

        if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
            UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, function );
        }else {
            UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, function );
        }
    }

    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}