package com.musthave0145.mochelins.model;

import java.io.Serializable;

public class MapData implements Serializable {
    public int storeId;
    public String storeName;
    public double storeLat;
    public double storeLng;
    public double rating;

    public double getRating() {
        return Math.floor(this.rating * 10) / 10.0;
    }
}