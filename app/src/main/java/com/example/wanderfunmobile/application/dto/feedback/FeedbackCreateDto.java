package com.example.wanderfunmobile.application.dto.feedback;

import com.example.wanderfunmobile.application.dto.feedbackimage.FeedbackImageCreateDto;

import java.util.Date;
import java.util.List;

public class FeedbackCreateDto {
    private float rating;
    private String comment;
    private List<FeedbackImageCreateDto> feedbackImages;

    public FeedbackCreateDto() {
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
}
