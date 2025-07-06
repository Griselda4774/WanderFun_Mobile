package com.example.wanderfunmobile.data.dto.favouriteplace;

import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;

public class FavoritePlaceDto {
    private Long id;
    private Long userId;
    private MiniPlaceDto place;

    public FavoritePlaceDto() {
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
}
