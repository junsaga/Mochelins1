package com.musthave0145.mochelins.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.musthave0145.mochelins.R;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    ImageView btnPlus;
    ImageView btnMinus;
    EditText editDistance;

    ArrayList<Integer> userFoodSelcetedList = new ArrayList<>();


//    Integer[] btnFoodInteger = {
//            R.id.btnFoodAll, R.id.btnChicken, R.id.btnChina, R.id.btnBurger, R.id.btnKorean,
//            R.id.btnCafe, R.id.btnBunsik, R.id.btnPizza, R.id.btnJapan, R.id.btnJokbal,
//            R.id.btnZzim, R.id.btnSandwich, R.id.btnSushi,
//            R.id.btnAsian, R.id.btnGogi, R.id.btnSalad, R.id.btnDosirak, R.id.btnYasik
//    };
//    CardView[] btnFoodSelectArray = new CardView[btnFoodInteger.length];

    Integer[] btnStarInteger = {R.id.starBtnNone, R.id.starBtnUp,R.id.starBtnDown};
    CardView[] btnStarSelectArray = new CardView[btnStarInteger.length];

    Integer[] imgStarInteger = {R.id.starImgNone, R.id.ImgBubble,R.id.starImgDown};
    ImageView[] imgStarSelectArray = new ImageView[imgStarInteger.length];

    Integer[] txtStarInteger = {R.id.starTxtNone, R.id.txtStoreName,R.id.starTxtDown};
    TextView[] txtStarSelectArray = new TextView[txtStarInteger.length];

    Integer[] btnLikeInteger = {R.id.likeBtnNone, R.id.likeBtnUp, R.id.likeBtnDown};
    CardView[] btnLikeSelectArray = new CardView[btnLikeInteger.length];

    Integer[] imgLikeInteger = {R.id.likeImgNone, R.id.likeImgUp, R.id.likeImgDown};
    ImageView[] imgLikeSelectArray = new ImageView[imgLikeInteger.length];

    Integer[] txtLikeInteger = {R.id.likeTxtNone, R.id.likeTxtUp, R.id.likeTxtDown};
    TextView[] txtLikeSelectArray = new TextView[txtLikeInteger.length];

    Integer[] btnViewInteger = {R.id.viewBtnNone, R.id.viewBtnUp, R.id.viewBtnDown};
    CardView[] btnViewSelectArray = new CardView[btnViewInteger.length];

    Integer[] imgViewInteger = {R.id.viewImgNone, R.id.viewImgUp, R.id.viewImgDown};
    ImageView[] imgViewSelectArray = new ImageView[imgViewInteger.length];

    Integer[] txtViewInteger = {R.id.viewTxtNone, R.id.viewTxtUp, R.id.viewTxtDown};
    TextView[] txtViewSelectArray = new TextView[txtViewInteger.length];


//
//    Integer[] imgInteger = {R.id.starImgNone, R.id.starImgUp,R.id.starImgDown,
//            R.id.likeImgNone, R.id.likeImgUp, R.id.likeImgDown,
//            R.id.viewImgNone, R.id.viewBtnUp, R.id.viewBtnDown
//    };
//    ImageView[] imgSelectArray = new ImageView[imgInteger.length];
//
//    Integer[] txtInteger = {R.id.starTxtNone, R.id.starTxtUp,R.id.starTxtDown,
//            R.id.likeTxtNone, R.id.likeTxtUp, R.id.likeTxtDown,
//            R.id.viewTxtNone, R.id.viewTxtUp, R.id.viewTxtDown
//    };
//    TextView[] txtSelectArray = new TextView[txtInteger.length];

//
//    CardView starBtnNone;
//    CardView starBtnUp;
//    CardView starBtnDown;
//
//    ImageView starImgNone;
//    ImageView starImgUp;
//    ImageView starImgDown;
//
//    TextView starTxtNone;
//    TextView starTxtUp;
//    TextView starTxtDown;
//
//    CardView likeBtnNone;
//    CardView likeBtnUp;
//    CardView likeBtnDown;
//
//    ImageView likeImgNone;
//    ImageView likeImgUp;
//    ImageView likeImgDown;
//
//    TextView likeTxtNone;
//    TextView likeTxtUp;
//    TextView likeTxtDown;
//
//    CardView viewBtnNone;
//    CardView viewBtnUp;
//    CardView viewBtnDown;
//
//    ImageView viewImgNone;
//    ImageView viewImgUp;
//    ImageView viewImgDown;
//
//    TextView viewTxtNone;
//    TextView viewTxtUp;
//    TextView viewTxtDown;

