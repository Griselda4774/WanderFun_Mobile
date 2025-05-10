package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.addresses.ProvinceDto;
import com.example.wanderfunmobile.domain.model.addresses.District;
import com.example.wanderfunmobile.domain.model.addresses.Province;
import com.example.wanderfunmobile.domain.model.addresses.Ward;
import com.example.wanderfunmobile.data.api.backend.AddressApi;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.repository.AddressRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepositoryImpl implements AddressRepository {
    private final AddressApi addressApi;
    private final ObjectMapper objectMapper;

    @Inject
    public AddressRepositoryImpl(AddressApi addressApi, ObjectMapper objectMapper) {
        this.addressApi = addressApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public LiveData<List<Province>> findAllProvinces() {
        MutableLiveData<List<Province>> findAllProvincesLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<ProvinceDto>>> call = addressApi.findAllProvinces();
            call.enqueue(new Callback<ResponseDto<List<ProvinceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<ProvinceDto>>> call,
                                       @NonNull Response<ResponseDto<List<ProvinceDto>>> response) {
                    if (response.body() != null) {
                        if (!response.body().isError() && response.body().getData() != null) {
                            List<ProvinceDto> provinceDtos = response.body().getData();
                            List<Province> provinces = objectMapper.mapList(provinceDtos, Province.class);
                            findAllProvincesLiveData.postValue(provinces);
                        } else {
                            Log.e("AddressRepositoryImpl", "Error: " + response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<ProvinceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("AddressRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });

        } catch (Exception e) {
            Log.e("AddressRepositoryImpl", "Error during catch: " + e.getMessage());
        }

        return findAllProvincesLiveData;
    }

    @Override
    public LiveData<List<Province>> findAllProvincesByNameContaining(String name) {
        return null;
    }

    @Override
    public LiveData<Province> findProvinceByName(String name) {
        return null;
    }

    @Override
    public LiveData<List<District>> findAllDistrictsByProvinceCode(String provinceCode) {
        return null;
    }

    @Override
    public LiveData<District> findDistrictByNameAndProvinceCode(String name, String provinceCode) {
        return null;
    }

    @Override
    public LiveData<List<Ward>> findAllWardsByDistrictCode(String districtCode) {
        return null;
    }

    @Override
    public LiveData<Ward> findWardByNameAndDistrictCode(String name, String districtCode) {
        return null;
    }
}
