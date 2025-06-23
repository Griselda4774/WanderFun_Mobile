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
import com.example.wanderfunmobile.databinding.FragmentPlaceDescriptionInfoBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.model.places.Section;
import com.example.wanderfunmobile.presentation.ui.adapter.place.SectionItemAdapter;
import com.example.wanderfunmobile.presentation.viewmodel.places.PlaceViewModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlaceDescriptionInfoFragment extends Fragment {

    private FragmentPlaceDescriptionInfoBinding viewBinding;
    private final List<Section> sectionList = new ArrayList<>();
    private PlaceViewModel placeViewModel;

    private SectionItemAdapter sectionItemAdapter;

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
        placeViewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);
        return viewBinding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = viewBinding.placeDescriptionList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sectionItemAdapter = new SectionItemAdapter(sectionList);
        recyclerView.setAdapter(sectionItemAdapter);

        placeViewModel.getFindPlaceByIdResponseLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!result.isError() && result.getData() != null) {
                bindPlaceData(result.getData());
                sectionList.addAll(result.getData().getPlaceDetail().getSectionList());
                sectionItemAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
        placeViewModel = null;
    }

    @SuppressLint("SetTextI18n")
    private void bindPlaceData(Place place) {
        if (place != null) {
            // Address
            if (place.getAddress() != null) {
                StringBuilder addressBuilder = new StringBuilder();

                if (place.getAddress().getStreet() != null && !place.getAddress().getStreet().isEmpty()) {
                    addressBuilder.append(place.getAddress().getStreet());
                }

                if (place.getAddress().getWard() != null && place.getAddress().getWard().getFullName() != null && !place.getAddress().getWard().getFullName().isEmpty()) {
                    if (addressBuilder.length() > 0) addressBuilder.append(", ");
                    addressBuilder.append(place.getAddress().getWard().getFullName());
                }

                if (place.getAddress().getDistrict() != null && place.getAddress().getDistrict().getFullName() != null && !place.getAddress().getDistrict().getFullName().isEmpty()) {
                    if (addressBuilder.length() > 0) addressBuilder.append(", ");
                    addressBuilder.append(place.getAddress().getDistrict().getFullName());
                }

                if (place.getAddress().getProvince() != null && place.getAddress().getProvince().getFullName() != null && !place.getAddress().getProvince().getFullName().isEmpty()) {
                    if (addressBuilder.length() > 0) addressBuilder.append(", ");
                    addressBuilder.append(place.getAddress().getProvince().getFullName());
                }

                String address = addressBuilder.toString();

                viewBinding.address.setText(address);
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