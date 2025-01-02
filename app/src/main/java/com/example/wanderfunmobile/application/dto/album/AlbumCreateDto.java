package com.example.wanderfunmobile.application.dto.album;

import com.example.wanderfunmobile.application.dto.albumimage.AlbumImageCreateDto;

import java.util.List;

public class AlbumCreateDto {
    private String name;
    private String description;
    private Long placeId;
    private double placeLongitude;
    private double placeLatitude;
    private String placeName;
    private String placeCoverImageUrl;
    private List<AlbumImageCreateDto> albumImages;

    public AlbumCreateDto() {};

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

    public List<AlbumImageCreateDto> getAlbumImages() {
        return albumImages;
    }

    public void setAlbumImages(List<AlbumImageCreateDto> albumImages) {
        this.albumImages = albumImages;
    }
}
