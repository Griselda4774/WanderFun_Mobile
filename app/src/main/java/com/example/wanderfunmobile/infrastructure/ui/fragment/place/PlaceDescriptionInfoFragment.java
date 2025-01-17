package com.example.wanderfunmobile.infrastructure.ui.fragment.place;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.databinding.FragmentPlaceDescriptionInfoBinding;
import com.example.wanderfunmobile.domain.model.Place;
import com.example.wanderfunmobile.domain.model.Section;
import com.example.wanderfunmobile.infrastructure.ui.adapter.place.PlaceDescriptionItemAdapter;
import com.example.wanderfunmobile.infrastructure.util.DateTimeUtil;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;

import java.time.LocalTime;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlaceDescriptionInfoFragment extends Fragment {

    private FragmentPlaceDescriptionInfoBinding viewBinding;
    private Place place;
    private List<Section> descriptionList;
    private PlaceViewModel placeViewModel;
    @Inject
    ObjectMapper objectMapper;

    public PlaceDescriptionInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentPlaceDescriptionInfoBinding.inflate(inflater, container, false);
        assert getParentFragment() != null;
        placeViewModel = new ViewModelProvider(getParentFragment()).get(PlaceViewModel.class);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeViewModel.getPlaceByIdResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (!data.isError()) {
                place = objectMapper.map(data.getData(), Place.class);
                descriptionList = place.getDescription();
                setInfo();
                showDescriptionList();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }

    @SuppressLint("SetTextI18n")
    private void setInfo() {
        // Address
        ConstraintLayout placeAddress = viewBinding.placeAddress;
        TextView placeAddressContent = viewBinding.placeAddressContent;
        if (place.getAddress() != null && !place.getAddress().isEmpty()) {
            placeAddressContent.setText(place.getAddress());
        } else {
            placeAddress.setVisibility(View.GONE);
        }

        // Time open & close
        ConstraintLayout placeTimeOpening = viewBinding.placeTimeOpening;
        TextView placeTimeOpeningTimeClose = viewBinding.placeTimeOpeningTimeClose;
        ConstraintLayout placeTimeClosing = viewBinding.placeTimeClosing;
        TextView placeTimeClosingTimeOpen = viewBinding.placeTimeClosingTimeOpen;
        if (place.getTimeOpen() != null && place.getTimeClose() != null) {
            LocalTime currentTime = LocalTime.now();
            if (place.getTimeClose().isBefore(currentTime) || place.getTimeOpen().isAfter(currentTime)) {
                placeTimeClosing.setVisibility(View.VISIBLE);
                placeTimeOpening.setVisibility(View.GONE);
                placeTimeClosingTimeOpen.setText(place.getTimeOpen().toString());
            } else {
                placeTimeOpening.setVisibility(View.VISIBLE);
                placeTimeClosing.setVisibility(View.GONE);
                placeTimeOpeningTimeClose.setText(DateTimeUtil.localTimeToString(place.getTimeClose()));
            }
        } else {
            placeTimeOpening.setVisibility(View.GONE);
            placeTimeClosing.setVisibility(View.GONE);
        }

        // Category
        ConstraintLayout placeCategory = viewBinding.placeCategory;
        TextView placeCategoryContent = viewBinding.placeCategoryContent;
        if (place.getCategory() != null) {
            placeCategoryContent.setText(place.getCategory().name());
        } else {
            placeCategory.setVisibility(View.GONE);
        }

        // Check-in point
        TextView placeCheckInPointContent = viewBinding.placeCheckInPointContent;
        placeCheckInPointContent.setText("Điểm check-in: " + place.getCheckInPoint());

        // Alternative name
        ConstraintLayout placeAlternativeName = viewBinding.placeAlternativeName;
        TextView placeAlternativeNameContent = viewBinding.placeAlternativeNameContent;
        if (place.getAlternativeName() != null && !place.getAlternativeName().isEmpty()) {
            placeAlternativeNameContent.setText(place.getAlternativeName());
        } else {
            placeAlternativeName.setVisibility(View.GONE);
        }

        // Operator
        ConstraintLayout placeOperator = viewBinding.placeOperator;
        TextView placeOperatorContent = viewBinding.placeOperatorContent;
        if (place.getOperator() != null && !place.getOperator().isEmpty()) {
            placeOperatorContent.setText("Trực thuộc: " + place.getOperator());
        } else {
            placeOperator.setVisibility(View.GONE);
        }

        // Link
        ConstraintLayout placeLink = viewBinding.placeLink;
        TextView placeLinkContent = viewBinding.placeLinkContent;
        if (place.getLink() != null && !place.getLink().isEmpty()) {
            placeLinkContent.setText(place.getLink());
        } else {
            placeLink.setVisibility(View.GONE);
        }
    }

    private void showDescriptionList() {
        RecyclerView recyclerView = viewBinding.placeDescriptionList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PlaceDescriptionItemAdapter placeDescriptionItemAdapter = new PlaceDescriptionItemAdapter(descriptionList);
        recyclerView.setAdapter(placeDescriptionItemAdapter);
    }
}