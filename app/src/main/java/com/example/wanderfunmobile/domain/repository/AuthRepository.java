package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.auth.LoginDto;
import com.example.wanderfunmobile.data.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.data.dto.auth.MailOtpDto;
import com.example.wanderfunmobile.data.dto.auth.RegisterDto;
import com.example.wanderfunmobile.data.dto.auth.TokenResponseDto;
import com.example.wanderfunmobile.domain.model.Result;

public interface AuthRepository {
    LiveData<ResponseDto<LoginResponseDto>> login(LoginDto loginDto);
    LiveData<ResponseDto<LoginResponseDto>> register(RegisterDto registerDto);
    LiveData<ResponseDto<LoginResponseDto>> logout(String bearerToken);
    LiveData<ResponseDto<TokenResponseDto>> refreshToken(String bearerToken);
    LiveData<Result<Void>> sendOtp(String email);
    LiveData<Result<Void>> verifyOtp(MailOtpDto mailOtpDto);
    LiveData<Result<Void>> forgotPassword(String email, String otpCode, String newPassword);
    LiveData<Result<Void>> changePassword(String bearerToken, String oldPassword, String newPassword);
}
