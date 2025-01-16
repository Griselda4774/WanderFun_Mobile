package com.example.wanderfunmobile.domain.model;

import java.util.Date;
import java.util.List;

public class Album {
    private Long id;
    private String name;
    private String description;
    private Long placeId;
    private double placeLongitude;
    private double placeLatitude;
    private String placeName;
    private String placeCoverImageUrl;
    private List<AlbumImage> albumImages;
    private Date lastModified;
    private Long userId;
    private Date lastModified;

    public Album() {};

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

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public double getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceCoverImageUrl() {
        return placeCoverImageUrl;
    }

    public void setPlaceCoverImageUrl(String placeCoverImageUrl) {
        this.placeCoverImageUrl = placeCoverImageUrl;
    }

    public List<AlbumImage> getAlbumImages() {
        return albumImages;
    }

    public void setAlbumImages(List<AlbumImage> albumImages) {
        this.albumImages = albumImages;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
