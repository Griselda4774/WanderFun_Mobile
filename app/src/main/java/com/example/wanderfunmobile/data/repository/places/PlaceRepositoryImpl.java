package com.example.wanderfunmobile.data.repository.places;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;
import com.example.wanderfunmobile.data.api.backend.places.PlaceApi;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.repository.places.PlaceRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepositoryImpl implements PlaceRepository {
    private final PlaceApi placeApi;
    private final ObjectMapper objectMapper;

    @Inject
    public PlaceRepositoryImpl(PlaceApi placeApi, ObjectMapper objectMapper) {
        this.placeApi = placeApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public LiveData<Result<List<Place>>> findAllPlaces() {
        MutableLiveData<Result<List<Place>>> findAllPlacesResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl FindAllPlaces Error";
        try {
            Call<ResponseDto<List<PlaceDto>>> call = placeApi.findAllPlaces();
            call.enqueue(new Callback<ResponseDto<List<PlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<PlaceDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Place>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Place.class));
                        }
                        findAllPlacesResponseLiveData.postValue(result);
                    }
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

        return findAllPlacesResponseLiveData;
    }

    @Override
    public LiveData<Result<List<Place>>> findAllPlacesByProvinceName(String provinceName) {
        MutableLiveData<Result<List<Place>>> findAllPlacesByProvinceNameResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl FindAllPlacesByProvinceName Error";
        try {
            Call<ResponseDto<List<PlaceDto>>> call = placeApi.findAllPlacesByProvinceName(provinceName);
            call.enqueue(new Callback<ResponseDto<List<PlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<PlaceDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Place>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Place.class));
                        }
                        findAllPlacesByProvinceNameResponseLiveData.postValue(result);
                    }
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

        return findAllPlacesByProvinceNameResponseLiveData;
    }

    @Override
    public LiveData<Result<List<Place>>> findAllPlacesByNameContaining(String name) {
        MutableLiveData<Result<List<Place>>> findAllPlacesByNameContainingResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl FindAllPlacesByNameContaining Error";
        try {
            Call<ResponseDto<List<PlaceDto>>> call = placeApi.findAllPlacesByNameContaining(name);
            call.enqueue(new Callback<ResponseDto<List<PlaceDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PlaceDto>>> call,
                                       @NonNull Response<ResponseDto<List<PlaceDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Place>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Place.class));
                        }
                        findAllPlacesByNameContainingResponseLiveData.postValue(result);
                    }
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

        return findAllPlacesByNameContainingResponseLiveData;
    }

    @Override
    public LiveData<Result<Place>> findPlaceById(Long placeId) {
        MutableLiveData<Result<Place>> findPlaceByIdResponseLiveData = new MutableLiveData<>();
        String errorType = "PlaceRepositoryImpl FindPlaceById Error";

        try {
            Call<ResponseDto<PlaceDto>> call = placeApi.findPlaceById(placeId);
            call.enqueue(new Callback<ResponseDto<PlaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<PlaceDto>> call,
                                       @NonNull Response<ResponseDto<PlaceDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Place> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Place.class));
                        }
                        findPlaceByIdResponseLiveData.postValue(result);
                    }
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

        return findPlaceByIdResponseLiveData;
    }

