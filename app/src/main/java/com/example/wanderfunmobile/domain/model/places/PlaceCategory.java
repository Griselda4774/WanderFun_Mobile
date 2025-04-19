package com.example.wanderfunmobile.domain.model.places;

import com.example.wanderfunmobile.domain.model.images.Image;

public class PlaceCategory {
    private Integer id;
    private String name;
    private String nameEn;
    private Image iconImage;

    public PlaceCategory() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Image getIconImage() {
        return iconImage;
    }

    public void setIconImage(Image iconImage) {
        this.iconImage = iconImage;
    }
}
