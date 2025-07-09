package com.example.wanderfunmobile.data.dto.place;

import android.media.Image;

import com.example.wanderfunmobile.data.dto.images.ImageDto;

import org.parceler.Parcel;

@Parcel
public class PlaceCategoryDto {

    private Integer id;
    private String name;
    private String nameEn;
    private ImageDto iconImage;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public ImageDto getIconImage() {
        return iconImage;
    }

    public void setIconImage(ImageDto iconImage) {
        this.iconImage = iconImage;
    }
}

