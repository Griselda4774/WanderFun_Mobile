package com.example.wanderfunmobile.network.dto.feedback;

import com.example.wanderfunmobile.network.dto.feedbackimage.FeedbackImageCreateDto;

import java.util.Date;
import java.util.List;

public class FeedbackCreateDto {
    private String userName;
    private String userAvatar;
    private float rating;
    private String comment;
    private List<FeedbackImageCreateDto> feedbackImages;
    private Date time;
    private Long placeId;

    public FeedbackCreateDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FeedbackImageCreateDto> getFeedbackImages() {
        return feedbackImages;
    }

    public void setFeedbackImages(List<FeedbackImageCreateDto> feedbackImages) {
        this.feedbackImages = feedbackImages;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }
}
