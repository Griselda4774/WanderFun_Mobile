package com.example.wanderfunmobile.domain.model.trips;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trip {
    private Long id;
    private String name;
    private LocalDate startTime;
    private LocalDate endTime;
    private Long userId;
    private List<TripPlace> tripPlaceList = new ArrayList<>();

    public Trip() {};

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<TripPlace> getTripPlaceList() {
        return tripPlaceList;
    }

    public void setTripPlaceList(List<TripPlace> tripPlaceList) {
        this.tripPlaceList = tripPlaceList;
    }
}
