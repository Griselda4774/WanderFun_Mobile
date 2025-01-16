package com.example.wanderfunmobile.infrastructure.api.backend;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.checkin.CheckInDto;
import com.example.wanderfunmobile.application.dto.favouriteplace.FavouritePlaceDto;
import com.example.wanderfunmobile.application.dto.feedback.FeedbackCreateDto;
import com.example.wanderfunmobile.application.dto.feedback.FeedbackDto;
import com.example.wanderfunmobile.application.dto.place.PlaceDto;
import com.example.wanderfunmobile.application.dto.place.PlaceMiniDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PlaceApi {
    @GET("place")
    Call<ResponseDto<List<PlaceMiniDto>>> getAllPlaces();

    @GET("place/search/{name}")
    Call<ResponseDto<List<PlaceMiniDto>>> searchPlacesByNameContaining(@Path("name") String name);

    @GET("place/{placeId}")
    Call<ResponseDto<PlaceDto>> getPlaceById(@Path("placeId") Long placeId);

    @POST("place/feedback/{placeId}")
    Call<ResponseDto<FeedbackDto>> createFeedback(@Header("Authorization") String bearerToken,
                                                  @Body FeedbackCreateDto feedbackCreateDto,
                                                  @Path("placeId") Long placeId);

    @GET("place/favourite")
    Call<ResponseDto<List<FavouritePlaceDto>>> getAllFavouritePlaces(@Header("Authorization") String bearerToken);

    @POST("place/favourite/{placeId}")
    Call<ResponseDto<FavouritePlaceDto>> addFavouritePlace(@Header("Authorization") String bearerToken,
                                                           @Path("placeId") Long placeId);

    @DELETE("place/favourite/list")
    Call<ResponseDto<FavouritePlaceDto>> deleteFavouritePlaceByIds(@Header("Authorization") String bearerToken,
                                                                   @Body List<Long> placeIds);

    @POST("place/checkin/{placeId}")
    Call<ResponseDto<CheckInDto>> checkInPlace(@Header("Authorization") String bearerToken,
                                               @Path("placeId") Long placeId);
}
