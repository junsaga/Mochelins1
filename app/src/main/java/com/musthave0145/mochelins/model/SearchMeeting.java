package com.musthave0145.mochelins.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchMeeting {
    public int id;
    public int userId;
    public int storeId;
    public String content;
    public String date;
    public String photoURL;
    public int maximum;
    public String createdAt;
    public String updatedAt;
    public String profile;
    public int attend;
    public ArrayList<Meeting> profiles;
    public static class Meeting implements Serializable {
        public String profile;
    }

    // 생성자, Getter 및 Setter 등 필요한 메서드 추가
}
