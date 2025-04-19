package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.checkin.CheckInDto;
import com.example.wanderfunmobile.data.dto.favouriteplace.FavouritePlaceDto;
import com.example.wanderfunmobile.data.dto.feedback.FeedbackCreateDto;
import com.example.wanderfunmobile.data.dto.feedback.FeedbackDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;

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
    Call<ResponseDto<List<PlaceDto>>> getAllPlaces();

    @GET("place/search/province/{provinceName}")
    Call<ResponseDto<List<PlaceDto>>> getAllPlacesByProvinceName(@Path("provinceName") String provinceName);

    @GET("place/search/{name}")
    Call<ResponseDto<List<PlaceDto>>> searchPlacesByNameContaining(@Path("name") String name);

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

    @GET("place/checkin/{placeId}")
    Call<ResponseDto<CheckInDto>> getCheckInByPlaceIdAndUserId(@Header("Authorization") String bearerToken,
                                                               @Path("placeId") Long placeId);

    @POST("place/checkin/{placeId}")
    Call<ResponseDto<CheckInDto>> checkInPlace(@Header("Authorization") String bearerToken,
                                               @Path("placeId") Long placeId);
}
