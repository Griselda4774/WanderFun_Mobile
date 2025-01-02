package com.example.wanderfunmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.album.AlbumCreateDto;
import com.example.wanderfunmobile.application.repository.AlbumRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AlbumViewModel extends ViewModel {
    private final AlbumRepository albumRepository;

    @Inject
    public AlbumViewModel(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    private final MutableLiveData<ResponseDto<Object>> getAllAlbumsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> getAlbumByIdResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> createAlbumResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> updateAlbumResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> deleteAllAlbumsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseDto<Object>> deleteAlbumResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<ResponseDto<Object>> getAllAlbumsResponseLiveData() {
        return getAllAlbumsResponseLiveData;
    }

    public LiveData<ResponseDto<Object>> getAlbumByIdResponseLiveData() {
        return getAlbumByIdResponseLiveData;
    }

    public LiveData<ResponseDto<Object>> createAlbumResponseLiveData() {
        return createAlbumResponseLiveData;
    }

    public LiveData<ResponseDto<Object>> updateAlbumResponseLiveData() {
        return updateAlbumResponseLiveData;
    }


    public LiveData<ResponseDto<Object>> deleteAllAlbumsResponseLiveData() {
        return deleteAllAlbumsResponseLiveData;
    }

    public LiveData<ResponseDto<Object>> deleteAlbumResponseLiveData() {
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
