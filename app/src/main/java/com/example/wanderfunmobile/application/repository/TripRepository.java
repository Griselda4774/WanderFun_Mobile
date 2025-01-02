package com.example.wanderfunmobile.application.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.trip.TripCreateDto;

public interface TripRepository {
    LiveData<ResponseDto<Object>> getAllTrips(String bearerToken);
    LiveData<ResponseDto<Object>> getTripById(String bearerToken, Long tripId);
    LiveData<ResponseDto<Object>> createTrip(String bearerToken, TripCreateDto tripCreateDto);
    LiveData<ResponseDto<Object>> updateTripById(String bearerToken, Long tripId, TripCreateDto tripCreateDto);
    LiveData<ResponseDto<Object>> deleteAllTrips(String bearerToken);
    LiveData<ResponseDto<Object>> deleteTripById(String bearerToken, Long tripId);
}
