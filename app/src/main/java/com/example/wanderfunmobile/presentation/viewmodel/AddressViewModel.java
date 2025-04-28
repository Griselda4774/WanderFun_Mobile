package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.domain.model.addresses.Province;
import com.example.wanderfunmobile.domain.repository.AddressRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddressViewModel extends ViewModel {
    private final AddressRepository addressRepository;
    
    @Inject
    public AddressViewModel(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    private final MutableLiveData<List<Province>> findAllProvincesResponseLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<List<Province>> getAllProvincesResponseLiveData() {
        return findAllProvincesResponseLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void findAllProvinces() {
        isLoading.setValue(true);
        addressRepository.findAllProvinces().observeForever(response -> {
            findAllProvincesResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }
}