//    CardView btnFoodAll, btnChicken, btnChina, btnBurger,
//            btnKorean, btnCafe, btnBunsik, btnPizza,
//            btnJapan, btnJokbal, btnZzim, btnSandwich,
//            btnSushi, btnAsian, btnGogi, btnSalad,
//            btnDosirak, btnYasik;
//
//    ImageView imgFoodAll, imgChicken, imgChina, imgBurger,
//            imgKorean, imgCafe, imgBunsik, imgPizza,
//            imgJapan, imgJokbal, imgZzim, imgSandwich,
//            imgSushi, imgAsian, imgGogi, imgSalad,
//
//    TextView txtFoodAll, txtChicken, txtChina, txtBurger,
//            txtKorean, txtCafe, txtBunsik, txtPizza,
//            txtJapan, txtJokbal, txtZzim, txtSandwich,
//            txtSushi, txtAsian,  txtGogi, txtSalad;




    Integer[] btnFoodInteger = {
            R.id.btnFoodAll, R.id.btnChicken, R.id.btnChina, R.id.btnBurger, R.id.btnKorean,
            R.id.btnCafe, R.id.btnBunsik, R.id.btnPizza, R.id.btnJapan, R.id.btnJokbal,
            R.id.btnZzim, R.id.btnSandwich, R.id.btnSushi,
            R.id.btnAsian, R.id.btnGogi, R.id.btnSalad, R.id.btnDosirak, R.id.btnYasik
    };
    CardView[] btnFoodSelectArray = new CardView[btnFoodInteger.length];

    Integer[] imgFoodInteger = {
            R.id.imgFoodAll, R.id.imgChicken, R.id.imgChina, R.id.imgBurger, R.id.imgKorean,
            R.id.imgCafe, R.id.imgBunsik, R.id.imgPizza, R.id.imgJapan, R.id.imgJokbal,
            R.id.imgZzim, R.id.imgSandwich, R.id.imgSushi,
            R.id.imgAsian, R.id.imgGogi, R.id.imgSalad, R.id.imgDosirak, R.id.imgYasik
    };
    ImageView[] imgFoodSelectArray = new ImageView[imgFoodInteger.length];

    Integer[] txtFoodInteger = {
            R.id.txtFoodAll, R.id.txtChicken, R.id.txtChina, R.id.txtBurger, R.id.txtKorean,
            R.id.txtCafe, R.id.txtBunsik, R.id.txtPizza, R.id.txtJapan, R.id.txtJokbal,
            R.id.txtZzim, R.id.txtSandwich, R.id.txtSushi,
            R.id.txtAsian, R.id.txtGogi, R.id.txtSalad, R.id.txtDosirak, R.id.txtYasik
    };
    TextView[] txtFoodSelectArray = new TextView[txtFoodInteger.length];
    ImageView imgSave;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        btnMinus = findViewById(R.id.btnStar1);
        btnPlus = findViewById(R.id.btnStar5);
        editDistance = findViewById(R.id.editPerson);
        imgSave = findViewById(R.id.imgSave);


        for (int i = 0; i < btnStarInteger.length ; i++){
            btnStarSelectArray[i] = findViewById(btnStarInteger[i]);
            imgStarSelectArray[i] = findViewById(imgStarInteger[i]);
            txtStarSelectArray[i] = findViewById(txtStarInteger[i]);

            btnLikeSelectArray[i] = findViewById(btnLikeInteger[i]);
            imgLikeSelectArray[i] = findViewById(imgLikeInteger[i]);
            txtLikeSelectArray[i] = findViewById(txtLikeInteger[i]);

            btnViewSelectArray[i] = findViewById(btnViewInteger[i]);
            imgViewSelectArray[i] = findViewById(imgViewInteger[i]);
            txtViewSelectArray[i] = findViewById(txtViewInteger[i]);
        }


        // 음식 버튼, 이미지, 글자 화면 연결로직
        for(int i = 0; i < btnFoodInteger.length ; i++){
            btnFoodSelectArray[i] = findViewById(btnFoodInteger[i]);
            imgFoodSelectArray[i] = findViewById(imgFoodInteger[i]);
            txtFoodSelectArray[i] = findViewById(txtFoodInteger[i]);
        }

        // 처음 버튼(전체)을 눌렀을 때 선택된 다른 버튼은 눌린게 풀리고, 전체선택 버튼만 눌리게 하는 로직
        btnFoodSelectArray[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgFoodSelectArray[0].setImageResource(R.drawable.button_on);
                txtFoodSelectArray[0].setTextColor(Color.WHITE);

                for(int i = 1; i < btnFoodInteger.length ; i++){
                    imgFoodSelectArray[i].setImageResource(R.drawable.button_off);
                    txtFoodSelectArray[i].setTextColor(Color.BLACK);
                }

            }
        });

        //
        for(int i = 1; i < btnFoodInteger.length ; i++){
            int finalI = i;
            btnFoodSelectArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgFoodSelectArray[0].setImageResource(R.drawable.button_off);
                    txtFoodSelectArray[0].setTextColor(Color.BLACK);

                    imgFoodSelectArray[finalI].setImageResource(R.drawable.button_on);
                    txtFoodSelectArray[finalI].setTextColor(Color.WHITE);
                }
            });
        }

        // 평점, 좋아요, 조회수 선택시 함수
        filterButtonClick(btnStarSelectArray, imgStarSelectArray, txtStarSelectArray);
        filterButtonClick(btnViewSelectArray, imgViewSelectArray, txtViewSelectArray);
        filterButtonClick(btnLikeSelectArray, imgLikeSelectArray, txtLikeSelectArray);

