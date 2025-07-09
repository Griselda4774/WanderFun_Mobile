package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.auth.ChangePasswordDto;
import com.example.wanderfunmobile.data.dto.auth.ForgotPasswordDto;
import com.example.wanderfunmobile.data.dto.auth.LoginDto;
import com.example.wanderfunmobile.data.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.data.dto.auth.MailOtpDto;
import com.example.wanderfunmobile.data.dto.auth.RegisterDto;
import com.example.wanderfunmobile.data.dto.auth.TokenResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("auth/login")
    Call<ResponseDto<LoginResponseDto>> login(@Body LoginDto loginDto);

    @POST("auth/register")
    Call<ResponseDto<LoginResponseDto>> register(@Body RegisterDto registerDto);

    @GET("auth/logout")
    Call<ResponseDto<LoginResponseDto>> logout(@Header("Authorization") String bearerToken);

    @GET("auth/refresh")
    Call<ResponseDto<TokenResponseDto>> refreshToken(@Header("Authorization") String bearerToken);

    @GET("auth/otp")
    Call<ResponseDto<Void>> sendOtp(@Query("email") String email);

    @POST("auth/otp/verify")
    Call<ResponseDto<Void>> verifyOtp(@Body MailOtpDto mailOtpDto);

    @POST("auth/password/forgot")
    Call<ResponseDto<Void>> forgotPassword(@Body ForgotPasswordDto forgotPasswordDto);

    @POST("auth/password/change")
    Call<ResponseDto<Void>> changePassword(@Header("Authorization") String bearerToken, @Body ChangePasswordDto changePasswordDto);
}
