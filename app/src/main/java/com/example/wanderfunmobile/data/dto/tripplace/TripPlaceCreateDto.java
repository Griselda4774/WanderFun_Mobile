package com.example.wanderfunmobile.data.dto.tripplace;

import com.example.wanderfunmobile.core.util.LocalDateDeserializer;
import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDate;
import java.util.Date;

public class TripPlaceCreateDto {
    private Long placeId;
    private LocalDate startTime;
    private LocalDate endTime;
    private String placeNotes;

    public TripPlaceCreateDto() {};

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
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

    public String getPlaceNotes() {
        return placeNotes;
    }

    public void setPlaceNotes(String placeNotes) {
        this.placeNotes = placeNotes;
    }
}
