package com.example.wanderfunmobile.data.dto.tripplace;

import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;

import java.time.LocalDate;
import java.util.Date;

public class TripPlaceDto {
    private Long id;
    private MiniPlaceDto place;
    private LocalDate startTime;
    private LocalDate endTime;
    private Long tripId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MiniPlaceDto getPlace() {
        return place;
    }

    public void setPlace(MiniPlaceDto place) {
        this.place = place;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
}
