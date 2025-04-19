package com.example.wanderfunmobile.data.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.domain.model.addresses.District;
import com.example.wanderfunmobile.domain.model.addresses.Province;
import com.example.wanderfunmobile.domain.model.addresses.Ward;

import java.util.List;

public interface AddressRepository {
    LiveData<List<Province>> findAllProvinces();
    LiveData<List<Province>> findAllProvincesByNameContaining(String name);
    LiveData<Province> findProvinceByName(String name);
    LiveData<List<District>> findAllDistrictsByProvinceCode(String provinceCode);
    LiveData<District> findDistrictByNameAndProvinceCode(String name, String provinceCode);
    LiveData<List<Ward>> findAllWardsByDistrictCode(String districtCode);
    LiveData<Ward> findWardByNameAndDistrictCode(String name, String districtCode);
}
