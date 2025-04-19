package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.addresses.DistrictDto;
import com.example.wanderfunmobile.data.dto.addresses.ProvinceDto;
import com.example.wanderfunmobile.data.dto.addresses.WardDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AddressApi {
    @GET("address/province")
    Call<ResponseDto<List<ProvinceDto>>> findAllProvinces();
    @GET("address/province/search/{key}")
    Call<ResponseDto<List<ProvinceDto>>> findAllProvincesByNameContaining(@Path("key")String key);
    @GET("address/province/{name}")
    Call<ResponseDto<List<ProvinceDto>>> findProvinceByName(@Path("name")String name);
    @GET("address/district/{provinceCode}")
    Call<ResponseDto<List<DistrictDto>>> findAllDistrictsByProvinceCode(@Path("provinceCode")String provinceCode);
    @GET("address/district/{name}/{provinceCode}")
    Call<ResponseDto<DistrictDto>> findDistrictByNameAndProvinceCode(@Path("name")String name, @Path("provinceCode")String provinceCode);
    @GET("address/ward/{districtCode}")
    Call<ResponseDto<List<WardDto>>> findAllWardsByDistrictCode(@Path("districtCode")String districtCode);
    @GET("address/ward/{name}/{districtCode}")
    Call<ResponseDto<ProvinceDto>> findWardByNameAndDistrictCode(@Path("name")String name, @Path("districtCode")String districtCode);
}
