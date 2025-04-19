package com.example.wanderfunmobile.data.dto.placeimage;

public class PlaceImageCreateDto {
    private String imageUrl;
    private String imagePublicId;

    public PlaceImageCreateDto() {};

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
