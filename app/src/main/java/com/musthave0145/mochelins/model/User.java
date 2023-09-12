package com.musthave0145.mochelins.model;

import java.io.Serializable;

public class User implements Serializable {

    public int id;
    public String email;
    public String password;

    public String name;

    public String profile;

    public String nickname;
    public int isMine = 0;

    public long kakaoId;

    public User() {
    }

    public User(String email, String password, String name, String nickname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String name, String profile, long kakaoId) {
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.kakaoId = kakaoId;
    }
}