package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.users.User;

public interface UserRepository {
//    LiveData<Result<User>> getSelfInfo(String bearerToken);
    LiveData<Result<User>> getMiniSelfInfo(String bearerToken);

//    LiveData<ResponseDto<SelfInfoDto>> updateSelfInfo(String bearerToken, ChangeInfoDto changeInfoDto);
//
//    LiveData<ResponseDto<SelfInfoDto>> deleteSelf(String bearerToken);
}
