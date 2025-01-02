package com.example.wanderfunmobile.network.backend;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.album.AlbumCreateDto;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AlbumApi {
    @GET("album")
    Call<ResponseDto<Object>> getAllAlbums(@Header("Authorization") String bearerToken);

    @GET("album/{albumId}")
    Call<ResponseDto<Object>> getAlbumById(@Header("Authorization") String bearerToken,
                                             @Path("albumId") Long albumId);

    @POST("album")
    Call<ResponseDto<Object>> createAlbum(@Header("Authorization") String bearerToken,
                                                @Body AlbumCreateDto albumCreateDto);

    @PUT("album/{albumId}")
    Call<ResponseDto<Object>> updateAlbum(@Header("Authorization") String bearerToken,
                                                @Path("albumId") Long albumId,
                                                @Body AlbumCreateDto albumCreateDto);

    @DELETE("album")
    Call<ResponseDto<Object>> deleteAllAlbums(@Header("Authorization") String bearerToken);

    @DELETE("album/{albumId}")
    Call<ResponseDto<Object>> deleteAlbum(@Header("Authorization") String bearerToken,
                                                @Path("albumId") Long albumId);
}
