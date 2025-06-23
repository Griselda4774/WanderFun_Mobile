package com.example.wanderfunmobile.data.repository.places;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.api.backend.places.FeedbackApi;
import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.place.FeedbackCreateDto;
import com.example.wanderfunmobile.data.dto.place.FeedbackDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.places.Feedback;
import com.example.wanderfunmobile.domain.repository.places.FeedbackRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackRepositoryImpl implements FeedbackRepository {
    private final FeedbackApi feedbackApi;
    private final ObjectMapper objectMapper;

    @Inject
    public FeedbackRepositoryImpl(FeedbackApi feedbackApi, ObjectMapper objectMapper) {
        this.feedbackApi = feedbackApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public LiveData<Result<List<Feedback>>> findAllByPlaceId(String bearerToken, Long placeId) {
        MutableLiveData<Result<List<Feedback>>> findAllFeedbacksByPlaceIdResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<FeedbackDto>>> call = feedbackApi.findAllByPlaceId(bearerToken, placeId);
            call.enqueue(new Callback<ResponseDto<List<FeedbackDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<FeedbackDto>>> call,
                                       @NonNull Response<ResponseDto<List<FeedbackDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Feedback>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Feedback.class));
                        }
                        findAllFeedbacksByPlaceIdResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<FeedbackDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PlaceRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PlaceRepositoryImpl", "Error during findAllPlacesByCursor", e);
        }

        return findAllFeedbacksByPlaceIdResponseLiveData;
    }

    @Override
    public LiveData<Result<Feedback>> create(String bearerToken, Long placeId, Feedback feedback) {
        MutableLiveData<Result<Feedback>> createFeedbackResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<FeedbackDto>> call = feedbackApi.create(bearerToken, placeId, objectMapper.map(feedback, FeedbackCreateDto.class));
            call.enqueue(new Callback<ResponseDto<FeedbackDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<FeedbackDto>> call,
                                       @NonNull Response<ResponseDto<FeedbackDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Feedback> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Feedback.class));
                        }
                        createFeedbackResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<FeedbackDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PlaceRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PlaceRepositoryImpl", "Error during create place", e);
        }
        return createFeedbackResponseLiveData;
    }

    @Override
    public LiveData<Result<Feedback>> updateById(String bearerToken, Long feedbackId, Feedback feedback) {
        MutableLiveData<Result<Feedback>> updateFeedbackResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<FeedbackDto>> call = feedbackApi.updateById(bearerToken, feedbackId, objectMapper.map(feedback, FeedbackCreateDto.class));
            call.enqueue(new Callback<ResponseDto<FeedbackDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<FeedbackDto>> call,
                                       @NonNull Response<ResponseDto<FeedbackDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Feedback> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Feedback.class));
                        }
                        updateFeedbackResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<FeedbackDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("FeedbackRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("feedbackRepositoryImpl", "Error during update feedback", e);
        }

        return updateFeedbackResponseLiveData;
    }

    @Override
    public LiveData<Result<Feedback>> deleteById(String bearerToken, Long feedbackId, String localId) {
        MutableLiveData<Result<Feedback>> deleteFeedbackResponseLiveData = new MutableLiveData<>();

        try {
            Call<ResponseDto<FeedbackDto>> call = feedbackApi.deleteById(bearerToken, feedbackId);
            call.enqueue(new Callback<ResponseDto<FeedbackDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<FeedbackDto>> call,
                                       @NonNull Response<ResponseDto<FeedbackDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Feedback> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Feedback.class));
                        } else {
                            result.setData(new Feedback());
                        }
                        result.getData().setLocalId(localId);
                        deleteFeedbackResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<FeedbackDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("FeedbackRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("feedbackRepositoryImpl", "Error during delete feedback", e);
        }

        return deleteFeedbackResponseLiveData;
    }
}
