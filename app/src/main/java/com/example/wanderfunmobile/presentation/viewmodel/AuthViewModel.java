package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.auth.MailOtpDto;
import com.example.wanderfunmobile.data.dto.auth.TokenResponseDto;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.repository.AuthRepository;
import com.example.wanderfunmobile.data.dto.auth.LoginDto;
import com.example.wanderfunmobile.data.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.data.dto.auth.RegisterDto;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<ResponseDto<LoginResponseDto>> loginResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<LoginResponseDto>> registerResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<LoginResponseDto>> logoutResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<TokenResponseDto>> refreshTokenResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Void>> sendOtpResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Void>> verifyOtpResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Void>> forgotPasswordResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Void>> changePasswordResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();



    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<ResponseDto<LoginResponseDto>> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }

    public LiveData<ResponseDto<LoginResponseDto>> getRegisterResponseLiveData() {
        return registerResponseLiveData;
    }


    public LiveData<ResponseDto<LoginResponseDto>> getLogoutResponseLiveData() {
        return logoutResponseLiveData;
    }

    public LiveData<ResponseDto<TokenResponseDto>> getRefreshTokenResponseLiveData() {
        return refreshTokenResponseLiveData;
    }

    public LiveData<Result<Void>> getSendOtpResponseLiveData() {
        return sendOtpResponseLiveData;
    }

    public LiveData<Result<Void>> getVerifyOtpResponseLiveData() {
        return verifyOtpResponseLiveData;
    }

    public LiveData<Result<Void>> getForgotPasswordResponseLiveData() {
        return forgotPasswordResponseLiveData;
    }

    public LiveData<Result<Void>> getChangePasswordResponseLiveData() {
        return changePasswordResponseLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void login(LoginDto loginDto) {
        isLoading.setValue(true);
        authRepository.login(loginDto).observeForever(response -> {
            loginResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void register(RegisterDto registerDto) {
        isLoading.setValue(true);
        authRepository.register(registerDto).observeForever(response -> {
            registerResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void logout(String bearerToken) {
        isLoading.setValue(true);
        authRepository.logout(bearerToken).observeForever(response -> {
            logoutResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void refreshToken(String bearerToken) {
        isLoading.setValue(true);
        authRepository.refreshToken(bearerToken).observeForever(response -> {
            refreshTokenResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void sendOtp(String email) {
        isLoading.setValue(true);
        authRepository.sendOtp(email).observeForever(result -> {
            sendOtpResponseLiveData.setValue(result);
            isLoading.setValue(false);
        });
    }

    public void verifyOtp(MailOtpDto mailOtpDto) {
        isLoading.setValue(true);
        authRepository.verifyOtp(mailOtpDto).observeForever(result -> {
            verifyOtpResponseLiveData.setValue(result);
            isLoading.setValue(false);
        });
    }

    public void forgotPassword(String email, String otpCode, String newPassword) {
        isLoading.setValue(true);
        authRepository.forgotPassword(email, otpCode, newPassword).observeForever(result -> {
            forgotPasswordResponseLiveData.setValue(result);
            isLoading.setValue(false);
        });
    }

    public void changePassword(String bearerToken, String oldPassword, String newPassword) {
        isLoading.setValue(true);
        authRepository.changePassword(bearerToken, oldPassword, newPassword).observeForever(result -> {
            changePasswordResponseLiveData.setValue(result);
            isLoading.setValue(false);
        });
    }
}
