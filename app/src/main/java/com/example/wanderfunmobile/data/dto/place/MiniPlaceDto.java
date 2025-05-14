package com.example.wanderfunmobile.data.dto.place;

import com.example.wanderfunmobile.domain.model.images.Image;

public class MiniPlaceDto {
    private Long id;
    private String name;
    private Image coverImage;

    public MiniPlaceDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Image getCoverImage() {
        return coverImage;
    }
    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }
}
