package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.goong.GoongTripRespondDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GoongApi {
    @GET("trip")
    Call<GoongTripRespondDto> getGoongTrip(
            @Header("Authorization") String bearerToken,
            @Query("api_key") String apiKey,
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("waypoints") String waypoints,
            @Query("vehicle") String vehicle
    );
}
