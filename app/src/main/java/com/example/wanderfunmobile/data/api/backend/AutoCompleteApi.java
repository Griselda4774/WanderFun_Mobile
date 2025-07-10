package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.addresses.ProvinceDto;
import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AutoCompleteApi {
    @GET("autocomplete/place")
    Call<ResponseDto<List<MiniPlaceDto>>> autoCompleteSearchPlace(@Query("keyword") String keyword);
    @GET("autocomplete/province")
    Call<ResponseDto<List<ProvinceDto>>> autoCompleteSearchProvince(@Query("keyword") String keyword);
}
