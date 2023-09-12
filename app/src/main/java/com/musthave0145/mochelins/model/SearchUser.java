package com.musthave0145.mochelins.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchUser implements Serializable {
    public int id;
    public String email;
    public String nickname;
    public String name;
    public String profile;
    public String createdAt;
    public String updatedAt;
    public double rating;
    public int reviewsCnt;
    public ArrayList<Review> reviews;
    public static class Review implements Serializable {
        public String photo;
    }
}
