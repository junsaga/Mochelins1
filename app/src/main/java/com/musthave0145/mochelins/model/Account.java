package com.musthave0145.mochelins.model;

import com.google.gson.annotations.SerializedName;

public class Account {
     @SerializedName("storeName")
     private String storeName;

     @SerializedName("price")
     private int price;

     @SerializedName("payment")
     private String payment;

     @SerializedName("content")
     private String content;

     @SerializedName("date")
     private String date;

     @SerializedName("accountBooksId")
     private int accountBooksId;
     public Account(String storeName, int price, String payment, String content, String date, int accountBooksId) {
          this.storeName = storeName;
          this.price = price;
          this.payment = payment;
          this.content = content;
          this.date = date;
          this.accountBooksId = accountBooksId;
     }
}
