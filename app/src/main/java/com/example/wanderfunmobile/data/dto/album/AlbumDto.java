package com.example.wanderfunmobile.data.dto.album;

import com.example.wanderfunmobile.data.dto.albumimage.AlbumImageDto;

import java.util.Date;
import java.util.List;

public class AlbumDto {
    private Long id;
    private String name;
    private String description;
    private Long placeId;
    private double placeLongitude;
    private double placeLatitude;
    private String placeName;
    private String placeCoverImageUrl;
    private List<AlbumImageDto> albumImages;
    private Date lastModified;

    public AlbumDto() {};

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

    public List<AlbumImageDto> getAlbumImages() {
        return albumImages;
    }

    public void setAlbumImages(List<AlbumImageDto> albumImages) {
        this.albumImages = albumImages;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
