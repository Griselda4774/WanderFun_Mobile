package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.api.backend.GoongApi;
import com.example.wanderfunmobile.data.dto.goong.direction.GoongDirectionRequestDto;
import com.example.wanderfunmobile.data.dto.goong.direction.GoongDirectionRespondDto;
import com.example.wanderfunmobile.data.dto.goong.trip.GoongTripRequestDto;
import com.example.wanderfunmobile.data.dto.goong.trip.GoongTripRespondDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.repository.GoongRepository;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoongRepositoryImpl implements GoongRepository {
    private GoongApi goongApi;

    private ObjectMapper objectMapper;

    @Inject
    public GoongRepositoryImpl(GoongApi goongApi, ObjectMapper objectMapper) {
        this.goongApi = goongApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public LiveData<Result<GoongTripRespondDto>> getGoongTrip(String bearerToken, GoongTripRequestDto goongTripRequestDto, String apiKey) {
        MutableLiveData<Result<GoongTripRespondDto>> getGoongTripLiveData = new MutableLiveData<>();
        String errorType = "GoongRepositoryImpl GetGoongTrip Error";
        try{
            Call<GoongTripRespondDto> call = goongApi.getGoongTrip(
                    bearerToken,
                    apiKey,
                    goongTripRequestDto.getOrigin(),
                    goongTripRequestDto.getDestination(),
                    (goongTripRequestDto.getWaypoints() != null && !goongTripRequestDto.getWaypoints().isEmpty()
                            ? String.join(";", goongTripRequestDto.getWaypoints())
                            : null),
                    goongTripRequestDto.getVehicle()
            );
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<GoongTripRespondDto> call,
                                       @NonNull Response<GoongTripRespondDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<GoongTripRespondDto> result = new Result<>();
                        result.setData(objectMapper.map(response.body(), GoongTripRespondDto.class));
                        result.setError(false);

                        getGoongTripLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GoongTripRespondDto> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch: " + e);
        }

        return getGoongTripLiveData;
    }

    @Override
    public LiveData<Result<GoongDirectionRespondDto>> getGoongDirection(String bearerToken, GoongDirectionRequestDto goongDirectionRequestDto, String apiKey) {
        MutableLiveData<Result<GoongDirectionRespondDto>> getGoongDirectionLiveData = new MutableLiveData<>();
        String errorType = "GoongRepositoryImpl GetGoongDirection Error";
        try {
            Call<GoongDirectionRespondDto> call = goongApi.getGoongDirection(
                    bearerToken,
                    apiKey,
                    goongDirectionRequestDto.getOrigin(),
                    (goongDirectionRequestDto.getDestination() != null && !goongDirectionRequestDto.getDestination().isEmpty()
                            ? String.join(";", goongDirectionRequestDto.getDestination())
                            : null),
                    goongDirectionRequestDto.getVehicle()
            );
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<GoongDirectionRespondDto> call,
                                       @NonNull Response<GoongDirectionRespondDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<GoongDirectionRespondDto> result = new Result<>();
                        result.setData(objectMapper.map(response.body(), GoongDirectionRespondDto.class));
                        result.setError(false);

                        getGoongDirectionLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GoongDirectionRespondDto> call,
                                      @NonNull Throwable throwable) {
                    Log.e(errorType, "Error during onFailure");
                }
            });
        } catch (Exception e) {
            Log.e(errorType, "Error during catch: " + e);
        }

        return getGoongDirectionLiveData;
    }
}
