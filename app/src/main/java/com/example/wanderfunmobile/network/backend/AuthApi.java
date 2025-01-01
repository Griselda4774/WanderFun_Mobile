package com.example.wanderfunmobile.network.backend;

import com.example.wanderfunmobile.network.dto.ResponseDto;
import com.example.wanderfunmobile.network.dto.EmptyDataDto;
import com.example.wanderfunmobile.network.dto.auth.LoginDto;
import com.example.wanderfunmobile.network.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.network.dto.auth.RegisterDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/login")
    Call<ResponseDto<LoginResponseDto>> login(@Body LoginDto loginDto);

    @POST("auth/register")
    Call<ResponseDto<EmptyDataDto>> register(@Body RegisterDto registerDto);
}
