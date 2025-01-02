package com.example.wanderfunmobile.infrastructure.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.repository.PlaceRepository;
import com.example.wanderfunmobile.infrastructure.util.ErrorGenerateUtil;
import com.example.wanderfunmobile.network.backend.PlaceApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepositoryImpl implements PlaceRepository {
    private final PlaceApi placeApi;

    @Inject
    public PlaceRepositoryImpl(PlaceApi placeApi) {
        this.placeApi = placeApi;
    }

    @Override
    public LiveData<ResponseDto<Object>> getAllPlaces() {
        MutableLiveData<ResponseDto<Object>> getAllPlacesResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl GetAllPlaces Error";
        try {
            Call<ResponseDto<Object>> call = placeApi.getAllPlaces();
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getAllPlacesResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        getAllPlacesResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    getAllPlacesResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            getAllPlacesResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return getAllPlacesResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> getPlaceById(Long placeId) {
        MutableLiveData<ResponseDto<Object>> getPlaceByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl GetPlaceById Error";

        try {
            Call<ResponseDto<Object>> call = placeApi.getPlaceById(placeId);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getPlaceByIdResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        getPlaceByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    getPlaceByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            getPlaceByIdResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return getPlaceByIdResponseLiveData;
    }
}