//
//        starBtnNone = findViewById(R.id.starBtnNone);
//        starBtnUp = findViewById(R.id.starBtnUp);
//        starBtnDown = findViewById(R.id.starBtnDown);
//
//        starImgNone = findViewById(R.id.starImgNone);
//        starImgUp = findViewById(R.id.starImgUp);
//        starImgDown = findViewById(R.id.starImgDown);
//
//        starTxtNone = findViewById(R.id.starTxtNone);
//        starTxtUp = findViewById(R.id.starTxtUp);
//        starTxtDown = findViewById(R.id.starTxtDown);

        //////////////////////////// like
//        likeBtnNone = findViewById(R.id.likeBtnNone);
//        likeBtnUp = findViewById(R.id.likeBtnUp);
//        likeBtnDown = findViewById(R.id.likeBtnDown);
//
//        likeImgNone = findViewById(R.id.likeImgNone);
//        likeImgUp = findViewById(R.id.likeImgUp);
//        likeImgDown = findViewById(R.id.likeImgDown);
//
//        likeTxtNone = findViewById(R.id.likeTxtNone);
//        likeTxtUp = findViewById(R.id.likeTxtUp);
//        likeTxtDown = findViewById(R.id.likeTxtDown);

        ////////////////////////////// view
//        viewBtnNone = findViewById(R.id.viewBtnNone);
//        viewBtnUp = findViewById(R.id.viewBtnUp);
//        viewBtnDown = findViewById(R.id.viewBtnDown);
//
//        viewImgNone = findViewById(R.id.viewImgNone);
//        viewImgUp = findViewById(R.id.viewImgUp);
//        viewImgDown = findViewById(R.id.viewImgDown);
//
//        viewTxtNone = findViewById(R.id.viewTxtNone);
//        viewTxtUp = findViewById(R.id.viewTxtUp);
//        viewTxtDown = findViewById(R.id.viewTxtDown);
//        filterButtonClick(btnStarSelectArray[0],btnStarSelectArray[1],btnStarSelectArray[2],
//                 imgStarSelectArray[0],imgStarSelectArray[1], imgStarSelectArray[2],
//                 txtStarSelectArray[0], txtStarSelectArray[1], txtStarSelectArray[2]);
//
//        filterButtonClick(btnLikeSelectArray[0],btnLikeSelectArray[1],btnLikeSelectArray[2],
//                imgLikeSelectArray[0],imgLikeSelectArray[1], imgLikeSelectArray[2],
//                txtLikeSelectArray[0], txtLikeSelectArray[1], txtLikeSelectArray[2]);
//
//        filterButtonClick(btnViewSelectArray[0],btnViewSelectArray[1],btnViewSelectArray[2],
//                imgViewSelectArray[0],imgViewSelectArray[1], imgViewSelectArray[2],
//                txtViewSelectArray[0], txtViewSelectArray[1], txtViewSelectArray[2]);

//        for (int i = 0; i < btnStarInteger.length; i++){
//        }
//
        // btn


//        filterButtonClick(btnStarSelectArray[0],btnLikeSelectArray[1],btnViewSelectArray[2],
//                imgStarSelectArray[0],imgLikeSelectArray[1], imgViewSelectArray[2],
//                txtStarSelectArray[0], txtLikeSelectArray[1], txtViewSelectArray[2]);

