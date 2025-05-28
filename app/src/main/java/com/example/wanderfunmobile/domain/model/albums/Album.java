package com.example.wanderfunmobile.domain.model.albums;

import com.example.wanderfunmobile.domain.model.images.Image;
import com.example.wanderfunmobile.domain.model.places.Place;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Album {
    private Long id;
    private String name;
    private String description;
    private Image coverImage;
    private List<AlbumImage> albumImageList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Place place;
    private Long userId;

    public Album() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<AlbumImage> getAlbumImageList() {
        return albumImageList;
    }

    public void setAlbumImageList(List<AlbumImage> albumImageList) {
        this.albumImageList = albumImageList;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
