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

import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.StringUtil;
import com.example.wanderfunmobile.databinding.FragmentPlaceDescriptionInfoBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.model.places.Section;
import com.example.wanderfunmobile.presentation.ui.adapter.place.SectionItemAdapter;
import com.example.wanderfunmobile.presentation.viewmodel.places.PlaceViewModel;
import com.google.gson.Gson;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlaceDescriptionInfoFragment extends Fragment {
    private FragmentPlaceDescriptionInfoBinding viewBinding;
    private final List<Section> sectionList = new ArrayList<>();
    private PlaceViewModel placeViewModel;
    private Place place;
    private SectionItemAdapter sectionItemAdapter;
    @Inject
    Gson gson;

    public PlaceDescriptionInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            place = gson.fromJson(getArguments().getString("place_json"), Place.class);
            sectionList.addAll(place.getPlaceDetail().getSectionList());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentPlaceDescriptionInfoBinding.inflate(inflater, container, false);

        return viewBinding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }

    private void setUpView() {
        RecyclerView recyclerView = viewBinding.sectionList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sectionItemAdapter = new SectionItemAdapter(sectionList);
        recyclerView.setAdapter(sectionItemAdapter);

        bindPlaceData(place);
    }

    @SuppressLint("SetTextI18n")
    private void bindPlaceData(Place place) {
        if (place != null) {
            // Address
            if (place.getAddress() != null) {
                viewBinding.address.setText(StringUtil.formatAddressToString(place.getAddress()));
            } else {
                viewBinding.address.setText("Không có dữ liệu");
            }

            // Time open & close
            if (place.getPlaceDetail().isClosed()) {
                viewBinding.opening.setVisibility(View.GONE);
                viewBinding.closing.setVisibility(View.GONE);
                viewBinding.openAllDay.setVisibility(View.GONE);
                viewBinding.closed.setVisibility(View.VISIBLE);
            } else {
                if (place.getPlaceDetail().isOpenAllDay()) {
                    viewBinding.opening.setVisibility(View.GONE);
                    viewBinding.closing.setVisibility(View.GONE);
                    viewBinding.openAllDay.setVisibility(View.VISIBLE);
                    viewBinding.closed.setVisibility(View.GONE);
                } else {
                    if (place.getPlaceDetail().getTimeOpen() != null && place.getPlaceDetail().getTimeClose() != null) {
                        LocalTime currentTime = LocalTime.now();
                        if (place.getPlaceDetail().getTimeClose().isBefore(currentTime) || place.getPlaceDetail().getTimeOpen().isAfter(currentTime)) {
                            viewBinding.closing.setVisibility(View.VISIBLE);
                            viewBinding.opening.setVisibility(View.GONE);
                            viewBinding.openAllDay.setVisibility(View.GONE);
                            viewBinding.closed.setVisibility(View.GONE);
                            viewBinding.openTime.setText(place.getPlaceDetail().getTimeOpen().toString());
                        } else {
                            viewBinding.opening.setVisibility(View.VISIBLE);
                            viewBinding.closing.setVisibility(View.GONE);
                            viewBinding.openAllDay.setVisibility(View.GONE);
                            viewBinding.closed.setVisibility(View.GONE);
                            viewBinding.closeTime.setText(DateTimeUtil.localTimeToString(place.getPlaceDetail().getTimeClose()));
                        }
                    } else {
                        viewBinding.opening.setVisibility(View.GONE);
                        viewBinding.closing.setVisibility(View.GONE);
                        viewBinding.openAllDay.setVisibility(View.GONE);
                        viewBinding.closed.setVisibility(View.GONE);
                    }
                }
            }

            // Category
            if (place.getCategory() != null) {
                viewBinding.category.setText(place.getCategory().getName());
            } else {
                viewBinding.category.setText("Không có dữ liệu");
            }

            // Check-in point
            viewBinding.checkInPoint.setText(String.valueOf(place.getPlaceDetail().getCheckInPoint()));


            // Alternative name
            if (place.getPlaceDetail().getAlternativeName() != null && !place.getPlaceDetail().getAlternativeName().isEmpty()) {
                viewBinding.alternativeName.setText(place.getPlaceDetail().getAlternativeName());
            } else {
                viewBinding.alternativeName.setText("Không có dữ liệu");
            }

            // Operator
            if (place.getPlaceDetail().getOperator() != null && !place.getPlaceDetail().getOperator().isEmpty()) {
                viewBinding.operator.setText(place.getPlaceDetail().getOperator());
            } else {
                viewBinding.operator.setText("Không có dữ liệu");
            }

            // Link
            if (place.getPlaceDetail().getUrl() != null && !place.getPlaceDetail().getUrl().isEmpty()) {
                viewBinding.link.setText(place.getPlaceDetail().getUrl());
            } else {
                viewBinding.link.setText("Không có dữ liệu");
            }
        }
    }
}