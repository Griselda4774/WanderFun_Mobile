package com.example.wanderfunmobile.data.dto.place;

import com.example.wanderfunmobile.data.dto.images.ImageDto;

public class FeedbackCreateDto {
    private int rating;
    private String content;
    private ImageDto image;

    public FeedbackCreateDto() {
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
