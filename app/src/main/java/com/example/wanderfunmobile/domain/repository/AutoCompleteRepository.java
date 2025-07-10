package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.addresses.Province;
import com.example.wanderfunmobile.domain.model.places.Place;

import java.util.List;

public interface AutoCompleteRepository {
    LiveData<Result<List<Place>>> autoCompleteSearchPlace(String keyword);
    LiveData<Result<List<Province>>> autoCompleteSearchProvince(String keyword);
}
