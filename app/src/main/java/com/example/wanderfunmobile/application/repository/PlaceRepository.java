package com.example.wanderfunmobile.application.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.place.PlaceDto;

import java.util.List;

public interface PlaceRepository {
    public LiveData<ResponseDto<List<PlaceDto>>> getAllPlaces();
    public LiveData<ResponseDto<PlaceDto>> getPlaceById(Long placeId);
}
