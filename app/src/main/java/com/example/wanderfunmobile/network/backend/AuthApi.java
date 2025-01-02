package com.example.wanderfunmobile.network.backend;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.EmptyDataDto;
import com.example.wanderfunmobile.application.dto.auth.LoginDto;
import com.example.wanderfunmobile.application.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.application.dto.auth.RegisterDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/login")
    Call<ResponseDto<Object>> login(@Body LoginDto loginDto);

    @POST("auth/register")
    Call<ResponseDto<Object>> register(@Body RegisterDto registerDto);

    @GET("auth/logout")
    Call<ResponseDto<Object>> logout(@Header("Authorization") String bearerToken);

    @GET("auth/refresh")
    Call<ResponseDto<Object>> refreshToken(@Header("Authorization") String bearerToken);
}
