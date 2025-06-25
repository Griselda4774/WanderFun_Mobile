package com.example.wanderfunmobile.presentation.viewmodel.places;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.places.Feedback;
import com.example.wanderfunmobile.domain.repository.places.FeedbackRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FeedbackViewModel extends ViewModel {
    private final FeedbackRepository feedbackRepository;

    @Inject
    public FeedbackViewModel(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    private final MutableLiveData<Result<List<Feedback>>> findAllFeedbacksByPlaceIdLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Feedback>> createFeedbackLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Feedback>> updateFeedbackLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Feedback>> deleteFeedbackLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Result<List<Feedback>>> getFindAllFeedbacksByPlaceIdLiveData() {
        return findAllFeedbacksByPlaceIdLiveData;
    }

    public MutableLiveData<Result<Feedback>> getCreateFeedbackLiveData() {
        return createFeedbackLiveData;
    }

    public MutableLiveData<Result<Feedback>> getUpdateFeedbackLiveData() {
        return updateFeedbackLiveData;
    }

    public MutableLiveData<Result<Feedback>> getDeleteFeedbackLiveData() {
        return deleteFeedbackLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void findAllFeedbacksByPlaceId(Long placeId) {
        isLoading.setValue(true);
        feedbackRepository.findAllByPlaceId(placeId).observeForever(result -> {
            isLoading.setValue(false);
            findAllFeedbacksByPlaceIdLiveData.setValue(result);
        });
    }

    public void createFeedback(String bearerToken, Long placeId, Feedback feedback) {
        isLoading.setValue(true);
        feedbackRepository.create(bearerToken, placeId, feedback).observeForever(result -> {
            isLoading.setValue(false);
            createFeedbackLiveData.setValue(result);
        });
    }

    public void updateFeedback(String bearerToken, Long feedbackId, Feedback feedback) {
        isLoading.setValue(true);
        feedbackRepository.updateById(bearerToken, feedbackId, feedback).observeForever(result -> {
            isLoading.setValue(false);
            updateFeedbackLiveData.setValue(result);
        });
    }

    public void deleteFeedback(String bearerToken, Long feedbackId, String localId) {
        isLoading.setValue(true);
        feedbackRepository.deleteById(bearerToken, feedbackId, localId).observeForever(result -> {
            isLoading.setValue(false);
            deleteFeedbackLiveData.setValue(result);
        });
    }
}