package com.musthave0145.mochelins.model;

import java.io.Serializable;
import java.util.Date;

public class Store implements Serializable {
    public int id;
    public int storeId;
    public int userId;
    public String content;
    public String storeName;
    public String storeAddr;
    public int view;
    public double rating;
    public String photo;
    public String createdAt;
    public String updatedAt;
    public double storeLat;
    public double storeLng;
    public int likeCnt;
    public int isLike;
    public String summary;


    public double getRating() {
        return Math.floor(this.rating * 10) / 10.0;
    }

}
