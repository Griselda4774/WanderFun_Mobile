package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.auth.LoginDto;
import com.example.wanderfunmobile.data.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.data.dto.auth.RegisterDto;
import com.example.wanderfunmobile.data.dto.auth.TokenResponseDto;

public interface AuthRepository {
    LiveData<ResponseDto<LoginResponseDto>> login(LoginDto loginDto);
    LiveData<ResponseDto<LoginResponseDto>> register(RegisterDto registerDto);
    LiveData<ResponseDto<LoginResponseDto>> logout(String bearerToken);
    LiveData<ResponseDto<TokenResponseDto>> refreshToken(String bearerToken);
}
