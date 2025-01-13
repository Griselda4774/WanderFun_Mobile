package com.example.wanderfunmobile.infrastructure.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.album.AlbumCreateDto;
import com.example.wanderfunmobile.application.dto.album.AlbumDto;
import com.example.wanderfunmobile.application.repository.AlbumRepository;
import com.example.wanderfunmobile.infrastructure.api.backend.AlbumApi;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumRepositoryImpl implements AlbumRepository {
    private final AlbumApi albumApi;

    @Inject
    public AlbumRepositoryImpl(AlbumApi albumApi) {
        this.albumApi = albumApi;
    }

    @Override
    public LiveData<ResponseDto<List<AlbumDto>>> getAllAlbums(String bearerToken) {
        MutableLiveData<ResponseDto<List<AlbumDto>>> getAllAlbumsResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl GetAllAlbums Error";
        try {
            Call<ResponseDto<List<AlbumDto>>> call = albumApi.getAllAlbums(bearerToken);
            call.enqueue(new Callback<ResponseDto<List<AlbumDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<AlbumDto>>> call,
                                       @NonNull Response<ResponseDto<List<AlbumDto>>> response) {
                    getAllAlbumsResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<AlbumDto>>> call, 
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getAllAlbumsResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<AlbumDto>> getAlbumById(String bearerToken, Long albumId) {
        MutableLiveData<ResponseDto<AlbumDto>> getAlbumByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl GetAlbumById Error";

        try {
            Call<ResponseDto<AlbumDto>> call = albumApi.getAlbumById(bearerToken, albumId);
            call.enqueue(new Callback<ResponseDto<AlbumDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<AlbumDto>> call,
                                       @NonNull Response<ResponseDto<AlbumDto>> response) {
                    getAlbumByIdResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<AlbumDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getAlbumByIdResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<AlbumDto>> createAlbum(String bearerToken, AlbumCreateDto albumCreateDto) {
        MutableLiveData<ResponseDto<AlbumDto>> createAlbumResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl CreateAlbum Error";

        try {
            Call<ResponseDto<AlbumDto>> call = albumApi.createAlbum(bearerToken, albumCreateDto);
            call.enqueue(new Callback<ResponseDto<AlbumDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<AlbumDto>> call,
                                       @NonNull Response<ResponseDto<AlbumDto>> response) {
                    createAlbumResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<AlbumDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }


        return createAlbumResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<AlbumDto>> updateAlbumById(String bearerToken, Long albumId, AlbumCreateDto albumCreateDto) {
        MutableLiveData<ResponseDto<AlbumDto>> updateAlbumResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl UpdateAlbum Error";

        try {
            Call<ResponseDto<AlbumDto>> call = albumApi.updateAlbum(bearerToken, albumId, albumCreateDto);
            call.enqueue(new Callback<ResponseDto<AlbumDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<AlbumDto>> call,
                                       @NonNull Response<ResponseDto<AlbumDto>> response) {
                    updateAlbumResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<AlbumDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return updateAlbumResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<AlbumDto>> deleteAllAlbums(String bearerToken) {
        MutableLiveData<ResponseDto<AlbumDto>> deleteAllAlbumsResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl DeleteAllAlbums Error";

        try {
            Call<ResponseDto<AlbumDto>> call = albumApi.deleteAllAlbums(bearerToken);
            call.enqueue(new Callback<ResponseDto<AlbumDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<AlbumDto>> call,
                                       @NonNull Response<ResponseDto<AlbumDto>> response) {
                    deleteAllAlbumsResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<AlbumDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");

                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return deleteAllAlbumsResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<AlbumDto>> deleteAlbumById(String bearerToken, Long albumId) {
        MutableLiveData<ResponseDto<AlbumDto>> deleteAlbumResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl DeleteAlbum Error";

        try {
            Call<ResponseDto<AlbumDto>> call = albumApi.deleteAlbum(bearerToken, albumId);
            call.enqueue(new Callback<ResponseDto<AlbumDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<AlbumDto>> call,
                                       @NonNull Response<ResponseDto<AlbumDto>> response) {
                    deleteAlbumResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<AlbumDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return deleteAlbumResponseLiveData;
    }
}
