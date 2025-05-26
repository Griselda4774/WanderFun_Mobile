package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.user.ChangeInfoDto;
import com.example.wanderfunmobile.data.dto.user.MiniUserDto;
import com.example.wanderfunmobile.data.dto.user.SelfInfoDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface UserApi {
    @GET("user/self")
    Call<ResponseDto<SelfInfoDto>> getSelfInfo(@Header("Authorization") String accessToken);
//
//    @PUT("user/self")
//    Call<ResponseDto<SelfInfoDto>> updateSelfInfo(@Header("Authorization") String accessToken,
//                                                   @Body ChangeInfoDto changeInfoDto);
//
//    @DELETE("user/self")
//    Call<ResponseDto<SelfInfoDto>> deleteSelf(@Header("Authorization") String accessToken);

    @GET("user/self/mini")
    Call<ResponseDto<MiniUserDto>> getMiniSelfInfo(@Header("Authorization") String accessToken);
}
