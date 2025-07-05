package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.api.backend.CheckInApi;
import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.checkin.CheckInDto;
import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.checkins.CheckIn;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.repository.CheckInRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInRepositoryImpl implements CheckInRepository {
    private final CheckInApi checkInApi;
    private final ObjectMapper objectMapper;

    @Inject
    public CheckInRepositoryImpl(CheckInApi checkInApi, ObjectMapper objectMapper) {
        this.checkInApi = checkInApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public LiveData<Result<List<CheckIn>>> findAllByUser(String bearerToken) {
        MutableLiveData<Result<List<CheckIn>>> findAllByUserResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<CheckInDto>>> call = checkInApi.findAllByUser(bearerToken);
            call.enqueue(new Callback<ResponseDto<List<CheckInDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<CheckInDto>>> call,
                                       @NonNull Response<ResponseDto<List<CheckInDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<CheckIn>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), CheckIn.class));
                        }
                        findAllByUserResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<CheckInDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("CheckInRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("CheckInRepositoryImpl", "Error during findAllPostsByCursor", e);
        }

        return findAllByUserResponseLiveData;
    }

    @Override
    public LiveData<Result<CheckIn>> createCheckIn(String bearerToken, Long placeId) {
        MutableLiveData<Result<CheckIn>> createCheckInResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<CheckInDto>> call = checkInApi.createCheckIn(bearerToken, placeId);
            call.enqueue(new Callback<ResponseDto<CheckInDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<CheckInDto>> call,
                                       @NonNull Response<ResponseDto<CheckInDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<CheckIn> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), CheckIn.class));
                        }
                        createCheckInResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<CheckInDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("CheckInRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("CheckInRepositoryImpl", "Error during create comment", e);
        }
        return createCheckInResponseLiveData;
    }

    @Override
    public LiveData<Result<List<Place>>> findAllEligiblePlaces(String bearerToken, Double userLng, Double userLat) {
        MutableLiveData<Result<List<Place>>> findAllEligiblePlacesResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<MiniPlaceDto>>> call = checkInApi.findAllEligiblePlaces(bearerToken, userLng, userLat);
            call.enqueue(new Callback<ResponseDto<List<MiniPlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<MiniPlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<MiniPlaceDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Place>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Place.class));
                        }
                        findAllEligiblePlacesResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<MiniPlaceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("CheckInRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("CheckInRepositoryImpl", "Error during create comment", e);
        }
        return findAllEligiblePlacesResponseLiveData;
    }
}
