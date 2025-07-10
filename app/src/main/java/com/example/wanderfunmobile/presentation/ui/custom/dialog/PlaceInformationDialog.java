package com.example.wanderfunmobile.presentation.ui.custom.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.wanderfunmobile.core.util.StringUtil;
import com.example.wanderfunmobile.databinding.DialogPlaceInformationBinding;
import com.example.wanderfunmobile.domain.model.places.Place;

public class PlaceInformationDialog extends Dialog {
    private DialogPlaceInformationBinding binding;

    private Place place;

    public PlaceInformationDialog(@NonNull Context context, Place place) {
        super(context);
        this.place = place;
        init(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DialogPlaceInformationBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());

        // Dialog setup
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        setupPlaceData();
        setupDismissSpace();
        setupSeeMoreLess();
    }

    private void setupDismissSpace() {
        binding.dialog.setOnClickListener(v -> dismiss());
        binding.innerContainer.setOnClickListener(v -> {});
    }

    private void setupSeeMoreLess() {
        binding.seeMore.setOnClickListener(v -> showExtraInfo(true));
        binding.seeLess.setOnClickListener(v -> showExtraInfo(false));
        showExtraInfo(false); // default
    }

    private void showExtraInfo(boolean expand) {
        binding.seeMore.setVisibility(expand ? View.GONE : View.VISIBLE);
        binding.seeLess.setVisibility(expand ? View.VISIBLE : View.GONE);
        binding.categorySection.setVisibility(expand ? View.VISIBLE : View.GONE);
        binding.checkInPointSection.setVisibility(expand ? View.VISIBLE : View.GONE);
        binding.alternativeNameSection.setVisibility(expand ? View.VISIBLE : View.GONE);
        binding.operatorSection.setVisibility(expand ? View.VISIBLE : View.GONE);
        binding.linkSection.setVisibility(expand ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void setupPlaceData() {
        if (place == null || place.getPlaceDetail() == null) return;

        // Address
        if (place.getAddress() != null) {
            binding.address.setText(StringUtil.formatAddressToString(place.getAddress()));
        } else {
            binding.address.setText("Không có dữ liệu");
        }

        // Price range
        if (place.getPlaceDetail().getPriceRangeTop() > 0) {
            if (place.getPlaceDetail().getPriceRangeBottom() > 0) {
                binding.priceRange.setText("Từ " +
                        place.getPlaceDetail().getPriceRangeBottom() + " đến " +
                        place.getPlaceDetail().getPriceRangeTop() + " VNĐ");
            } else {
                binding.priceRange.setText("Từ " +
                        place.getPlaceDetail().getPriceRangeTop() + " VNĐ");
            }
        } else {
            binding.priceRange.setText("Miễn phí");
        }

        // Category
        if (place.getCategory() != null) {
            binding.category.setText(place.getCategory().getName());
        } else {
            binding.category.setText("Không có dữ liệu");
        }

        // Check-in point
        binding.checkInPoint.setText(String.valueOf(place.getPlaceDetail().getCheckInPoint()));

        // Alternative name
        if (place.getPlaceDetail().getAlternativeName() != null && !place.getPlaceDetail().getAlternativeName().isEmpty()) {
            binding.alternativeName.setText(place.getPlaceDetail().getAlternativeName());
        } else {
            binding.alternativeName.setText("Không có dữ liệu");
        }

        // Operator
        if (place.getPlaceDetail().getOperator() != null && !place.getPlaceDetail().getOperator().isEmpty()) {
            binding.operator.setText(place.getPlaceDetail().getOperator());
        } else {
            binding.operator.setText("Không có dữ liệu");
        }

        // Link
        if (place.getPlaceDetail().getUrl() != null && !place.getPlaceDetail().getUrl().isEmpty()) {
            binding.link.setText(place.getPlaceDetail().getUrl());
        } else {
            binding.link.setText("Không có dữ liệu");
        }

        // Description
        if (place.getPlaceDetail().getDescription() != null && !place.getPlaceDetail().getDescription().isEmpty()) {
            binding.descriptionContent.setText(place.getPlaceDetail().getDescription());
        } else {
            binding.descriptionContent.setText("Không có dữ liệu");
        }
    }
}
