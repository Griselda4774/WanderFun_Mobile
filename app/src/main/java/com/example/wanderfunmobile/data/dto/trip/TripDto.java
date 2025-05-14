package com.example.wanderfunmobile.data.dto.trip;

import com.example.wanderfunmobile.data.dto.tripplace.TripPlaceDto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class TripDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String imagePublicId;
    private LocalDate startTime;
    private LocalDate endTime;
    private List<TripPlaceDto> tripPlaces;

    public TripDto() {
    }

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

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public List<TripPlaceDto> getTripPlaces() {
        return tripPlaces;
    }

    public void setTripPlaces(List<TripPlaceDto> tripPlaces) {
        this.tripPlaces = tripPlaces;
    }
}
