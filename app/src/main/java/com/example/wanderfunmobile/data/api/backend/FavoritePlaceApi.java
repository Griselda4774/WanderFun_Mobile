package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.favouriteplace.FavoritePlaceDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FavoritePlaceApi {
    @GET("favorite-place")
    Call<ResponseDto<List<PlaceDto>>> findAllByUser(@Header("Authorization") String bearerToken);

    @POST("favorite-place")
    Call<ResponseDto<PlaceDto>> createFavoritePlace(@Header("Authorization") String bearerToken,
                                                            @Query("placeId") Long placeId);

    @DELETE("favorite-place")
    Call<ResponseDto<PlaceDto>> deleteByUserAndPlaceId(@Header("Authorization") String bearerToken,
                                                               @Query("placeId") Long id);
}
