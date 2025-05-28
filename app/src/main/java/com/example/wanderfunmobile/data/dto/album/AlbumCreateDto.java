package com.example.wanderfunmobile.data.dto.album;

import com.example.wanderfunmobile.data.dto.images.ImageDto;

import java.util.List;

public class AlbumCreateDto {
    private String name;
    private String description;
    private ImageDto coverImage;
    private List<AlbumImageDto> albumImageList;
    private Long placeId;

    public AlbumCreateDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageDto getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(ImageDto coverImage) {
        this.coverImage = coverImage;
    }

    public List<AlbumImageDto> getAlbumImageList() {
        return albumImageList;
    }

    public void setAlbumImageList(List<AlbumImageDto> albumImageList) {
        this.albumImageList = albumImageList;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }
}
