package com.musthave0145.mochelins.model;

public class SearchItem {
     public SearchStore store;
     public SearchUser user;
     public SearchReview review;
     public SearchMeeting meeting;
    // 생성자, Getter 및 Setter 등 필요한 메서드 추가


    public SearchItem(SearchStore store, SearchUser user, SearchReview review, SearchMeeting meeting) {
        this.store = store;
        this.user = user;
        this.review = review;
        this.meeting = meeting;
    }
}
