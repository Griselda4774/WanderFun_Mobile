package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.auth.ChangePasswordDto;
import com.example.wanderfunmobile.data.dto.auth.ForgotPasswordDto;
import com.example.wanderfunmobile.data.dto.auth.LoginDto;
import com.example.wanderfunmobile.data.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.data.dto.auth.MailOtpDto;
import com.example.wanderfunmobile.data.dto.auth.RegisterDto;
import com.example.wanderfunmobile.data.dto.auth.TokenResponseDto;
import com.example.wanderfunmobile.data.api.backend.AuthApi;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.repository.AuthRepository;

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
    public LiveData<ResponseDto<LoginResponseDto>> login(LoginDto loginDto) {
        MutableLiveData<ResponseDto<LoginResponseDto>> loginResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Login Error";
        try {
            Call<ResponseDto<LoginResponseDto>> call = authApi.login(loginDto);
            call.enqueue(new Callback<ResponseDto<LoginResponseDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<LoginResponseDto>> call,
                                       @NonNull Response<ResponseDto<LoginResponseDto>> response) {
                    loginResponseLiveData.postValue(response.body());
                }
                @Override
                public void onFailure(@NonNull Call<ResponseDto<LoginResponseDto>> call,
                                      @NonNull Throwable t) {
                    Log.e(errorType, "Error during login onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during login catch");
        }

        return loginResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<LoginResponseDto>> register(RegisterDto registerDto) {
        MutableLiveData<ResponseDto<LoginResponseDto>> registerResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Register Error";
        try {
            Call<ResponseDto<LoginResponseDto>> call = authApi.register(registerDto);
            call.enqueue(new Callback<ResponseDto<LoginResponseDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<LoginResponseDto>> call,
                                       @NonNull Response<ResponseDto<LoginResponseDto>> response) {
                    registerResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<LoginResponseDto>> call,
                                       @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during register onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during register catch");
        }

        return registerResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<LoginResponseDto>> logout(String bearerToken) {
        MutableLiveData<ResponseDto<LoginResponseDto>> logoutResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Logout Error";
        try {
            Call<ResponseDto<LoginResponseDto>> call = authApi.logout(bearerToken);
            call.enqueue(new Callback<ResponseDto<LoginResponseDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<LoginResponseDto>> call,
                                       @NonNull Response<ResponseDto<LoginResponseDto>> response) {
                    logoutResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<LoginResponseDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during logout onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during logout catch");
        }

        return logoutResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<TokenResponseDto>> refreshToken(String bearerToken) {
        MutableLiveData<ResponseDto<TokenResponseDto>> refreshTokenResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Refresh Token Error";
        try {
            Call<ResponseDto<TokenResponseDto>> call = authApi.refreshToken(bearerToken);
            call.enqueue(new Callback<ResponseDto<TokenResponseDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<TokenResponseDto>> call,
                                       @NonNull Response<ResponseDto<TokenResponseDto>> response) {
                    refreshTokenResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<TokenResponseDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return refreshTokenResponseLiveData;
    }

    @Override
    public LiveData<Result<Void>> sendOtp(String email) {
        MutableLiveData<Result<Void>> sendOtpResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Send OTP Error";
        try {
            Call<ResponseDto<Void>> call = authApi.sendOtp(email);
            call.enqueue(new Callback<ResponseDto<Void>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Void>> call,
                                       @NonNull Response<ResponseDto<Void>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Void> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        sendOtpResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Void>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during send OTP onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during send OTP catch");
        }

        return sendOtpResponseLiveData;
    }

    @Override
    public LiveData<Result<Void>> verifyOtp(MailOtpDto mailOtpDto) {
        MutableLiveData<Result<Void>> verifyOtpResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Verify OTP Error";
        try {
            Call<ResponseDto<Void>> call = authApi.verifyOtp(mailOtpDto);
            call.enqueue(new Callback<ResponseDto<Void>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Void>> call,
                                       @NonNull Response<ResponseDto<Void>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Void> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        verifyOtpResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Void>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during verify OTP onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during verify OTP catch");
        }

        return verifyOtpResponseLiveData;
    }

    @Override
    public LiveData<Result<Void>> forgotPassword(String email, String otpCode, String newPassword) {
        MutableLiveData<Result<Void>> forgotPasswordResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Verify OTP Error";
        try {
            ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
            forgotPasswordDto.setEmail(email);
            forgotPasswordDto.setOtp(otpCode);
            forgotPasswordDto.setNewPassword(newPassword);
            Call<ResponseDto<Void>> call = authApi.forgotPassword(forgotPasswordDto);
            call.enqueue(new Callback<ResponseDto<Void>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Void>> call,
                                       @NonNull Response<ResponseDto<Void>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Void> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        forgotPasswordResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Void>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during forgot password onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during forgot password catch");
        }

        return forgotPasswordResponseLiveData;
    }

    @Override
    public LiveData<Result<Void>> changePassword(String bearerToken, String oldPassword, String newPassword) {
        MutableLiveData<Result<Void>> changePasswordResponseLiveData = new MutableLiveData<>();
        String errorType = "AuthRepositoryImpl Verify OTP Error";
        try {
            ChangePasswordDto changePasswordDto = new ChangePasswordDto();
            changePasswordDto.setNewPassword(newPassword);
            changePasswordDto.setOldPassword(oldPassword);
            Call<ResponseDto<Void>> call = authApi.changePassword(bearerToken, changePasswordDto);
            call.enqueue(new Callback<ResponseDto<Void>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Void>> call,
                                       @NonNull Response<ResponseDto<Void>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Void> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        changePasswordResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Void>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during change password onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during change password catch");
        }

        return changePasswordResponseLiveData;
    }
}
