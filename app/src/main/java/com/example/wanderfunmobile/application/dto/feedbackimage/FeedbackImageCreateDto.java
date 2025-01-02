package com.example.wanderfunmobile.application.dto.feedbackimage;

public class FeedbackImageCreateDto {
    private String imageUrl;
    private String imagePublicId;

    public FeedbackImageCreateDto() {};

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }
}
