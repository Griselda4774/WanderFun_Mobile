package com.example.wanderfunmobile.data.dto.place;

import com.example.wanderfunmobile.data.dto.images.ImageDto;

import org.parceler.Parcel;

@Parcel
public class MiniPlaceDto {
    private Long id;
    private String name;
    private ImageDto coverImage;

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

    public ImageDto getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(ImageDto coverImage) {
        this.coverImage = coverImage;
    }
}
