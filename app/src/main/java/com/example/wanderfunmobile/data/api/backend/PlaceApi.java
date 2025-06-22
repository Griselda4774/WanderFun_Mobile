package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.place.FeedbackCreateDto;
import com.example.wanderfunmobile.data.dto.place.FeedbackDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;
import com.example.wanderfunmobile.data.dto.posts.CommentCreateDto;
import com.example.wanderfunmobile.data.dto.posts.CommentDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlaceApi {
    @GET("place")
    Call<ResponseDto<List<PlaceDto>>> findAllPlaces();

    @GET("place/search/province/{provinceName}")
    Call<ResponseDto<List<PlaceDto>>> findAllPlacesByProvinceName(@Path("provinceName") String provinceName);

    @GET("place/search/{name}")
    Call<ResponseDto<List<PlaceDto>>> findAllPlacesByNameContaining(@Path("name") String name);

    @GET("place/{placeId}")
    Call<ResponseDto<PlaceDto>> findPlaceById(@Path("placeId") Long placeId);

    @GET("place/feedback")
    Call<ResponseDto<List<FeedbackDto>>> findAllFeedbacksByPlaceId(@Header("Authorization") String bearerToken,
                                                                 @Query("placeId") Long placeId);

    @POST("place/feedback")
    Call<ResponseDto<FeedbackDto>> createFeedback(@Header("Authorization") String bearerToken,
                                                @Query("placeId") Long placeId,
                                                @Body FeedbackCreateDto feedbackCreateDto);

    @PUT("place/feedback/{feedbackId}")
    Call<ResponseDto<FeedbackDto>> updateFeedback(@Header("Authorization") String bearerToken,
                                                @Path("feedbackId") Long feedbackId,
                                                @Body FeedbackCreateDto feedbackCreateDto);

    @DELETE("place/feedback/{feedbackId}")
    Call<ResponseDto<FeedbackDto>> deleteFeedback(@Header("Authorization") String bearerToken,
                                                @Path("feedbackId") Long feedbackId);

//    @GET("place/favourite")
//    Call<ResponseDto<List<FavouritePlaceDto>>> findAllFavouritePlaces(@Header("Authorization") String bearerToken);
//
//    @POST("place/favourite/{placeId}")
//    Call<ResponseDto<FavouritePlaceDto>> addFavouritePlace(@Header("Authorization") String bearerToken,
//                                                           @Path("placeId") Long placeId);
//
//    @DELETE("place/favourite/list")
//    Call<ResponseDto<FavouritePlaceDto>> deleteFavouritePlaceByIds(@Header("Authorization") String bearerToken,
//                                                                   @Body List<Long> placeIds);
//
//    @GET("place/checkin/{placeId}")
//    Call<ResponseDto<CheckInDto>> findCheckInByPlaceId(@Header("Authorization") String bearerToken,
//                                                               @Path("placeId") Long placeId);
//
//    @POST("place/checkin/{placeId}")
//    Call<ResponseDto<CheckInDto>> checkInPlace(@Header("Authorization") String bearerToken,
//                                               @Path("placeId") Long placeId);
}
