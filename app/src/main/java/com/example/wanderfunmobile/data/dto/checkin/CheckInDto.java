package com.example.wanderfunmobile.data.dto.checkin;

import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;

import java.time.LocalDateTime;
import java.util.Date;

public class CheckInDto {
    private Long id;
    private Long userId;
    private MiniPlaceDto place;
    private LocalDateTime createdAt;

    public CheckInDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MiniPlaceDto getPlace() {
        return place;
    }

    public void setPlace(MiniPlaceDto place) {
        this.place = place;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
