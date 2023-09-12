package com.musthave0145.mochelins.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Meeting implements Serializable {
    public int id;
    public int userId;
    public int storeId;
    public String content;
    public String date;
    public String photo;
    public String profile;
    public String storeName;
    public String storeAddr;
    public double storeLat;
    public double storeLng;
    public int pay;
    public int isMine;
    public int isAttend;
    public double distance;
    public int attend;
    public int maximum;
    public String nickname;

    public ArrayList<User> profiles;

}
