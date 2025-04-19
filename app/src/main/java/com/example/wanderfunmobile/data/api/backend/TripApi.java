package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.trip.TripCreateDto;
import com.example.wanderfunmobile.data.dto.trip.TripDto;

import java.util.List;

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
    Call<ResponseDto<List<TripDto>>> getAllTrips(@Header("Authorization") String bearerToken);

    @GET("trip/{tripId}")
    Call<ResponseDto<TripDto>> getTripById(@Header("Authorization") String bearerToken,
                                           @Path("tripId") Long tripId);

    @POST("trip")
    Call<ResponseDto<TripDto>> createTrip(@Header("Authorization") String bearerToken,
                                               @Body TripCreateDto tripCreateDto);

    @PUT("trip/{tripId}")
    Call<ResponseDto<TripDto>> updateTrip(@Header("Authorization") String bearerToken,
                                               @Path("tripId") Long tripId,
                                               @Body TripCreateDto tripCreateDto);

    @DELETE("trip/{tripId}")
    Call<ResponseDto<TripDto>> deleteTrip(@Header("Authorization") String bearerToken,
                                               @Path("tripId") Long tripId);

    @DELETE("trip")
    Call<ResponseDto<TripDto>> deleteAllTrips(@Header("Authorization") String bearerToken);
}
