package com.example.wanderfunmobile.network.backend;

import com.example.wanderfunmobile.network.dto.EmptyDataDto;
import com.example.wanderfunmobile.network.dto.ResponseDto;
import com.example.wanderfunmobile.network.dto.album.AlbumCreateDto;
import com.example.wanderfunmobile.network.dto.album.AlbumDto;

import java.util.List;

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
    Call<ResponseDto<List<AlbumDto>>> getAllAlbums(@Header("Authorization") String bearerToken);

    @GET("album/{albumId}")
    Call<ResponseDto<AlbumDto>> getAlbumById(@Header("Authorization") String bearerToken,
                                             @Path("albumId") Long albumId);

    @POST("album")
    Call<ResponseDto<EmptyDataDto>> createAlbum(@Header("Authorization") String bearerToken,
                                                @Body AlbumCreateDto albumCreateDto);

    @PUT("album/{albumId}")
    Call<ResponseDto<EmptyDataDto>> updateAlbum(@Header("Authorization") String bearerToken,
                                                @Path("albumId") Long albumId,
                                                @Body AlbumCreateDto albumCreateDto);

    @DELETE("album")
    Call<ResponseDto<EmptyDataDto>> deleteAllAlbums(@Header("Authorization") String bearerToken);

    @DELETE("album/{albumId}")
    Call<ResponseDto<EmptyDataDto>> deleteAlbum(@Header("Authorization") String bearerToken,
                                                @Path("albumId") Long albumId);
}
