package com.musthave0145.mochelins.model;

import java.io.Serializable;

public class SearchReview implements Serializable {
    public int id;
    public int storeId;
    public int userId;
    public String content;
    public int view;
    public double rating;
    public String photo;
    public String createdAt;
    public String updatedAt;
    public String storeName;
    public String storeAddr;
    public double storeLat;
    public double storeLng;
    public int likeCnt;
    public int isLike;

    // 생성자, Getter 및 Setter 등 필요한 메서드 추가


}
