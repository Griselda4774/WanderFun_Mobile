package com.example.wanderfunmobile.application.dto.trip;

import com.example.wanderfunmobile.application.dto.tripplace.TripPlaceCreateDto;

import java.util.Date;
import java.util.List;

public class TripCreateDto {
    private String name;
    private String imageUrl;
    private String imagePublicId;
    private Date startTime;
    private Date endTime;
    private List<TripPlaceCreateDto> tripPlaces;

    public TripCreateDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<TripPlaceCreateDto> getTripPlaces() {
        return tripPlaces;
    }

    public void setTripPlaces(List<TripPlaceCreateDto> tripPlaces) {
        this.tripPlaces = tripPlaces;
    }
}
