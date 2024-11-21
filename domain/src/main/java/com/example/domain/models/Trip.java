package com.example.domain.models;

import java.util.Date;
import java.util.List;

public class Trip {
    private Long id;
    private Date startTime;
    private Date endTime;
    private List<TripPlace> tripPlaces;

    public Trip() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<TripPlace> getTripPlaces() {
        return tripPlaces;
    }

    public void setTripPlaces(List<TripPlace> tripPlaces) {
        this.tripPlaces = tripPlaces;
    }
}
