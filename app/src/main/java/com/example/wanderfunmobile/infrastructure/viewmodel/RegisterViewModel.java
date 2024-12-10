package com.example.wanderfunmobile.infrastructure.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.network.backend.AuthApi;
import com.example.wanderfunmobile.network.dto.ResponseDto;
import com.example.wanderfunmobile.network.dto.auth.EmptyDataDto;
import com.example.wanderfunmobile.network.dto.auth.RegisterDto;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class RegisterViewModel extends ViewModel {
    private final AuthApi authApi;
    private final MutableLiveData<String> liveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    @Inject
    public RegisterViewModel(AuthApi authApi) {
        this.authApi = authApi;
    }

    public LiveData<String> getLiveData() { return liveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }

    public void register(RegisterDto registerDto) {
        try {
            Call<ResponseDto<EmptyDataDto>> call = authApi.register(registerDto);
            call.enqueue(new Callback<ResponseDto<EmptyDataDto>>() {
                @Override
                public void onResponse(Call<ResponseDto<EmptyDataDto>> call, Response<ResponseDto<EmptyDataDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        liveData.postValue(response.body().getMessage());
                    } else {
                        String errorMessage = "Register failed";
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
                public void onFailure(Call<ResponseDto<EmptyDataDto>> call, Throwable t) {
                    errorLiveData.postValue(t.getMessage());
                }
            });
        } catch (Exception e) {
            errorLiveData.postValue(e.getMessage());
        }
    }
}
