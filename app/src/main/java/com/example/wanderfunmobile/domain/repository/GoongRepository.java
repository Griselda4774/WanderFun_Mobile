package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.goong.direction.GoongDirectionRequestDto;
import com.example.wanderfunmobile.data.dto.goong.direction.GoongDirectionRespondDto;
import com.example.wanderfunmobile.data.dto.goong.trip.GoongTripRequestDto;
import com.example.wanderfunmobile.data.dto.goong.trip.GoongTripRespondDto;
import com.example.wanderfunmobile.domain.model.Result;

public interface GoongRepository {
    LiveData<Result<GoongTripRespondDto>> getGoongTrip(String bearerToken, GoongTripRequestDto goongTripRequestDto, String apiKey);

    LiveData <Result<GoongDirectionRespondDto>> getGoongDirection(String bearerToken, GoongDirectionRequestDto goongDirectionRequestDto, String apiKey);
}
