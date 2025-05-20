package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.checkin.CheckInDto;
import com.example.wanderfunmobile.data.dto.favouriteplace.FavouritePlaceDto;
import com.example.wanderfunmobile.data.dto.feedback.FeedbackCreateDto;
import com.example.wanderfunmobile.data.dto.feedback.FeedbackDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;
import com.example.wanderfunmobile.data.api.backend.PlaceApi;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.repository.PlaceRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepositoryImpl implements PlaceRepository {
    private final PlaceApi placeApi;

    @Inject
    public PlaceRepositoryImpl(PlaceApi placeApi) {
        this.placeApi = placeApi;
    }

    @Override
    public LiveData<ResponseDto<List<PlaceDto>>> getAllPlaces() {
        MutableLiveData<ResponseDto<List<PlaceDto>>> getAllPlacesResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl GetAllPlaces Error";
        try {
            Call<ResponseDto<List<PlaceDto>>> call = placeApi.getAllPlaces();
            call.enqueue(new Callback<ResponseDto<List<PlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<PlaceDto>>> response) {
                    getAllPlacesResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getAllPlacesResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<List<PlaceDto>>> getAllPlacesByProvinceName(String provinceName) {
        MutableLiveData<ResponseDto<List<PlaceDto>>> getAllPlacesByProvinceNameResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl GetAllPlacesByProvinceName Error";
        try {
            Call<ResponseDto<List<PlaceDto>>> call = placeApi.getAllPlacesByProvinceName(provinceName);
            call.enqueue(new Callback<ResponseDto<List<PlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<PlaceDto>>> response) {
                    getAllPlacesByProvinceNameResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch: " + e);
        }

        return getAllPlacesByProvinceNameResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<List<PlaceDto>>> searchPlacesByNameContaining(String name) {
        MutableLiveData<ResponseDto<List<PlaceDto>>> searchPlacesByNameContainingResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl searchPlacesByNameContaining Error";
        try {
            Call<ResponseDto<List<PlaceDto>>> call = placeApi.searchPlacesByNameContaining(name);
            call.enqueue(new Callback<ResponseDto<List<PlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<PlaceDto>>> response) {
                    searchPlacesByNameContainingResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });

        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return searchPlacesByNameContainingResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<PlaceDto>> getPlaceById(Long placeId) {
        MutableLiveData<ResponseDto<PlaceDto>> getPlaceByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl GetPlaceById Error";

        try {
            Call<ResponseDto<PlaceDto>> call = placeApi.getPlaceById(placeId);
            call.enqueue(new Callback<ResponseDto<PlaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<PlaceDto>> call,
                                       @NonNull Response<ResponseDto<PlaceDto>> response) {
                    getPlaceByIdResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<PlaceDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getPlaceByIdResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<FeedbackDto>> createFeedback(String bearerToken, FeedbackCreateDto feedbackCreateDto, Long placeId) {
        MutableLiveData<ResponseDto<FeedbackDto>> createFeedbackResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl CreateFeedback Error";
        try {
            Call<ResponseDto<FeedbackDto>> call = placeApi.createFeedback(bearerToken, feedbackCreateDto, placeId);
            call.enqueue(new Callback<ResponseDto<FeedbackDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<FeedbackDto>> call,
                                       @NonNull Response<ResponseDto<FeedbackDto>> response) {
                    createFeedbackResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<FeedbackDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return createFeedbackResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<List<FavouritePlaceDto>>> getAllFavouritePlaces(String bearerToken) {
        MutableLiveData<ResponseDto<List<FavouritePlaceDto>>> getAllFavouritePlacesResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl getAllFavouritePlaces Error";
        try {
            Call<ResponseDto<List<FavouritePlaceDto>>> call = placeApi.getAllFavouritePlaces(bearerToken);
            call.enqueue(new Callback<ResponseDto<List<FavouritePlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<FavouritePlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<FavouritePlaceDto>>> response) {
                    getAllFavouritePlacesResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<FavouritePlaceDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getAllFavouritePlacesResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<FavouritePlaceDto>> addFavouritePlace(String bearerToken, Long placeId) {
        MutableLiveData<ResponseDto<FavouritePlaceDto>> addFavouritePlaceResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl addFavouritePlace Error";
        try {
            Call<ResponseDto<FavouritePlaceDto>> call = placeApi.addFavouritePlace(bearerToken, placeId);
            call.enqueue(new Callback<ResponseDto<FavouritePlaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<FavouritePlaceDto>> call,
                                       @NonNull Response<ResponseDto<FavouritePlaceDto>> response) {
                    addFavouritePlaceResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<FavouritePlaceDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return addFavouritePlaceResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<FavouritePlaceDto>> deleteFavouritePlaceByIds(String bearerToken, List<Long> placeIds) {
        MutableLiveData<ResponseDto<FavouritePlaceDto>> deleteFavouritePlaceByIdsResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl CreateFeedback Error";
        try {
            Call<ResponseDto<FavouritePlaceDto>> call = placeApi.deleteFavouritePlaceByIds(bearerToken, placeIds);
            call.enqueue(new Callback<ResponseDto<FavouritePlaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<FavouritePlaceDto>> call,
                                       @NonNull Response<ResponseDto<FavouritePlaceDto>> response) {
                    deleteFavouritePlaceByIdsResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<FavouritePlaceDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return deleteFavouritePlaceByIdsResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<CheckInDto>> checkInPlace(String bearerToken, Long placeId) {
        MutableLiveData<ResponseDto<CheckInDto>> checkInPlaceResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl CheckInPlace Error";
        try {
            Call<ResponseDto<CheckInDto>> call = placeApi.checkInPlace(bearerToken, placeId);
            call.enqueue(new Callback<ResponseDto<CheckInDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<CheckInDto>> call,
                                       @NonNull Response<ResponseDto<CheckInDto>> response) {
                    checkInPlaceResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<CheckInDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return checkInPlaceResponseLiveData;
    }

    @Override
    public LiveData<ResponseDto<CheckInDto>> getCheckInByPlaceIdAndUserId(String bearerToken, Long placeId) {
        MutableLiveData<ResponseDto<CheckInDto>> getCheckInByPlaceIdAndUserIdResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl getCheckInByPlaceIdAndUserId Error";
        try {
            Call<ResponseDto<CheckInDto>> call = placeApi.getCheckInByPlaceIdAndUserId(bearerToken, placeId);
            call.enqueue(new Callback<ResponseDto<CheckInDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<CheckInDto>> call,
                                       @NonNull Response<ResponseDto<CheckInDto>> response) {
                    getCheckInByPlaceIdAndUserIdResponseLiveData.postValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<CheckInDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch");
        }

        return getCheckInByPlaceIdAndUserIdResponseLiveData;
    }
}
