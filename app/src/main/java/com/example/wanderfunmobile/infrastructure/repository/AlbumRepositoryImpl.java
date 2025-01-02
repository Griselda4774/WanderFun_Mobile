package com.example.wanderfunmobile.infrastructure.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.album.AlbumCreateDto;
import com.example.wanderfunmobile.application.repository.AlbumRepository;
import com.example.wanderfunmobile.infrastructure.util.ErrorGenerateUtil;
import com.example.wanderfunmobile.network.backend.AlbumApi;

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
    public LiveData<ResponseDto<Object>> getAllAlbums(String bearerToken) {
        MutableLiveData<ResponseDto<Object>> getAllAlbumsResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl GetAllAlbums Error";
        try {
            Call<ResponseDto<Object>> call = albumApi.getAllAlbums(bearerToken);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getAllAlbumsResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        getAllAlbumsResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(Call<ResponseDto<Object>> call, Throwable throwable) {

                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            getAllAlbumsResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return getAllAlbumsResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> getAlbumById(String bearerToken, Long albumId) {
        MutableLiveData<ResponseDto<Object>> getAlbumByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl GetAlbumById Error";

        try {
            Call<ResponseDto<Object>> call = albumApi.getAlbumById(bearerToken, albumId);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getAlbumByIdResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        getAlbumByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    getAlbumByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            getAlbumByIdResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return getAlbumByIdResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> createAlbum(String bearerToken, AlbumCreateDto albumCreateDto) {
        MutableLiveData<ResponseDto<Object>> createAlbumResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl CreateAlbum Error";

        try {
            Call<ResponseDto<Object>> call = albumApi.createAlbum(bearerToken, albumCreateDto);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        createAlbumResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        createAlbumResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    createAlbumResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            createAlbumResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }


        return createAlbumResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> updateAlbumById(String bearerToken, Long albumId, AlbumCreateDto albumCreateDto) {
        MutableLiveData<ResponseDto<Object>> updateAlbumResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl UpdateAlbum Error";

        try {
            Call<ResponseDto<Object>> call = albumApi.updateAlbum(bearerToken, albumId, albumCreateDto);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateAlbumResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        updateAlbumResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    updateAlbumResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            updateAlbumResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return updateAlbumResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> deleteAllAlbums(String bearerToken) {
        MutableLiveData<ResponseDto<Object>> deleteAllAlbumsResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl DeleteAllAlbums Error";

        try {
            Call<ResponseDto<Object>> call = albumApi.deleteAllAlbums(bearerToken);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        deleteAllAlbumsResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        deleteAllAlbumsResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    deleteAllAlbumsResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));

                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            deleteAllAlbumsResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return deleteAllAlbumsResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> deleteAlbumById(String bearerToken, Long albumId) {
        MutableLiveData<ResponseDto<Object>> deleteAlbumResponseLiveData = new MutableLiveData<>();
        String errorType = "AlbumRepositoryImpl DeleteAlbum Error";

        try {
            Call<ResponseDto<Object>> call = albumApi.deleteAlbum(bearerToken, albumId);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        deleteAlbumResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        deleteAlbumResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    deleteAlbumResponseLiveData.postValue(ErrorGenerateUtil.createOnFailureError(errorType));
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            deleteAlbumResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return deleteAlbumResponseLiveData;
    }
}
