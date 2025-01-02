package com.example.wanderfunmobile.infrastructure.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.user.ChangeInfoDto;
import com.example.wanderfunmobile.application.repository.UserRepository;
import com.example.wanderfunmobile.infrastructure.util.ErrorGenerateUtil;
import com.example.wanderfunmobile.network.backend.UserApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepositoryImpl implements UserRepository {
    private final UserApi userApi;

    @Inject
    public UserRepositoryImpl(UserApi userApi) {
        this.userApi = userApi;
    }

    @Override
    public LiveData<ResponseDto<Object>> getSelfInfo(String bearerToken) {
        MutableLiveData<ResponseDto<Object>> getSelfInfoResponseLiveData = new MutableLiveData<>();
        String errorType = "UserRepositoryImpl GetSelfInfo Error";

        try {
            Call<ResponseDto<Object>> call = userApi.getSelfInfo(bearerToken);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getSelfInfoResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        getSelfInfoResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    getSelfInfoResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            getSelfInfoResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return getSelfInfoResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> updateSelfInfo(String bearerToken, ChangeInfoDto changeInfoDto) {
        MutableLiveData<ResponseDto<Object>> updateSelfInfoResponseLiveData = new MutableLiveData<>();
        String errorType = "UserRepositoryImpl UpdateSelfInfo Error";

        try {
            Call<ResponseDto<Object>> call = userApi.updateSelfInfo(bearerToken, changeInfoDto);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateSelfInfoResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        updateSelfInfoResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    updateSelfInfoResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            updateSelfInfoResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return updateSelfInfoResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> deleteSelf(String bearerToken) {
        MutableLiveData<ResponseDto<Object>> deleteSelfResponseLiveData = new MutableLiveData<>();
        String errorType = "UserRepositoryImpl DeleteSelf Error";

        try {
            Call<ResponseDto<Object>> call = userApi.deleteSelf(bearerToken);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        deleteSelfResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        deleteSelfResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    deleteSelfResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            deleteSelfResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return deleteSelfResponseLiveData;
    }
}
