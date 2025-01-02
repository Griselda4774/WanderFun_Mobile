package com.example.wanderfunmobile.network.backend;

import com.example.wanderfunmobile.application.dto.EmptyDataDto;
import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.user.ChangeInfoDto;
import com.example.wanderfunmobile.application.dto.user.SelfInfoDto;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface UserApi {
    @GET("user/self")
    Call<ResponseDto<Object>> getSelfInfo(@Header("Authorization") String accessToken);

    @PUT("user/self")
    Call<ResponseDto<Object>> updateSelfInfo(@Header("Authorization") String accessToken,
                                                   @Body ChangeInfoDto changeInfoDto);

    @DELETE("user/self")
    Call<ResponseDto<Object>> deleteSelf(@Header("Authorization") String accessToken);
}
