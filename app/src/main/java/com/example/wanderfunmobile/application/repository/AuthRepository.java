package com.example.wanderfunmobile.application.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.auth.LoginDto;
import com.example.wanderfunmobile.application.dto.auth.RegisterDto;

public interface AuthRepository {
    public LiveData<ResponseDto<Object>> login(LoginDto loginDto);
    public LiveData<ResponseDto<Object>> register(RegisterDto registerDto);
    public LiveData<ResponseDto<Object>> logout(String bearerToken);
    public LiveData<ResponseDto<Object>> refreshToken(String bearerToken);

}
