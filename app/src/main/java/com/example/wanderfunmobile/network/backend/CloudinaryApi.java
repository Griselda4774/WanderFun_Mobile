package com.example.wanderfunmobile.network.backend;

import com.example.wanderfunmobile.application.dto.ResponseDto;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CloudinaryApi {
    @GET("cloudinary/signature")
    Call<ResponseDto<Object>> getSignature(@Header("Authorization") String bearerToken,
                                                           @Query("timestamp") String timestamp);

    @DELETE("cloudinary")
    Call<ResponseDto<Object>> deleteImage(@Header("Authorization") String bearerToken,
                                                @Query("public_id") String publicId);
}
