package com.example.wanderfunmobile.infrastructure.api.backend;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.cloudinary.CloudinarySignatureDto;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CloudinaryApi {
    @GET("cloudinary/signature")
    Call<ResponseDto<CloudinarySignatureDto>> getSignature(@Header("Authorization") String bearerToken,
                                                           @Query("timestamp") String timestamp);

    @DELETE("cloudinary")
    Call<ResponseDto<CloudinarySignatureDto>> deleteImage(@Header("Authorization") String bearerToken,
                                                @Query("public_id") String publicId);
}
