package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.user.ChangeInfoDto;
import com.example.wanderfunmobile.data.dto.user.SelfInfoDto;

public interface UserRepository {
    LiveData<ResponseDto<SelfInfoDto>> getSelfInfo(String bearerToken);

    LiveData<ResponseDto<SelfInfoDto>> updateSelfInfo(String bearerToken, ChangeInfoDto changeInfoDto);

    LiveData<ResponseDto<SelfInfoDto>> deleteSelf(String bearerToken);
}
