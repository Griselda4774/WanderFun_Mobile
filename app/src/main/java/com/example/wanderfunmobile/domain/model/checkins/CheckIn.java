package com.example.wanderfunmobile.domain.model.checkins;

import com.example.wanderfunmobile.domain.model.places.Place;

import java.time.LocalDateTime;

public class CheckIn {
    private Long id;
    private Long userId;
    private Place place;
    private LocalDateTime createdAt;

    public CheckIn() {
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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}