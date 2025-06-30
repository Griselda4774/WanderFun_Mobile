package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.checkins.CheckIn;
import com.example.wanderfunmobile.domain.model.places.Place;

import java.util.List;

public interface CheckInRepository {
    LiveData<Result<List<CheckIn>>> findAllByUser(String bearerToken);
    LiveData<Result<CheckIn>> createCheckIn(String bearerToken, Long placeId);
    LiveData<Result<List<Place>>> findAllEligiblePlaces(String bearerToken, Double userLng, Double userLat);
}
