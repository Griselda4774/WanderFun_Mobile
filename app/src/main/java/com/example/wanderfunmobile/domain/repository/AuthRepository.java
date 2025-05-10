package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.auth.LoginDto;
import com.example.wanderfunmobile.data.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.data.dto.auth.RegisterDto;
import com.example.wanderfunmobile.data.dto.auth.TokenResponseDto;

public interface AuthRepository {
    public LiveData<ResponseDto<LoginResponseDto>> login(LoginDto loginDto);

    public LiveData<ResponseDto<LoginResponseDto>> register(RegisterDto registerDto);

    public LiveData<ResponseDto<LoginResponseDto>> logout(String bearerToken);

    public LiveData<ResponseDto<TokenResponseDto>> refreshToken(String bearerToken);
}
