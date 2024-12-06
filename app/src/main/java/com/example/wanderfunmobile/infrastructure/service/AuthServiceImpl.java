package com.example.wanderfunmobile.infrastructure.service;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.auth.LoginDto;
import com.example.wanderfunmobile.application.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.application.dto.auth.RegisterDto;
import com.example.wanderfunmobile.application.service.AuthService;
import com.example.wanderfunmobile.network.backend.AuthApi;

import java.util.Date;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class AuthServiceImpl implements AuthService {
    private final AuthApi authApi;

    @Inject
    public AuthServiceImpl(AuthApi authApi) {
        this.authApi = authApi;
    }

    @Override
    public ResponseDto<LoginResponseDto> login(LoginDto loginDto) {
        try {
            Call<ResponseDto<LoginResponseDto>> call = authApi.login(loginDto);
            Response<ResponseDto<LoginResponseDto>> response = call.execute();
            return response.body();
        } catch (Exception e) {
            ResponseDto<LoginResponseDto> failedResponse = new ResponseDto<>();
            handleErrorResponse(failedResponse, e);
            return failedResponse;
        }
    }

    @Override
    public ResponseDto<?> register(RegisterDto registerDto) {
        try {
            Call<ResponseDto<?>> call = authApi.register(registerDto);
            Response<ResponseDto<?>> response = call.execute();
            return response.body();
        } catch (Exception e) {
            ResponseDto<?> failedResponse = new ResponseDto<>();
            handleErrorResponse(failedResponse, e);
            return failedResponse;
        }
    }

    private void handleErrorResponse(ResponseDto<?> response, Exception e) {
        response.setStatusCode("400 BAD REQUEST");
        response.setTimestamp(new Date());
        response.setMessage(e.getMessage());
        response.setData(null);
    }
}
