package com.example.wanderfunmobile.application.service;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.auth.LoginDto;
import com.example.wanderfunmobile.application.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.application.dto.auth.RegisterDto;

public interface AuthService {
    public ResponseDto<LoginResponseDto> login(LoginDto loginDto);
    public ResponseDto<?> register(RegisterDto registerDto);
}
