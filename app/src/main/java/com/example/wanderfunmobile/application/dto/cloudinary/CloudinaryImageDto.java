package com.example.wanderfunmobile.application.dto.cloudinary;

public class CloudinaryImageDto {
    private String publicId;
    private String url;

    public CloudinaryImageDto() {
    }

    public CloudinaryImageDto(String publicId, String url) {
        this.publicId = publicId;
        this.url = url;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