//    @Override
//    public LiveData<Result<List<FavouritePlace>>> findAllFavouritePlaces(String bearerToken) {
//        MutableLiveData<Result<List<FavouritePlace>>> findAllFavouritePlacesResponseLiveData = new MutableLiveData<>();
//        String errorType = "PlaceRepositoryImpl FindAllFavouritePlaces Error";
//        try {
//            Call<ResponseDto<List<FavouritePlaceDto>>> call = placeApi.findAllFavouritePlaces(bearerToken);
//            call.enqueue(new Callback<ResponseDto<List<FavouritePlaceDto>>>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseDto<List<FavouritePlaceDto>>> call,
//                                       @NonNull Response<ResponseDto<List<FavouritePlaceDto>>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Result<List<FavouritePlace>> result = new Result<>();
//                        result.setError(response.body().isError());
//                        result.setMessage(response.body().getMessage());
//                        if (response.body().getData() != null) {
//                            result.setData(objectMapper.mapList(response.body().getData(), FavouritePlace.class));
//                        }
//                        findAllFavouritePlacesResponseLiveData.postValue(result);
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseDto<List<FavouritePlaceDto>>> call,
//                                      @NonNull Throwable throwable) {
//                    Log.e(errorType, "Error during onFailure");
//                }
//            });
//        } catch (Exception e) {
//            Log.e(errorType, "Error during catch");
//        }
//
//        return findAllFavouritePlacesResponseLiveData;
//    }
//
//    @Override
//    public LiveData<Result<FavouritePlace>> addFavouritePlace(String bearerToken, Long placeId) {
//        MutableLiveData<Result<FavouritePlace>> addFavouritePlaceResponseLiveData = new MutableLiveData<>();
//        String errorType = "PlaceRepositoryImpl addFavouritePlace Error";
//        try {
//            Call<ResponseDto<FavouritePlaceDto>> call = placeApi.addFavouritePlace(bearerToken, placeId);
//            call.enqueue(new Callback<ResponseDto<FavouritePlaceDto>>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseDto<FavouritePlaceDto>> call,
//                                       @NonNull Response<ResponseDto<FavouritePlaceDto>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Result<FavouritePlace> result = new Result<>();
//                        result.setError(response.body().isError());
//                        result.setMessage(response.body().getMessage());
//                        if (response.body().getData() != null) {
//                            result.setData(objectMapper.map(response.body().getData(), FavouritePlace.class));
//                        }
//                        addFavouritePlaceResponseLiveData.postValue(result);
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseDto<FavouritePlaceDto>> call,
//                                      @NonNull Throwable throwable) {
//                    Log.e(errorType, "Error during onFailure");
//                }
//            });
//        } catch (Exception e) {
//            Log.e(errorType, "Error during catch");
//        }
//
//        return addFavouritePlaceResponseLiveData;
//    }
//
//    @Override
//    public LiveData<Result<FavouritePlace>> deleteFavouritePlaceByIds(String bearerToken, List<Long> placeIds) {
//        MutableLiveData<Result<FavouritePlace>> deleteFavouritePlaceByIdsResponseLiveData = new MutableLiveData<>();
//        String errorType = "PlaceRepositoryImpl CreateFeedback Error";
//        try {
//            Call<ResponseDto<FavouritePlaceDto>> call = placeApi.deleteFavouritePlaceByIds(bearerToken, placeIds);
//            call.enqueue(new Callback<ResponseDto<FavouritePlaceDto>>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseDto<FavouritePlaceDto>> call,
//                                       @NonNull Response<ResponseDto<FavouritePlaceDto>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Result<FavouritePlace> result = new Result<>();
//                        result.setError(response.body().isError());
//                        result.setMessage(response.body().getMessage());
//                        if (response.body().getData() != null) {
//                            result.setData(objectMapper.map(response.body().getData(), FavouritePlace.class));
//                        }
//                        deleteFavouritePlaceByIdsResponseLiveData.postValue(result);
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseDto<FavouritePlaceDto>> call,
//                                      @NonNull Throwable throwable) {
//                    Log.e(errorType, "Error during onFailure");
//                }
//            });
//        } catch (Exception e) {
//            Log.e(errorType, "Error during catch");
//        }
//
//        return deleteFavouritePlaceByIdsResponseLiveData;
//    }
//
//    @Override
//    public LiveData<Result<CheckIn>> checkInPlace(String bearerToken, Long placeId) {
//        MutableLiveData<Result<CheckIn>> checkInPlaceResponseLiveData = new MutableLiveData<>();
//        String errorType = "PlaceRepositoryImpl CheckInPlace Error";
//        try {
//            Call<ResponseDto<CheckInDto>> call = placeApi.checkInPlace(bearerToken, placeId);
//            call.enqueue(new Callback<ResponseDto<CheckInDto>>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseDto<CheckInDto>> call,
//                                       @NonNull Response<ResponseDto<CheckInDto>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Result<CheckIn> result = new Result<>();
//                        result.setError(response.body().isError());
//                        result.setMessage(response.body().getMessage());
//                        if (response.body().getData() != null) {
//                            result.setData(objectMapper.map(response.body().getData(), CheckIn.class));
//                        }
//                        checkInPlaceResponseLiveData.postValue(result);
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseDto<CheckInDto>> call,
//                                      @NonNull Throwable throwable) {
//                    Log.e(errorType, "Error during onFailure");
//                }
//            });
//        } catch (Exception e) {
//            Log.e(errorType, "Error during catch");
//        }
//
//        return checkInPlaceResponseLiveData;
//    }
//
//    @Override
//    public LiveData<Result<CheckIn>> findCheckInByPlaceId(String bearerToken, Long placeId) {
//        MutableLiveData<Result<CheckIn>> findCheckInByPlaceIdResponseLiveData = new MutableLiveData<>();
//        String errorType = "PlaceRepositoryImpl findCheckInByPlaceId Error";
//        try {
//            Call<ResponseDto<CheckInDto>> call = placeApi.findCheckInByPlaceId(bearerToken, placeId);
//            call.enqueue(new Callback<ResponseDto<CheckInDto>>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseDto<CheckInDto>> call,
//                                       @NonNull Response<ResponseDto<CheckInDto>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Result<CheckIn> result = new Result<>();
//                        result.setError(response.body().isError());
//                        result.setMessage(response.body().getMessage());
//                        if (response.body().getData() != null) {
//                            result.setData(objectMapper.map(response.body().getData(), CheckIn.class));
//                        }
//                        findCheckInByPlaceIdResponseLiveData.postValue(result);
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseDto<CheckInDto>> call,
//                                      @NonNull Throwable throwable) {
//                    Log.e(errorType, "Error during onFailure");
//                }
//            });
//        } catch (Exception e) {
//            Log.e(errorType, "Error during catch");
//        }
//
//        return findCheckInByPlaceIdResponseLiveData;
//    }
}
