package com.example.wanderfunmobile.network.backend;

import com.example.wanderfunmobile.network.dto.EmptyDataDto;
import com.example.wanderfunmobile.network.dto.ResponseDto;
import com.example.wanderfunmobile.network.dto.cloudinary.CloudinarySignatureDto;

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
    Call<ResponseDto<EmptyDataDto>> deleteImage(@Header("Authorization") String bearerToken,
                                                @Query("public_id") String publicId);
}
