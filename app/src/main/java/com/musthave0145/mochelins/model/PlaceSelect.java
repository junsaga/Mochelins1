package com.musthave0145.mochelins.model;

import java.io.Serializable;

public class PlaceSelect implements Serializable {

    public double storeLat;
    public double storeLng;

    public String storeName;
    public String storeAddr;

    public PlaceSelect() {
    }

    public PlaceSelect(double storeLat, double storeLng, String storeName, String storeAddr) {
        this.storeLat = storeLat;
        this.storeLng = storeLng;
        this.storeName = storeName;
        this.storeAddr = storeAddr;
    }
}


