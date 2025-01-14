package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.album.AlbumCreateDto;
import com.example.wanderfunmobile.application.dto.album.AlbumDto;
import com.example.wanderfunmobile.application.repository.AlbumRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AlbumViewModel extends ViewModel {
    private final AlbumRepository albumRepository;

    @Inject
    public AlbumViewModel(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    private final MutableLiveData<ResponseDto<List<AlbumDto>>> getAllAlbumsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<AlbumDto>> getAlbumByIdResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<AlbumDto>> createAlbumResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<AlbumDto>> updateAlbumResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<AlbumDto>> deleteAllAlbumsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<AlbumDto>> deleteAlbumResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<ResponseDto<List<AlbumDto>>> getAllAlbumsResponseLiveData() {
        return getAllAlbumsResponseLiveData;
    }

    public LiveData<ResponseDto<AlbumDto>> getAlbumByIdResponseLiveData() {
        return getAlbumByIdResponseLiveData;
    }

    public LiveData<ResponseDto<AlbumDto>> createAlbumResponseLiveData() {
        return createAlbumResponseLiveData;
    }

    public LiveData<ResponseDto<AlbumDto>> updateAlbumResponseLiveData() {
        return updateAlbumResponseLiveData;
    }


    public LiveData<ResponseDto<AlbumDto>> deleteAllAlbumsResponseLiveData() {
        return deleteAllAlbumsResponseLiveData;
    }

    public LiveData<ResponseDto<AlbumDto>> deleteAlbumResponseLiveData() {
        return deleteAlbumResponseLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void getAllAlbums(String bearerToken) {
        isLoading.setValue(true);
        albumRepository.getAllAlbums(bearerToken).observeForever(response -> {
            getAllAlbumsResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void getAlbumById(String bearerToken, Long albumId) {
        isLoading.setValue(true);
        albumRepository.getAlbumById(bearerToken, albumId).observeForever(response -> {
            getAlbumByIdResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }


    public void createAlbum(String bearerToken, AlbumCreateDto albumCreateDto) {
        isLoading.setValue(true);
        albumRepository.createAlbum(bearerToken, albumCreateDto).observeForever(response -> {
            createAlbumResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void updateAlbumById(String bearerToken, Long albumId, AlbumCreateDto albumCreateDto) {
        isLoading.setValue(true);
        albumRepository.updateAlbumById(bearerToken, albumId, albumCreateDto).observeForever(response -> {
            updateAlbumResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void deleteAllAlbums(String bearerToken) {
        isLoading.setValue(true);
        albumRepository.deleteAllAlbums(bearerToken).observeForever(response -> {
            deleteAllAlbumsResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }

    public void deleteAlbumById(String bearerToken, Long albumId) {
        isLoading.setValue(true);
        albumRepository.deleteAlbumById(bearerToken, albumId).observeForever(response -> {
            deleteAlbumResponseLiveData.setValue(response);
            isLoading.setValue(false);
        });
    }
}
