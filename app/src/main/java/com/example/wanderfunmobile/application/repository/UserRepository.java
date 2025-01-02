package com.example.wanderfunmobile.application.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.user.ChangeInfoDto;

public interface UserRepository {
    LiveData<ResponseDto<Object>> getSelfInfo(String bearerToken);
    LiveData<ResponseDto<Object>> updateSelfInfo(String bearerToken, ChangeInfoDto changeInfoDto);
    LiveData<ResponseDto<Object>> deleteSelf(String bearerToken);
}
