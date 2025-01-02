package com.example.wanderfunmobile.application.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;

public interface PlaceRepository {
    public LiveData<ResponseDto<Object>> getAllPlaces();
    public LiveData<ResponseDto<Object>> getPlaceById(Long placeId);
}
