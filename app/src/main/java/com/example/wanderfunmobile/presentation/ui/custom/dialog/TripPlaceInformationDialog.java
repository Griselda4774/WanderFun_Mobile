package com.example.wanderfunmobile.presentation.ui.custom.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.StringUtil;
import com.example.wanderfunmobile.databinding.DialogPlaceSelectionBinding;
import com.example.wanderfunmobile.databinding.DialogTripPlaceInformationBinding;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;

public class TripPlaceInformationDialog extends Dialog {

    private DialogTripPlaceInformationBinding binding;

    private TripPlace tripPlace;

    public TripPlaceInformationDialog(@NonNull Context context, TripPlace tripPlace) {
        super(context);
        this.tripPlace = tripPlace;
        init(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DialogTripPlaceInformationBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());

        // Dialog setup
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        setupDismissSpace();
        setupTripPlaceInformation();
    }

    private void setupDismissSpace(){
        binding.dialog.setOnClickListener(v -> {
            dismiss(); // user tapped outside dialog
        });

        binding.innerContainer.setOnClickListener(v -> {
            // do nothing — prevents the click from propagating
        });
    }

    @SuppressLint("SetTextI18n")
    private void setupTripPlaceInformation() {
        binding.tripPlaceName.setText(tripPlace.getPlace().getName());
        binding.tripPlaceNote.setText(tripPlace.getPlaceNotes());
        binding.tripPlaceAddress.setText(StringUtil.formatAddressToStringNoStreet(tripPlace.getPlace().getAddress()));
        binding.tripPlaceTime.setText(DateTimeUtil.localDateToString(tripPlace.getStartTime()) + " - " + DateTimeUtil.localDateToString(tripPlace.getEndTime()));
        Glide.with(getContext())
                .load(tripPlace.getPlace().getCoverImage().getImageUrl())
                .into(binding.tripPlaceImage);
    }

}