//        filterButtonClick(starBtnNone, starBtnUp, starBtnDown,
//                starImgNone, starImgUp, starImgDown,
//                starTxtNone, starTxtUp, starTxtDown);
//
//        filterButtonClick(likeBtnNone, likeBtnUp, likeBtnDown,
//                likeImgNone, likeImgUp, likeImgDown,
//                likeTxtNone, likeTxtUp, likeTxtDown);
//
//        filterButtonClick(viewBtnNone, viewBtnUp, viewBtnDown,
//                viewImgNone, viewImgUp, viewImgDown,
//                viewTxtNone, viewTxtUp, viewTxtDown);

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strDistance = editDistance.getText().toString().trim();
                double distance = Double.parseDouble(strDistance);
                if (distance > 0.5) {
                    distance = distance - 0.5;
                    editDistance.setText(distance+"");
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strDistance = editDistance.getText().toString().trim();
                double distance = Double.parseDouble(strDistance);
                if (distance < 5) {
                    distance = distance + 0.5;
                    editDistance.setText(distance+"");
                }
            }
        });

        editDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력 전에 수행
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력 중에 수행
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력 후에 수행할 작업
                String text = editable.toString();
                double value;

                try {
                    value = Double.parseDouble(text);
                    if (value < 0.5 || value > 5.0) {
                        editDistance.setError("0.5부터 5.0 사이의 값을 입력하세요.");
                    } else {
                        editDistance.setError(null); // 에러 메시지 제거
                    }
                } catch (NumberFormatException e) {
                    editDistance.setError("올바른 숫자를 입력하세요.");
                }
            }
        });

//        starBtnNone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                starImgNone.setImageResource(R.drawable.button_on);
//                starImgUp.setImageResource(R.drawable.button_off);
//                starImgDown.setImageResource(R.drawable.button_off);
//
//                starTxtNone.setTextColor(Color.WHITE);
//                starTxtUp.setTextColor(Color.BLACK);
//                starTxtDown.setTextColor(Color.BLACK);
//
//
//            }
//        });
//
//        starBtnUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                starImgNone.setImageResource(R.drawable.button_off);
//                starImgUp.setImageResource(R.drawable.button_on);
//                starImgDown.setImageResource(R.drawable.button_off);
//
//                starTxtNone.setTextColor(Color.BLACK);
//                starTxtUp.setTextColor(Color.WHITE);
//                starTxtDown.setTextColor(Color.BLACK);
//            }
//        });
//
//        starBtnDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                starImgNone.setImageResource(R.drawable.button_off);
//                starImgUp.setImageResource(R.drawable.button_off);
//                starImgDown.setImageResource(R.drawable.button_on);
//
//                starTxtNone.setTextColor(Color.BLACK);
//                starTxtUp.setTextColor(Color.BLACK);
//                starTxtDown.setTextColor(Color.WHITE);
//
//            }
//        });
    }

    //원준님 그저 빛/...
    // 선택한 것은,

    void filterButtonClick(CardView[] cardViewArray, ImageView[] imageViewArray, TextView[] textViewArray){
        for (int i = 0; i < cardViewArray.length; i++) {
            int finalI = i;
            cardViewArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int j = 0; j < imageViewArray.length; j++) {
                        if(j == finalI){
                            imageViewArray[j].setImageResource(R.drawable.button_on);
                            textViewArray[j].setTextColor(Color.WHITE);
                        } else {
                            imageViewArray[j].setImageResource(R.drawable.button_off);
                            textViewArray[j].setTextColor(Color.BLACK);
                        }
                    }
                }
            });
        }
//        cardView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                imageView1.setImageResource(R.drawable.button_on);
//                imageView2.setImageResource(R.drawable.button_off);
//                imageView3.setImageResource(R.drawable.button_off);
//
//                textView1.setTextColor(Color.WHITE);
//                textView2.setTextColor(Color.BLACK);
//                textView3.setTextColor(Color.BLACK);
//
//
//            }
//        });
//
//        cardView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                imageView1.setImageResource(R.drawable.button_off);
//                imageView2.setImageResource(R.drawable.button_on);
//                imageView3.setImageResource(R.drawable.button_off);
//
//                textView1.setTextColor(Color.BLACK);
//                textView2.setTextColor(Color.WHITE);
//                textView3.setTextColor(Color.BLACK);
//            }
//        });
//
//        cardView3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                imageView1.setImageResource(R.drawable.button_off);
//                imageView2.setImageResource(R.drawable.button_off);
//                imageView3.setImageResource(R.drawable.button_on);
//
//                textView1.setTextColor(Color.BLACK);
//                textView2.setTextColor(Color.BLACK);
//                textView3.setTextColor(Color.WHITE);
//
//            }
//        });
    }

}

