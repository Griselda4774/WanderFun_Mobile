package com.example.wanderfunmobile.data.api.backend.places;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.place.FeedbackCreateDto;
import com.example.wanderfunmobile.data.dto.place.FeedbackDto;

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

public interface FeedbackApi {
    @GET("feedback")
    Call<ResponseDto<List<FeedbackDto>>> findAllByPlaceId(@Header("Authorization") String bearerToken,
                                                                   @Query("placeId") Long placeId);

    @POST("feedback")
    Call<ResponseDto<FeedbackDto>> create(@Header("Authorization") String bearerToken,
                                                  @Query("placeId") Long placeId,
                                                  @Body FeedbackCreateDto feedbackCreateDto);

    @PUT("feedback/{feedbackId}")
    Call<ResponseDto<FeedbackDto>> updateById(@Header("Authorization") String bearerToken,
                                                  @Path("feedbackId") Long feedbackId,
                                                  @Body FeedbackCreateDto feedbackCreateDto);

    @DELETE("feedback/{feedbackId}")
    Call<ResponseDto<FeedbackDto>> deleteById(@Header("Authorization") String bearerToken,
                                                  @Path("feedbackId") Long feedbackId);
}
