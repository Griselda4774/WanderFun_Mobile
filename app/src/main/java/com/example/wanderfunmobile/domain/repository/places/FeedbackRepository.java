package com.example.wanderfunmobile.domain.repository.places;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.places.Feedback;

import java.util.List;

public interface FeedbackRepository {
    LiveData<Result<List<Feedback>>> findAllByPlaceId(String bearerToken, Long placeId);
    LiveData<Result<Feedback>> create(String bearerToken, Long placeId, Feedback feedback);
    LiveData<Result<Feedback>> updateById(String bearerToken, Long feedbackId, Feedback feedback);
    LiveData<Result<Feedback>> deleteById(String bearerToken, Long feedbackId, String localId);
}
