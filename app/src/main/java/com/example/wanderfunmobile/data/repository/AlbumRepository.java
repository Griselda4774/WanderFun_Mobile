package com.example.wanderfunmobile.data.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.album.AlbumCreateDto;
import com.example.wanderfunmobile.data.dto.album.AlbumDto;

import java.util.List;

public interface AlbumRepository {
    LiveData<ResponseDto<List<AlbumDto>>> getAllAlbums(String bearerToken);

    LiveData<ResponseDto<AlbumDto>> getAlbumById(String bearerToken, Long albumId);

    LiveData<ResponseDto<AlbumDto>> createAlbum(String bearerToken, AlbumCreateDto albumCreateDto);

    LiveData<ResponseDto<AlbumDto>> updateAlbumById(String bearerToken, Long albumId, AlbumCreateDto albumCreateDto);

    LiveData<ResponseDto<AlbumDto>> deleteAllAlbums(String bearerToken);

    LiveData<ResponseDto<AlbumDto>> deleteAlbumById(String bearerToken, Long albumId);
}
