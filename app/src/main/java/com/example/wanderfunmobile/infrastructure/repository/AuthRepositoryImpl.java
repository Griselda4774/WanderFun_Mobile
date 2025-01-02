package com.example.wanderfunmobile.infrastructure.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.auth.LoginDto;
import com.example.wanderfunmobile.application.dto.auth.RegisterDto;
import com.example.wanderfunmobile.application.repository.AuthRepository;
import com.example.wanderfunmobile.infrastructure.util.ErrorGenerateUtil;
import com.example.wanderfunmobile.network.backend.AuthApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthApi authApi;

    @Inject
    public AuthRepositoryImpl(AuthApi authApi) {
        this.authApi = authApi;
    }

    @Override
    public LiveData<ResponseDto<Object>> login(LoginDto loginDto) {
        MutableLiveData<ResponseDto<Object>> loginResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Login Error";
        try {
            Call<ResponseDto<Object>> call = authApi.login(loginDto);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        loginResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during login onResponse");
                        loginResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable t) {
                    Log.e(errorType, "Error during login onFailure");
                    loginResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during login catch");
            loginResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return loginResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> register(RegisterDto registerDto) {
        MutableLiveData<ResponseDto<Object>> registerResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Register Error";
        try {
            Call<ResponseDto<Object>> call = authApi.register(registerDto);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        registerResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during register onResponse");
                        registerResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during register onFailure");
                    registerResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during register catch");
            registerResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return registerResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> logout(String bearerToken) {
        MutableLiveData<ResponseDto<Object>> logoutResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Logout Error";
        try {
            Call<ResponseDto<Object>> call = authApi.logout(bearerToken);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        logoutResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during logout onResponse");
                        logoutResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during logout onFailure");
                    logoutResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during logout catch");
            logoutResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return logoutResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> refreshToken(String bearerToken) {
        MutableLiveData<ResponseDto<Object>> refreshTokenResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Refresh Token Error";
        try {
            Call<ResponseDto<Object>> call = authApi.refreshToken(bearerToken);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        refreshTokenResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        refreshTokenResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    refreshTokenResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            refreshTokenResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return refreshTokenResponseLiveData;
    }
}
