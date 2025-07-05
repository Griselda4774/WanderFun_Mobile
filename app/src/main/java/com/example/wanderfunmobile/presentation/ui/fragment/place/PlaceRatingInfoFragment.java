package com.example.wanderfunmobile.presentation.ui.fragment.place;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.databinding.FragmentPlaceRatingInfoBinding;
import com.example.wanderfunmobile.domain.model.places.Feedback;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.activity.place.FeedbackCreateActivity;
import com.example.wanderfunmobile.presentation.ui.adapter.place.FeedbackItemAdapter;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.ui.adapter.place.RatingBarItemAdapter;
import com.example.wanderfunmobile.presentation.ui.custom.starrating.StarRatingOutlineView;
import com.example.wanderfunmobile.presentation.ui.custom.starrating.StarRatingView;
import com.example.wanderfunmobile.presentation.viewmodel.places.FeedbackViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.places.PlaceViewModel;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlaceRatingInfoFragment extends Fragment {

    private FragmentPlaceRatingInfoBinding viewBinding;
    private FeedbackItemAdapter feedbackItemAdapter;
    private Place place;
    private final List<Feedback> feedbackList = new ArrayList<>();
    private final List<Float> ratingList = new ArrayList<>(List.of(0f, 0f, 0f, 0f, 0f));
    private FeedbackViewModel feedbackViewModel;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    Gson gson;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    public PlaceRatingInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            place = gson.fromJson(getArguments().getString("place_json"), Place.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewBinding = FragmentPlaceRatingInfoBinding.inflate(inflater, container, false);
        initViewModel();
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpActivityResultLauncher();

        observeData();

        setUpView();

        fetchFeedbackData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }

    private void setUpView() {
        RecyclerView recyclerView = viewBinding.placeRatingList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        feedbackItemAdapter = new FeedbackItemAdapter(feedbackList);
        recyclerView.setAdapter(feedbackItemAdapter);

        // Star rating outline
        StarRatingOutlineView starRatingOutlineView = viewBinding.starRatingOutlineView;
        starRatingOutlineView.setRating(0);
        starRatingOutlineView.setPlaceId(place.getId());
        starRatingOutlineView.setPlaceName(place.getName());
        starRatingOutlineView.enableIntent();
        starRatingOutlineView.setOnStarClickListener((rating, placeId, placeName) -> {
            Intent intent = new Intent(getContext(), FeedbackCreateActivity.class);
            intent.putExtra("place_id", placeId);
            intent.putExtra("place_name", placeName);
            intent.putExtra("rating", rating);
            activityResultLauncher.launch(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void bindRatingData(List<Feedback> feedbackList) {
        int totalRating = 0;
        if (!feedbackList.isEmpty()) {
            float oneStar = 0, twoStar = 0, threeStar = 0, fourStar = 0, fiveStar = 0;
            for (Feedback feedback : feedbackList) {
                totalRating += feedback.getRating();
                switch (feedback.getRating()) {
                    case 1:
                        oneStar++;
                        break;
                    case 2:
                        twoStar++;
                        break;
                    case 3:
                        threeStar++;
                        break;
                    case 4:
                        fourStar++;
                        break;
                    case 5:
                        fiveStar++;
                        break;
                }
            }
            int feedbackCount = feedbackList.size();
            ratingList.set(0, fiveStar / feedbackCount);
            ratingList.set(1, fourStar / feedbackCount);
            ratingList.set(2, threeStar / feedbackCount);
            ratingList.set(3, twoStar / feedbackCount);
            ratingList.set(4, oneStar / feedbackCount);
        }

        // Average rating
        TextView placeAverageRating = viewBinding.placeAverageRating;
        if (!feedbackList.isEmpty()) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedRating = decimalFormat.format((float) totalRating / feedbackList.size());
            placeAverageRating.setText(formattedRating);

        }
        else {
            placeAverageRating.setText("0.00");
        }

        // Star rating view
        StarRatingView ratingView = viewBinding.starRatingView;
        if (!feedbackList.isEmpty()) {
            int roundedRating = (int) Math.floor((float) totalRating / feedbackList.size());
            ratingView.setRating(roundedRating);
        } else {
            ratingView.setRating(0);
        }

        // Rating count
        TextView ratingCount = viewBinding.placeRatingCount;
        ratingCount.setText("(" + feedbackList.size() + ")");

        // Rating bar
        RecyclerView recyclerView = viewBinding.raringBarList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RatingBarItemAdapter ratingBarItemAdapter = new RatingBarItemAdapter(ratingList);
        recyclerView.setAdapter(ratingBarItemAdapter);
    }

    private void setUpActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null ) {
                            switch (Objects.requireNonNull(data.getStringExtra("status")))
                            {
                                case "feedback_created":
                                    Feedback newFeedback = gson.fromJson(Objects.requireNonNull(data.getStringExtra("feedback_json")), Feedback.class);
                                    feedbackList.add(0, newFeedback);
                                    feedbackItemAdapter.notifyItemInserted(0);
                                    viewBinding.placeRatingList.post(() -> {
                                        viewBinding.placeRatingList.invalidate();
                                        viewBinding.placeRatingList.requestLayout();
                                    });
                                    viewBinding.noFeedbackText.setVisibility(View.GONE);
                                    bindRatingData(feedbackList);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    private void initViewModel() {
        feedbackViewModel = new ViewModelProvider(requireActivity()).get(FeedbackViewModel.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeData() {
        feedbackViewModel.getFindAllFeedbacksByPlaceIdLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!result.isError() && result.getData() != null) {
                feedbackList.clear();
                feedbackList.addAll(result.getData());

                if (!feedbackList.isEmpty()) {
                    viewBinding.noFeedbackText.setVisibility(View.GONE);
                } else {
                    viewBinding.noFeedbackText.setVisibility(View.VISIBLE);
                }

                bindRatingData(result.getData());
                feedbackItemAdapter.notifyDataSetChanged();
            }
        });
    }

    private void fetchFeedbackData() {
        if (place != null) {
            feedbackViewModel.findAllFeedbacksByPlaceId(place.getId());
        }
    }
}