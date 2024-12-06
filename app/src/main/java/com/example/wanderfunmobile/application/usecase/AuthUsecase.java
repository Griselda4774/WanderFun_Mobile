package com.example.wanderfunmobile.application.usecase;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.auth.LoginDto;
import com.example.wanderfunmobile.application.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.application.service.AuthService;

import javax.inject.Inject;

public class AuthUsecase {
    private final AuthService authService;

    @Inject
    public AuthUsecase(AuthService authService) {
        this.authService = authService;
    }

    public ResponseDto<LoginResponseDto> login(String email, String password) {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);
        return authService.login(loginDto);
    }
}
