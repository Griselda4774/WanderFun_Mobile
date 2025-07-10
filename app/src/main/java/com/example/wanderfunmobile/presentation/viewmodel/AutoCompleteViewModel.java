package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.addresses.Province;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.repository.AutoCompleteRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AutoCompleteViewModel extends ViewModel {
    private final AutoCompleteRepository autoCompleteRepository;

    @Inject
    public AutoCompleteViewModel(AutoCompleteRepository autoCompleteRepository) {
        this.autoCompleteRepository = autoCompleteRepository;
    }

    private final MutableLiveData<Result<List<Place>>> autoCompleteSearchPlaceLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Province>>> autoCompleteSearchProvinceLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Result<List<Place>>> getAutoCompleteSearchPlaceLiveData() {
        return autoCompleteSearchPlaceLiveData;
    }

    public MutableLiveData<Result<List<Province>>> getAutoCompleteSearchProvinceLiveData() {
        return autoCompleteSearchProvinceLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void autoCompleteSearchPlace(String keyword) {
        isLoading.setValue(true);
        autoCompleteRepository.autoCompleteSearchPlace(keyword).observeForever(result -> {
            isLoading.setValue(false);
            autoCompleteSearchPlaceLiveData.setValue(result);
        });
    }

    public void autoCompleteSearchProvince(String keyword) {
        isLoading.setValue(true);
        autoCompleteRepository.autoCompleteSearchProvince(keyword).observeForever(result -> {
            isLoading.setValue(false);
            autoCompleteSearchProvinceLiveData.setValue(result);
        });
    }
}