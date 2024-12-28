package com.example.wanderfunmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.network.dto.ResponseDto;
import com.example.wanderfunmobile.network.dto.auth.LoginDto;
import com.example.wanderfunmobile.network.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.network.backend.AuthApi;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final AuthApi authApi;
    private final MutableLiveData<LoginResponseDto> liveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    @Inject
    public LoginViewModel(AuthApi authApi) {
        this.authApi = authApi;
    }

    public LiveData<LoginResponseDto> getLiveData() { return liveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }

    public void login(LoginDto loginDto) {
        try {
            Call<ResponseDto<LoginResponseDto>> call = authApi.login(loginDto);
            call.enqueue(new Callback<ResponseDto<LoginResponseDto>>() {
                @Override
                public void onResponse(Call<ResponseDto<LoginResponseDto>> call, Response<ResponseDto<LoginResponseDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        liveData.postValue(response.body().getData());
                    } else {
                        String errorMessage = "Login failed";
                        if (response.errorBody() != null) {
                            try {
                                errorMessage = response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        errorLiveData.postValue(errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<ResponseDto<LoginResponseDto>> call, Throwable t) {
                    errorLiveData.postValue(t.getMessage());
                }
            });
        } catch (Exception e) {
            errorLiveData.postValue(e.getMessage());
        }
    }

}
