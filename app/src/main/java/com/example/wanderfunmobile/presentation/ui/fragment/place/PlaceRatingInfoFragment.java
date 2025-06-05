package com.example.wanderfunmobile.presentation.ui.fragment.place;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.databinding.FragmentPlaceRatingInfoBinding;
import com.example.wanderfunmobile.domain.model.Feedback;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.adapter.place.FeedbackItemAdapter;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlaceRatingInfoFragment extends Fragment {

    private FragmentPlaceRatingInfoBinding viewBinding;
    private Place place;
    private List<Feedback> feedbackList = new ArrayList<>();
    private List<Float> ratingList = new ArrayList<>(List.of(0f, 0f, 0f, 0f, 0f));
    private PlaceViewModel placeViewModel;
    @Inject
    ObjectMapper objectMapper;


    public PlaceRatingInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewBinding = FragmentPlaceRatingInfoBinding.inflate(inflater, container, false);
        assert getParentFragment() != null;
        placeViewModel = new ViewModelProvider(getParentFragment()).get(PlaceViewModel.class);
        return viewBinding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = viewBinding.placeRatingList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FeedbackItemAdapter feedbackItemAdapter = new FeedbackItemAdapter(feedbackList);
        recyclerView.setAdapter(feedbackItemAdapter);

        placeViewModel.getFindPlaceByIdResponseLiveData().observe(getViewLifecycleOwner(), data -> {
//            if (data != null && !data.isError()) {
//                place = objectMapper.map(data.getData(), Place.class);
//                feedbackList.clear();
//                feedbackList.addAll(place.getFeedbacks());
//                if (!feedbackList.isEmpty()) {
//                    float oneStar = 0, twoStar = 0, threeStar = 0, fourStar = 0, fiveStar = 0;
//                    for (Feedback feedback : place.getFeedbacks()) {
//                        switch (feedback.getRating()) {
//                            case 1:
//                                oneStar++;
//                                break;
//                            case 2:
//                                twoStar++;
//                                break;
//                            case 3:
//                                threeStar++;
//                                break;
//                            case 4:
//                                fourStar++;
//                                break;
//                            case 5:
//                                fiveStar++;
//                                break;
//                        }
//                    }
//                    int feedbackCount = place.getFeedbacks().size();
//                    ratingList.set(0, fiveStar / feedbackCount);
//                    ratingList.set(1, fourStar / feedbackCount);
//                    ratingList.set(2, threeStar / feedbackCount);
//                    ratingList.set(3, twoStar / feedbackCount);
//                    ratingList.set(4, oneStar / feedbackCount);
//                }
//
//                setInfo();
//                Collections.reverse(feedbackList);
//                feedbackItemAdapter.notifyDataSetChanged();
//            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }

    @SuppressLint("SetTextI18n")
    private void setInfo() {
//        // Average rating
//        TextView placeAverageRating = viewBinding.placeAverageRating;
//        placeAverageRating.setText(String.valueOf(place.getAverageRating()));
//
//        // Star rating view
//        StarRatingView ratingView = viewBinding.starRatingView;
//        int roundedRating = (int) Math.floor(place.getAverageRating());
//        ratingView.setRating(roundedRating);
//
//        // Rating count
//        TextView ratingCount = viewBinding.placeRatingCount;
//        ratingCount.setText("(" + place.getFeedbacks().size() + ")");
//
//        // Rating bar
//        RecyclerView recyclerView = viewBinding.raringBarList;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        RatingBarItemAdapter ratingBarItemAdapter = new RatingBarItemAdapter(ratingList);
//        recyclerView.setAdapter(ratingBarItemAdapter);
//
//        // Star rating outline
//        StarRatingOutlineView starRatingOutlineView = viewBinding.starRatingOutlineView;
//        starRatingOutlineView.setRating(0);
//        starRatingOutlineView.setPlaceId(place.getId());
//        starRatingOutlineView.setPlaceName(place.getName());
//        starRatingOutlineView.enableIntent();
    }
}