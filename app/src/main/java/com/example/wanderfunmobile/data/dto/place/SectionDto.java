package com.example.wanderfunmobile.data.dto.place;

import com.example.wanderfunmobile.data.dto.images.ImageDto;

public class SectionDto {
    private Long id;
    private String title;
    private String content;
    private ImageDto image;
    private Long placeDetailId;

    public SectionDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getPlaceDetailId() {
        return placeDetailId;
    }

    public void setPlaceDetailId(Long placeDetailId) {
        this.placeDetailId = placeDetailId;
    }
}
