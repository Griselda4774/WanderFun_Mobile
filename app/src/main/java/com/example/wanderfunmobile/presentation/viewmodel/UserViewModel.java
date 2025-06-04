package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.users.User;
import com.example.wanderfunmobile.domain.repository.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final MutableLiveData<Result<User>> getSelfInfoResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<User>> getMiniSelfInfoResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<User>> updateSelfInfoResponseLiveData = new MutableLiveData<>();
//    private final MutableLiveData<ResponseDto<SelfInfoDto>> deleteSelfResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Result<User>> getSelfInfoResponseLiveData() {
        return getSelfInfoResponseLiveData;
    }

    public MutableLiveData<Result<User>> getMiniSelfInfoResponseLiveData() {
        return getMiniSelfInfoResponseLiveData;
    }

//
    public MutableLiveData<Result<User>> getUpdateSelfInfoResponseLiveData() {
        return updateSelfInfoResponseLiveData;
    }
//
//    public MutableLiveData<ResponseDto<SelfInfoDto>> deleteSelfResponseLiveData() {
//        return deleteSelfResponseLiveData;
//    }

    public MutableLiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void getSelfInfo(String bearerToken) {
        isLoading.setValue(true);
        userRepository.getSelfInfo(bearerToken).observeForever(response -> {
            getSelfInfoResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void getMiniSelfInfo(String bearerToken) {
        isLoading.setValue(true);
        userRepository.getMiniSelfInfo(bearerToken).observeForever(response -> {
            getMiniSelfInfoResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void updateSelfInfo(String bearerToken, User user) {
        isLoading.setValue(true);
        userRepository.updateSelfInfo(bearerToken, user).observeForever(response -> {
            updateSelfInfoResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }
//
//    public void deleteSelf(String bearerToken) {
//        isLoading.setValue(true);
//        userRepository.deleteSelf(bearerToken).observeForever(response -> {
//            deleteSelfResponseLiveData.setValue(response);
//            isLoading.setValue(false);
//        });
//    }
}
