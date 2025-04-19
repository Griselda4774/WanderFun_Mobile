package com.example.wanderfunmobile.data.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.checkin.CheckInDto;
import com.example.wanderfunmobile.data.dto.favouriteplace.FavouritePlaceDto;
import com.example.wanderfunmobile.data.dto.feedback.FeedbackCreateDto;
import com.example.wanderfunmobile.data.dto.feedback.FeedbackDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;

import java.util.List;

public interface PlaceRepository {
    LiveData<ResponseDto<List<PlaceDto>>> getAllPlaces();

    LiveData<ResponseDto<List<PlaceDto>>> getAllPlacesByProvinceName(String provinceName);

    LiveData<ResponseDto<List<PlaceDto>>> searchPlacesByNameContaining(String name);

    LiveData<ResponseDto<PlaceDto>> getPlaceById(Long placeId);

    LiveData<ResponseDto<FeedbackDto>> createFeedback(String bearerToken, FeedbackCreateDto feedbackCreateDto, Long placeId);

    LiveData<ResponseDto<List<FavouritePlaceDto>>> getAllFavouritePlaces(String bearerToken);

    LiveData<ResponseDto<FavouritePlaceDto>> addFavouritePlace(String bearerToken, Long placeId);

    LiveData<ResponseDto<FavouritePlaceDto>> deleteFavouritePlaceByIds(String bearerToken, List<Long> placeIds);

    LiveData<ResponseDto<CheckInDto>> checkInPlace(String bearerToken, Long placeId);

    LiveData<ResponseDto<CheckInDto>> getCheckInByPlaceIdAndUserId(String bearerToken, Long placeId);
}
