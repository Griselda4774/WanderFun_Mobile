package com.example.wanderfunmobile.data.dto.trip;

import com.example.wanderfunmobile.core.util.LocalDateDeserializer;
import com.example.wanderfunmobile.data.dto.tripplace.TripPlaceCreateDto;
import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDate;
import java.util.List;

public class TripCreateDto {
    private String name;
    private String imageUrl;
    private String imagePublicId;
    private LocalDate startTime;
    private LocalDate endTime;
    private List<TripPlaceCreateDto> tripPlaceList;

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

    public List<TripPlaceCreateDto> getTripPlaceList() {
        return tripPlaceList;
    }

    public void setTripPlaceList(List<TripPlaceCreateDto> tripPlaceList) {
        this.tripPlaceList = tripPlaceList;
    }
}
