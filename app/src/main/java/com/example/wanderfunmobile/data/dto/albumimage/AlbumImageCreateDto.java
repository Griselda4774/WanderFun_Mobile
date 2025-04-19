package com.example.wanderfunmobile.data.dto.albumimage;

public class AlbumImageCreateDto {
    private String imageUrl;
    private String imagePublicId;

    public AlbumImageCreateDto() {};

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
