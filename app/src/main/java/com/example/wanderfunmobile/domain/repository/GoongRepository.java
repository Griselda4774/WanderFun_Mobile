package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.data.dto.goong.GoongTripRequestDto;
import com.example.wanderfunmobile.data.dto.goong.GoongTripRespondDto;
import com.example.wanderfunmobile.domain.model.Result;

public interface GoongRepository {
    LiveData<Result<GoongTripRespondDto>> getGoongTrip(String bearerToken, GoongTripRequestDto goongTripRequestDto, String apiKey);
}
