package com.example.wanderfunmobile.domain.model.places;

import com.example.wanderfunmobile.domain.model.images.Image;

public class Section {
    private Long id;
    private String title;
    private String content;
    private Image image;
    private Long placeDetailId;

    public Section() {
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Long getPlaceDetailId() {
        return placeDetailId;
    }

    public void setPlaceDetailId(Long placeDetailId) {
        this.placeDetailId = placeDetailId;
    }
}
