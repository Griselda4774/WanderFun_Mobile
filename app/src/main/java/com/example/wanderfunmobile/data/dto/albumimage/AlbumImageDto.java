package com.example.wanderfunmobile.data.dto.albumimage;

public class AlbumImageDto {
    private Long id;
    private String imageUrl;
    private String imagePublicId;
    private Long albumId;

    public AlbumImageDto() {};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public String setImagePublicId(String imagePublicId) {
        return this.imagePublicId = imagePublicId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
}
