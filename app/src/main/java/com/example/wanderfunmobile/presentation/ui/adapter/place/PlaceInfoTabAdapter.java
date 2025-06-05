package com.example.wanderfunmobile.presentation.ui.adapter.place;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceDescriptionInfoFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceGeneralInfoFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceImageInfoFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceRatingInfoFragment;

public class PlaceInfoTabAdapter extends FragmentStateAdapter {
    public PlaceInfoTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new PlaceRatingInfoFragment();
            case 2:
                return new PlaceImageInfoFragment();
            default:
                return new PlaceDescriptionInfoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
