package com.example.wanderfunmobile.application.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.album.AlbumCreateDto;

public interface AlbumRepository {
    LiveData<ResponseDto<Object>> getAllAlbums(String bearerToken);
    LiveData<ResponseDto<Object>> getAlbumById(String bearerToken, Long albumId);
    LiveData<ResponseDto<Object>> createAlbum(String bearerToken, AlbumCreateDto albumCreateDto);
    LiveData<ResponseDto<Object>> updateAlbumById(String bearerToken, Long albumId, AlbumCreateDto albumCreateDto);
    LiveData<ResponseDto<Object>> deleteAllAlbums(String bearerToken);
    LiveData<ResponseDto<Object>> deleteAlbumById(String bearerToken, Long albumId);
}
