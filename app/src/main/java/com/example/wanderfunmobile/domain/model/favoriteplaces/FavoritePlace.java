package com.example.wanderfunmobile.domain.model.favoriteplaces;

import com.example.wanderfunmobile.domain.model.places.Place;

public class FavoritePlace {
    private Long id;
    private Long userId;
    private Place place;

    public FavoritePlace() {
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
}
