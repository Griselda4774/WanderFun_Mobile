package com.example.wanderfunmobile.application.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.checkin.CheckInDto;
import com.example.wanderfunmobile.application.dto.favouriteplace.FavouritePlaceDto;
import com.example.wanderfunmobile.application.dto.feedback.FeedbackCreateDto;
import com.example.wanderfunmobile.application.dto.feedback.FeedbackDto;
import com.example.wanderfunmobile.application.dto.place.PlaceDto;
import com.example.wanderfunmobile.application.dto.place.PlaceMiniDto;

import java.util.List;

public interface PlaceRepository {
    LiveData<ResponseDto<List<PlaceMiniDto>>> getAllPlaces();

    LiveData<ResponseDto<List<PlaceMiniDto>>> searchPlacesByNameContaining(String name);

    LiveData<ResponseDto<PlaceDto>> getPlaceById(Long placeId);

    LiveData<ResponseDto<FeedbackDto>> createFeedback(String bearerToken, FeedbackCreateDto feedbackCreateDto, Long placeId);

    LiveData<ResponseDto<List<FavouritePlaceDto>>> getAllFavouritePlaces(String bearerToken);

    LiveData<ResponseDto<FavouritePlaceDto>> addFavouritePlace(String bearerToken, Long placeId);

    LiveData<ResponseDto<FavouritePlaceDto>> deleteFavouritePlaceByIds(String bearerToken, List<Long> placeIds);

    LiveData<ResponseDto<CheckInDto>> checkInPlace(String bearerToken, Long placeId);

    LiveData<ResponseDto<CheckInDto>> getCheckInByPlaceIdAndUserId(String bearerToken, Long placeId);
}
