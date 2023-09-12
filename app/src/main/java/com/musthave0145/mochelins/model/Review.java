package com.musthave0145.mochelins.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Review implements Serializable {

    public int id;
    public int storeId;
    public int userId;
    public String content;
    public int view;
    public int rating;
    public String createdAt;
    public String updatedAt;

    public String nickname;
    public String profile;

    public String storeName;
    public String storeAddr;

    public double storeLat;
    public double storeLng;

    public String photo;
    public int commentCnt;
    public int likeCnt;
    public int isLike;
    public double distance;
    public int likesCnt;
    public int isMine;
    public int reviewCnt;

    public Review() {
    }

    public Review(int id, int storeId) {
        this.id = id;
        this.storeId = storeId;
    }

    public ArrayList<Photos> photos = new ArrayList<>();

    public class Photos implements Serializable{
        public String photo;
    }


    public ArrayList<tagName> tags = new ArrayList<>();

    public class tagName implements Serializable{
        public String name;
    }

}
