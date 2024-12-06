package com.example.wanderfunmobile.infrastructure.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.application.usecase.AuthUsecase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final AuthUsecase authUsecase;
    private final MutableLiveData<LoginResponseDto> loginLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    @Inject
    public LoginViewModel(AuthUsecase authUsecase) {
        this.authUsecase = authUsecase;
    }

    public LiveData<LoginResponseDto> getUserLiveData() { return loginLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }

    public void login(String username, String password) {
        try {
            ResponseDto<LoginResponseDto> loginResponse = authUsecase.login(username, password);
            loginLiveData.postValue(loginResponse.getData());
        } catch (Exception e) {
            errorLiveData.postValue(e.getMessage());
        }
    }
}
