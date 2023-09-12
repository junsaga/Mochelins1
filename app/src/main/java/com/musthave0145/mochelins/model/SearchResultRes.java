package com.musthave0145.mochelins.model;

import java.io.Serializable;
import java.util.ArrayList;

    public class SearchResultRes {
        public String result;
        public Item item;

        public static class Item implements Serializable{
            public Store store;
            public User user;
            public Review review;
            public Meeting meeting;
            public static class Store implements Serializable {
                public int storeId;
                public String storeName;
                public String storeAddr;
                public double storeLat;
                public double storeLng;
                public double rating;

                public Store(int storeId, String storeName, String storeAddr, double storeLat, double storeLng, double rating) {
                    this.storeId = storeId;
                    this.storeName = storeName;
                    this.storeAddr = storeAddr;
                    this.storeLat = storeLat;
                    this.storeLng = storeLng;
                    this.rating = rating;
                }

                public int getStoreId() {
                    return storeId;
                }

                public String getStoreName() {
                    return storeName;
                }

                public String getStoreAddr() {
                    return storeAddr;
                }

                public double getStoreLat() {
                    return storeLat;
                }

                public double getStoreLng() {
                    return storeLng;
                }

                public double getRating() {
                    return rating;
                }
            }



            public static class User implements Serializable {
                public int id;
                public String email;
                public String nickname;
                public String name;
                public String profile;
                public String createdAt;
                public String updatedAt;
                public double rating;
                public ArrayList<Review> reviews;
                public static class Review implements Serializable {
                    public String photo;
                }
                public int reviewsCont;
            }

            public static class Review implements Serializable{
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
            }

            public static class Meeting implements Serializable{
                public int id;
                public int userId;
                public int storeId;
                public String content;
                public String date;
                public String photoURL;
                public int maximun;
                public String createdAt;
                public String updatedAt;
                public String profile;
                public int attend;
                public ArrayList<Profiles> profiles;
                public static class Profiles implements Serializable {
                    public String profile;
                }
            }
        }



    }

