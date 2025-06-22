package com.example.wanderfunmobile.data.dto.place;

import com.example.wanderfunmobile.data.dto.images.ImageDto;

public class FeedbackCreateDto {
    private float rating;
    private String content;
    private ImageDto image;

    public FeedbackCreateDto() {
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }
}
