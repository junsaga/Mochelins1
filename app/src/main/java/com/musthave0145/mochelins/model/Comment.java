package com.musthave0145.mochelins.model;

import java.io.Serializable;

public class Comment implements Serializable {
    public int id;
    public int userId;
    public int reviewId;
    public String content;
    public String createdAt;
    public String updatedAt;
    public String nickname;
    public String profile;
    public int isMine;

    public Comment(String content) {
        this.content = content;
    }
}
