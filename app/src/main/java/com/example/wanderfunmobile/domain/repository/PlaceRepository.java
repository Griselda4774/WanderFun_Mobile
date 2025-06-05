package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.checkin.CheckInDto;
import com.example.wanderfunmobile.data.dto.favouriteplace.FavouritePlaceDto;
import com.example.wanderfunmobile.data.dto.feedback.FeedbackCreateDto;
import com.example.wanderfunmobile.data.dto.feedback.FeedbackDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;
import com.example.wanderfunmobile.domain.model.CheckIn;
import com.example.wanderfunmobile.domain.model.FavouritePlace;
import com.example.wanderfunmobile.domain.model.Feedback;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.places.Place;

import java.util.List;

public interface PlaceRepository {
    LiveData<Result<List<Place>>> findAllPlaces();

    LiveData<Result<List<Place>>> findAllPlacesByProvinceName(String provinceName);

    LiveData<Result<List<Place>>> findAllPlacesByNameContaining(String name);

    LiveData<Result<Place>> findPlaceById(Long placeId);

    LiveData<Result<Feedback>> createFeedback(String bearerToken, Feedback feedback, Long placeId);

    LiveData<Result<List<FavouritePlace>>> findAllFavouritePlaces(String bearerToken);

    LiveData<Result<FavouritePlace>> addFavouritePlace(String bearerToken, Long placeId);

    LiveData<Result<FavouritePlace>> deleteFavouritePlaceByIds(String bearerToken, List<Long> placeIds);

    LiveData<Result<CheckIn>> checkInPlace(String bearerToken, Long placeId);

    LiveData<Result<CheckIn>> findCheckInByPlaceId(String bearerToken, Long placeId);
}
