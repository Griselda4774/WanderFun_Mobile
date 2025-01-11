package com.example.wanderfunmobile.infrastructure.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.application.dto.ResponseDto;
import com.example.wanderfunmobile.application.dto.trip.TripCreateDto;
import com.example.wanderfunmobile.application.dto.trip.TripDto;
import com.example.wanderfunmobile.application.repository.TripRepository;
import com.example.wanderfunmobile.infrastructure.api.backend.TripApi;

import java.util.List;

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
    public LiveData<ResponseDto<List<TripDto>>> getAllTrips(String bearerToken) {
        MutableLiveData<ResponseDto<List<TripDto>>> getAllTripsResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl GetAllTrips Error";

        try {
            Call<ResponseDto<List<TripDto>>> call = tripApi.getAllTrips(bearerToken);
            call.enqueue(new Callback<ResponseDto<List<TripDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<TripDto>>> call,
                                       @NonNull Response<ResponseDto<List<TripDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getAllTripsResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<TripDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getAllTripsResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<TripDto>> getTripById(String bearerToken, Long tripId) {
        MutableLiveData<ResponseDto<TripDto>> getTripByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl GetTripById Error";

        try {
            Call<ResponseDto<TripDto>> call = tripApi.getTripById(bearerToken, tripId);
            call.enqueue(new Callback<ResponseDto<TripDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<TripDto>> call,
                                       @NonNull Response<ResponseDto<TripDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getTripByIdResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<TripDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getTripByIdResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<TripDto>> createTrip(String bearerToken, TripCreateDto tripCreateDto) {
        MutableLiveData<ResponseDto<TripDto>> createTripResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl CreateTrip Error";

        try {
            Call<ResponseDto<TripDto>> call = tripApi.createTrip(bearerToken, tripCreateDto);
            call.enqueue(new Callback<ResponseDto<TripDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<TripDto>> call,
                                       @NonNull Response<ResponseDto<TripDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        createTripResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<TripDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return createTripResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<TripDto>> updateTripById(String bearerToken, Long tripId, TripCreateDto tripCreateDto) {
        MutableLiveData<ResponseDto<TripDto>> updateTripByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl UpdateTripById Error";

        try {
            Call<ResponseDto<TripDto>> call = tripApi.updateTrip(bearerToken, tripId, tripCreateDto);
            call.enqueue(new Callback<ResponseDto<TripDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<TripDto>> call,
                                       @NonNull Response<ResponseDto<TripDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateTripByIdResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<TripDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return updateTripByIdResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<TripDto>> deleteAllTrips(String bearerToken) {
        MutableLiveData<ResponseDto<TripDto>> deleteAllTripsResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl DeleteAllTrips Error";

        try {
            Call<ResponseDto<TripDto>> call = tripApi.deleteAllTrips(bearerToken);
            call.enqueue(new Callback<ResponseDto<TripDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<TripDto>> call,
                                       @NonNull Response<ResponseDto<TripDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        deleteAllTripsResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<TripDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return deleteAllTripsResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<TripDto>> deleteTripById(String bearerToken, Long tripId) {
        MutableLiveData<ResponseDto<TripDto>> deleteTripByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "TripRepositoryImpl DeleteTripById Error";

        try {
            Call<ResponseDto<TripDto>> call = tripApi.deleteTrip(bearerToken, tripId);
            call.enqueue(new Callback<ResponseDto<TripDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<TripDto>> call,
                                       @NonNull Response<ResponseDto<TripDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        deleteTripByIdResponseLiveData.postValue(response.body());
                    } else {
                        Log.e(errorType, "Error during onResponse");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<TripDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return deleteTripByIdResponseLiveData;
    }
}
