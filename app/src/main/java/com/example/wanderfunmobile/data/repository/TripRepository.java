package com.example.wanderfunmobile.data.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.trip.TripCreateDto;
import com.example.wanderfunmobile.data.dto.trip.TripDto;

import java.util.List;

public interface TripRepository {
    LiveData<ResponseDto<List<TripDto>>> getAllTrips(String bearerToken);

    LiveData<ResponseDto<TripDto>> getTripById(String bearerToken, Long tripId);

    LiveData<ResponseDto<TripDto>> createTrip(String bearerToken, TripCreateDto tripCreateDto);

    LiveData<ResponseDto<TripDto>> updateTripById(String bearerToken, Long tripId, TripCreateDto tripCreateDto);

    LiveData<ResponseDto<TripDto>> deleteAllTrips(String bearerToken);

    LiveData<ResponseDto<TripDto>> deleteTripById(String bearerToken, Long tripId);
}
