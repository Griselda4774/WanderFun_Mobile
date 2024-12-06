package com.example.wanderfunmobile.network.backend;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.auth.LoginDto;
import com.example.wanderfunmobile.application.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.application.dto.auth.RegisterDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/login")
    Call<ResponseDto<LoginResponseDto>> login(@Body LoginDto loginDto);

    @POST("/auth/register")
    Call<ResponseDto<?>> register(@Body RegisterDto registerDto);
}
