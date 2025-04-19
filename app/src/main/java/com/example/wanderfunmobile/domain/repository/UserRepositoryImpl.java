package com.example.wanderfunmobile.domain.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.user.ChangeInfoDto;
import com.example.wanderfunmobile.data.dto.user.SelfInfoDto;
import com.example.wanderfunmobile.data.repository.UserRepository;
import com.example.wanderfunmobile.data.api.backend.UserApi;

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
    public LiveData<ResponseDto<SelfInfoDto>> getSelfInfo(String bearerToken) {
        MutableLiveData<ResponseDto<SelfInfoDto>> getSelfInfoResponseLiveData = new MutableLiveData<>();
        String errorType = "UserRepositoryImpl GetSelfInfo Error";

        try {
            Call<ResponseDto<SelfInfoDto>> call = userApi.getSelfInfo(bearerToken);
            call.enqueue(new Callback<ResponseDto<SelfInfoDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<SelfInfoDto>> call,
                                       @NonNull Response<ResponseDto<SelfInfoDto>> response) {
                    getSelfInfoResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<SelfInfoDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getSelfInfoResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<SelfInfoDto>> updateSelfInfo(String bearerToken, ChangeInfoDto changeInfoDto) {
        MutableLiveData<ResponseDto<SelfInfoDto>> updateSelfInfoResponseLiveData = new MutableLiveData<>();
        String errorType = "UserRepositoryImpl UpdateSelfInfo Error";

        try {
            Call<ResponseDto<SelfInfoDto>> call = userApi.updateSelfInfo(bearerToken, changeInfoDto);
            call.enqueue(new Callback<ResponseDto<SelfInfoDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<SelfInfoDto>> call,
                                       @NonNull Response<ResponseDto<SelfInfoDto>> response) {
                    updateSelfInfoResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<SelfInfoDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return updateSelfInfoResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<SelfInfoDto>> deleteSelf(String bearerToken) {
        MutableLiveData<ResponseDto<SelfInfoDto>> deleteSelfResponseLiveData = new MutableLiveData<>();
        String errorType = "UserRepositoryImpl DeleteSelf Error";

        try {
            Call<ResponseDto<SelfInfoDto>> call = userApi.deleteSelf(bearerToken);
            call.enqueue(new Callback<ResponseDto<SelfInfoDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<SelfInfoDto>> call,
                                       @NonNull Response<ResponseDto<SelfInfoDto>> response) {
                    deleteSelfResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<SelfInfoDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return deleteSelfResponseLiveData;
    }
}
