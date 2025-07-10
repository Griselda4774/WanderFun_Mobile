package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.api.backend.AutoCompleteApi;
import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.addresses.ProvinceDto;
import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.addresses.Province;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.repository.AutoCompleteRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteRepositoryImpl implements AutoCompleteRepository {
    private final AutoCompleteApi autoCompleteApi;
    private final ObjectMapper objectMapper;

    @Inject
    public AutoCompleteRepositoryImpl(AutoCompleteApi autoCompleteApi, ObjectMapper objectMapper) {
        this.autoCompleteApi = autoCompleteApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public LiveData<Result<List<Place>>> autoCompleteSearchPlace(String keyword) {
        MutableLiveData<Result<List<Place>>> autoCompleteSearchPlaceResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<MiniPlaceDto>>> call = autoCompleteApi.autoCompleteSearchPlace(keyword);
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
                        autoCompleteSearchPlaceResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<MiniPlaceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("AutoCompleteRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("AutoCompleteRepositoryImpl", "Error during findAllPostsByCursor", e);
        }

        return autoCompleteSearchPlaceResponseLiveData;
    }

    @Override
    public LiveData<Result<List<Province>>> autoCompleteSearchProvince(String keyword) {
        MutableLiveData<Result<List<Province>>> autoCompleteSearchProvinceResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<ProvinceDto>>> call = autoCompleteApi.autoCompleteSearchProvince(keyword);
            call.enqueue(new Callback<ResponseDto<List<ProvinceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<ProvinceDto>>> call,
                                       @NonNull Response<ResponseDto<List<ProvinceDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Province>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Province.class));
                        }
                        autoCompleteSearchProvinceResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<ProvinceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("AutoCompleteRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("AutoCompleteRepositoryImpl", "Error during findAllPostsByCursor", e);
        }

        return autoCompleteSearchProvinceResponseLiveData;
    }
}
