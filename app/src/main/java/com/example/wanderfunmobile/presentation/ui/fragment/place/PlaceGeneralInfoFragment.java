//package com.example.wanderfunmobile.presentation.ui.fragment.place;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.wanderfunmobile.databinding.FragmentPlaceGeneralInfoBinding;
//import com.example.wanderfunmobile.domain.model.places.Feedback;
//import com.example.wanderfunmobile.domain.model.places.Place;
//import com.example.wanderfunmobile.presentation.ui.adapter.place.FeedbackItemAdapter;
//import com.example.wanderfunmobile.presentation.ui.fragment.ExploreFragment;
//import com.example.wanderfunmobile.data.mapper.ObjectMapper;
//import com.example.wanderfunmobile.presentation.viewmodel.places.PlaceViewModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import dagger.hilt.android.AndroidEntryPoint;
//
//@AndroidEntryPoint
//public class PlaceGeneralInfoFragment extends Fragment {
//
//    private FragmentPlaceGeneralInfoBinding viewBinding;
//    private PlaceViewModel placeViewModel;
//    private Place place;
//    private List<Feedback> feedbackList = new ArrayList<>();
//    private List<Float> ratingList = new ArrayList<>(List.of(0f, 0f, 0f, 0f, 0f));
//    @Inject
//    ObjectMapper objectMapper;
//
//    public PlaceGeneralInfoFragment() {
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        viewBinding = FragmentPlaceGeneralInfoBinding.inflate(inflater, container, false);
//        placeViewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);
//        return viewBinding.getRoot();
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        RecyclerView recyclerView = viewBinding.placeRatingList;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        FeedbackItemAdapter feedbackItemAdapter = new FeedbackItemAdapter(feedbackList);
//        recyclerView.setAdapter(feedbackItemAdapter);
//
//        viewBinding.viewAllGeneralInfo.setOnClickListener(v -> {
//            Fragment parentFragment = getParentFragment();
//            if (parentFragment instanceof ExploreFragment) {
//                ((ExploreFragment) parentFragment).switchTab(3);
//            }
//        });
//
//        viewBinding.viewAllRating.setOnClickListener(v -> {
//            Fragment parentFragment = getParentFragment();
//            if (parentFragment instanceof ExploreFragment) {
//                ((ExploreFragment) parentFragment).switchTab(1);
//            }
//        });
//
//        placeViewModel.getFindPlaceByIdResponseLiveData().observe(getViewLifecycleOwner(), data -> {
////            if (data != null && !data.isError()) {
////                place = objectMapper.map(data.getData(), Place.class);
////                feedbackList.clear();
////                List<Feedback> tempList = place.getFeedbacks();
////                Collections.reverse(tempList);
////                if (tempList.size() < 3) {
////                    feedbackList.addAll(tempList);
////                } else {
////                    feedbackList.addAll(tempList.subList(0, 3));
////                }
////                float oneStar = 0, twoStar = 0, threeStar = 0, fourStar = 0, fiveStar = 0;
////                for (Feedback feedback : place.getFeedbacks()) {
////                    switch (feedback.getRating()) {
////                        case 1:
////                            oneStar++;
////                            break;
////                        case 2:
////                            twoStar++;
////                            break;
////                        case 3:
////                            threeStar++;
////                            break;
////                        case 4:
////                            fourStar++;
////                            break;
////                        case 5:
////                            fiveStar++;
////                            break;
////                    }
////                }
////                int feedbackCount = place.getFeedbacks().size();
////                ratingList.set(0, fiveStar / feedbackCount);
////                ratingList.set(1, fourStar / feedbackCount);
////                ratingList.set(2, threeStar / feedbackCount);
////                ratingList.set(3, twoStar / feedbackCount);
////                ratingList.set(4, oneStar / feedbackCount);
////
////                setInfo();
////                feedbackItemAdapter.notifyDataSetChanged();
////            }
//        });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        viewBinding = null;
//        placeViewModel = null;
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void setInfo() {
////        // Address
////        ConstraintLayout placeAddress = viewBinding.placeAddress;
////        TextView placeAddressContent = viewBinding.placeAddressContent;
////        if (place.getAddress() != null && !place.getAddress().isEmpty()) {
////            placeAddressContent.setText(place.getAddress());
////        } else {
////            placeAddress.setVisibility(View.GONE);
////        }
////
////        // Time open & close
////        ConstraintLayout placeTimeOpening = viewBinding.placeTimeOpening;
////        TextView placeTimeOpeningTimeClose = viewBinding.placeTimeOpeningTimeClose;
////        ConstraintLayout placeTimeClosing = viewBinding.placeTimeClosing;
////        TextView placeTimeClosingTimeOpen = viewBinding.placeTimeClosingTimeOpen;
////        if (place.isClosing()) {
////            placeTimeOpening.setVisibility(View.GONE);
////            placeTimeClosing.setVisibility(View.GONE);
////            viewBinding.openAllDay.setVisibility(View.GONE);
////            viewBinding.closing.setVisibility(View.VISIBLE);
////        } else {
////            if (place.isOpenAllDay()) {
////                placeTimeOpening.setVisibility(View.GONE);
////                placeTimeClosing.setVisibility(View.GONE);
////                viewBinding.openAllDay.setVisibility(View.VISIBLE);
////                viewBinding.closing.setVisibility(View.GONE);
////            } else {
////                if (place.getTimeOpen() != null && place.getTimeClose() != null) {
////                    LocalTime currentTime = LocalTime.now();
////                    if (place.getTimeClose().isBefore(currentTime) || place.getTimeOpen().isAfter(currentTime)) {
////                        placeTimeClosing.setVisibility(View.VISIBLE);
////                        placeTimeOpening.setVisibility(View.GONE);
////                        viewBinding.openAllDay.setVisibility(View.GONE);
////                        viewBinding.closing.setVisibility(View.GONE);
////                        placeTimeClosingTimeOpen.setText(place.getTimeOpen().toString());
////                    } else {
////                        placeTimeOpening.setVisibility(View.VISIBLE);
////                        placeTimeClosing.setVisibility(View.GONE);
////                        viewBinding.openAllDay.setVisibility(View.GONE);
////                        viewBinding.closing.setVisibility(View.GONE);
////                        placeTimeOpeningTimeClose.setText(DateTimeUtil.localTimeToString(place.getTimeClose()));
////                    }
////                } else {
////                    placeTimeOpening.setVisibility(View.GONE);
////                    placeTimeClosing.setVisibility(View.GONE);
////                    viewBinding.openAllDay.setVisibility(View.GONE);
////                    viewBinding.closing.setVisibility(View.GONE);
////                }
////            }
////        }
////
////        // Check-in point
////        TextView placeCheckInPointContent = viewBinding.placeCheckInPointContent;
////        placeCheckInPointContent.setText("Điểm check-in: " + place.getCheckInPoint());
////
////        // Average rating
////        TextView placeAverageRating = viewBinding.placeAverageRating;
////        placeAverageRating.setText(String.valueOf(place.getAverageRating()));
////
////        // Star rating view
////        StarRatingView ratingView = viewBinding.starRatingView;
////        int roundedRating = (int) Math.floor(place.getAverageRating());
////        ratingView.setRating(roundedRating);
////
////        // Rating count
////        TextView ratingCount = viewBinding.placeRatingCount;
////        ratingCount.setText("(" + place.getFeedbacks().size() + ")");
////
////        // Rating bar
////        RecyclerView recyclerView = viewBinding.raringBarList;
////        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
////        RatingBarItemAdapter ratingBarItemAdapter = new RatingBarItemAdapter(ratingList);
////        recyclerView.setAdapter(ratingBarItemAdapter);
////
////        // Star rating outline
////        StarRatingOutlineView starRatingOutlineView = viewBinding.starRatingOutlineView;
////        starRatingOutlineView.setRating(0);
////        starRatingOutlineView.setPlaceId(place.getId());
////        starRatingOutlineView.setPlaceName(place.getName());
////        starRatingOutlineView.enableIntent();
//    }
//}