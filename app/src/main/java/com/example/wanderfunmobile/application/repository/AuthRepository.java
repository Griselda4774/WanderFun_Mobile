package com.example.wanderfunmobile.application.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.auth.LoginDto;
import com.example.wanderfunmobile.application.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.application.dto.auth.RegisterDto;
import com.example.wanderfunmobile.application.dto.auth.TokenResponseDto;

public interface AuthRepository {
    public LiveData<ResponseDto<LoginResponseDto>> login(LoginDto loginDto);
    public LiveData<ResponseDto<LoginResponseDto>> register(RegisterDto registerDto);
    public LiveData<ResponseDto<LoginResponseDto>> logout(String bearerToken);
    public LiveData<ResponseDto<TokenResponseDto>> refreshToken(String bearerToken);

}
