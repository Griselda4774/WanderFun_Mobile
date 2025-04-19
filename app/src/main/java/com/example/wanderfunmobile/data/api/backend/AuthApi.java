package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.auth.LoginDto;
import com.example.wanderfunmobile.data.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.data.dto.auth.RegisterDto;
import com.example.wanderfunmobile.data.dto.auth.TokenResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/login")
    Call<ResponseDto<LoginResponseDto>> login(@Body LoginDto loginDto);

    @POST("auth/register")
    Call<ResponseDto<LoginResponseDto>> register(@Body RegisterDto registerDto);

    @GET("auth/logout")
    Call<ResponseDto<LoginResponseDto>> logout(@Header("Authorization") String bearerToken);

    @GET("auth/refresh")
    Call<ResponseDto<TokenResponseDto>> refreshToken(@Header("Authorization") String bearerToken);
}
