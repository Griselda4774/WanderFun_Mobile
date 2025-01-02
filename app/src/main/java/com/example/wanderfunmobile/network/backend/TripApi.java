package com.example.wanderfunmobile.network.backend;

import com.example.wanderfunmobile.application.dto.EmptyDataDto;
import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.trip.TripCreateDto;
import com.example.wanderfunmobile.application.dto.trip.TripDto;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TripApi {
    @GET("trip")
    Call<ResponseDto<Object>> getAllTrips(@Header("Authorization") String bearerToken);

    @GET("trip/{tripId}")
    Call<ResponseDto<Object>> getTripById(@Header("Authorization") String bearerToken,
                                           @Path("tripId") Long tripId);

    @POST("trip")
    Call<ResponseDto<Object>> createTrip(@Header("Authorization") String bearerToken,
                                               @Body TripCreateDto tripCreateDto);

    @PUT("trip/{tripId}")
    Call<ResponseDto<Object>> updateTrip(@Header("Authorization") String bearerToken,
                                               @Path("tripId") Long tripId,
                                               @Body TripCreateDto tripCreateDto);

    @DELETE("trip/{tripId}")
    Call<ResponseDto<Object>> deleteTrip(@Header("Authorization") String bearerToken,
                                               @Path("tripId") Long tripId);

    @DELETE("trip")
    Call<ResponseDto<Object>> deleteAllTrips(@Header("Authorization") String bearerToken);
}
