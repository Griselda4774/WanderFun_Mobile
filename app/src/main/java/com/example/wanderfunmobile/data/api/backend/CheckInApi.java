package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.checkin.CheckInDto;
import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CheckInApi {
    @GET("checkin")
    Call<ResponseDto<List<CheckInDto>>> findAllByUser(@Header("Authorization") String bearerToken);

    @POST("checkin")
    Call<ResponseDto<CheckInDto>> createCheckIn(@Header("Authorization") String bearerToken,
                                                @Query("placeId") Long placeId);

    @GET("checkin/eligible-places")
    Call<ResponseDto<List<MiniPlaceDto>>> findAllEligiblePlaces(@Header("Authorization") String bearerToken,
                                                            @Query("userLng") Double userLng,
                                                            @Query("userLat") Double userLat);
}
