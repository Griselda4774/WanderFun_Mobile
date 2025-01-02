package com.example.wanderfunmobile.infrastructure.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.trip.TripCreateDto;
import com.example.wanderfunmobile.application.repository.TripRepository;
import com.example.wanderfunmobile.infrastructure.util.ErrorGenerateUtil;
import com.example.wanderfunmobile.network.backend.TripApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripRepositoryImpl implements TripRepository {
    private final TripApi tripApi;

    @Inject
    public TripRepositoryImpl(TripApi tripApi) {
        this.tripApi = tripApi;
    }

    @Override
    public LiveData<ResponseDto<Object>> getAllTrips(String bearerToken) {
        MutableLiveData<ResponseDto<Object>> getAllTripsResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl GetAllTrips Error";

        try {
            Call<ResponseDto<Object>> call = tripApi.getAllTrips(bearerToken);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getAllTripsResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        getAllTripsResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    getAllTripsResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            getAllTripsResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return getAllTripsResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> getTripById(String bearerToken, Long tripId) {
        MutableLiveData<ResponseDto<Object>> getTripByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl GetTripById Error";

        try {
            Call<ResponseDto<Object>> call = tripApi.getTripById(bearerToken, tripId);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getTripByIdResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        getTripByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    getTripByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            getTripByIdResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return getTripByIdResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> createTrip(String bearerToken, TripCreateDto tripCreateDto) {
        MutableLiveData<ResponseDto<Object>> createTripResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl CreateTrip Error";

        try {
            Call<ResponseDto<Object>> call = tripApi.createTrip(bearerToken, tripCreateDto);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        createTripResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        createTripResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    createTripResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            createTripResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return createTripResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> updateTripById(String bearerToken, Long tripId, TripCreateDto tripCreateDto) {
        MutableLiveData<ResponseDto<Object>> updateTripByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl UpdateTripById Error";

        try {
            Call<ResponseDto<Object>> call = tripApi.updateTrip(bearerToken, tripId, tripCreateDto);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateTripByIdResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        updateTripByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    updateTripByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            updateTripByIdResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return updateTripByIdResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> deleteAllTrips(String bearerToken) {
        MutableLiveData<ResponseDto<Object>> deleteAllTripsResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl DeleteAllTrips Error";

        try {
            Call<ResponseDto<Object>> call = tripApi.deleteAllTrips(bearerToken);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        deleteAllTripsResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        deleteAllTripsResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    deleteAllTripsResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            deleteAllTripsResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return deleteAllTripsResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<Object>> deleteTripById(String bearerToken, Long tripId) {
        MutableLiveData<ResponseDto<Object>> deleteTripByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl DeleteTripById Error";

        try {
            Call<ResponseDto<Object>> call = tripApi.deleteTrip(bearerToken, tripId);
            call.enqueue(new Callback<ResponseDto<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Object>> call,
                                       @NonNull Response<ResponseDto<Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        deleteTripByIdResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                        deleteTripByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<Object>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                    deleteTripByIdResponseLiveData.postValue(ErrorGenerateUtil.createOnResponseError(errorType));
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
            deleteTripByIdResponseLiveData.postValue(ErrorGenerateUtil.createCatchError(errorType));
        }

        return deleteTripByIdResponseLiveData;
    }
}
