package com.example.wanderfunmobile.presentation.ui.adapter.place;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceDescriptionInfoFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceGeneralInfoFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceImageInfoFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceRatingInfoFragment;

public class PlaceInfoTabAdapter extends FragmentStateAdapter {
    private final Long placeId;
    public PlaceInfoTabAdapter(@NonNull FragmentActivity fragmentActivity, Long placeId) {
        super(fragmentActivity);
        this.placeId = placeId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putLong("place_id", placeId);
        Fragment fragment = switch (position) {
            case 1 -> new PlaceRatingInfoFragment();
            case 2 -> new PlaceImageInfoFragment();
            default -> new PlaceDescriptionInfoFragment();
        };
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
