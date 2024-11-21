package com.example.domain.models;

import java.util.Date;
import java.util.List;

public class Feedback {
    private Long id;
    private String userName;
    private float rating;
    private String comment;
    private List<String> imageUrls;
    private Date timeArrived;

    public Feedback() {
    }

    public Long getId() {
        return id;
    }

    public Date getTimeArrived() {
        return timeArrived;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getComment() {
        return comment;
    }

    public float getRating() {
        return rating;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimeArrived(Date timeArrived) {
        this.timeArrived = timeArrived;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
