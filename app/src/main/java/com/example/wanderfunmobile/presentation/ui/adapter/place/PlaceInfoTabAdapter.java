package com.example.wanderfunmobile.presentation.ui.adapter.place;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wanderfunmobile.core.util.ViewPager2HeightAdjuster;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnContentChangeListener;
import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceDescriptionInfoFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceImageInfoFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.place.PlaceRatingInfoFragment;

public class PlaceInfoTabAdapter extends FragmentStateAdapter {
    private final String placeJson;
    private final ViewPager2 viewPager2;
    public PlaceInfoTabAdapter(@NonNull FragmentActivity fragmentActivity, String placeJson, ViewPager2 viewPager2) {
        super(fragmentActivity);
        this.placeJson = placeJson;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("place_json", placeJson);
        Fragment fragment = switch (position) {
            case 1 -> new PlaceRatingInfoFragment();
            case 2 -> new PlaceImageInfoFragment();
            default -> new PlaceDescriptionInfoFragment();
        };
        fragment.setArguments(bundle);

        OnContentChangeListener listener = () -> {
            viewPager2.post(() -> ViewPager2HeightAdjuster.requestHeightUpdate(viewPager2));
        };

        if (fragment instanceof PlaceDescriptionInfoFragment descFragment) {
            descFragment.setOnContentChangeListener(listener);
        } else if (fragment instanceof PlaceRatingInfoFragment ratingFragment) {
            ratingFragment.setOnContentChangeListener(listener);
        } else if (fragment instanceof PlaceImageInfoFragment imageFragment) {
            imageFragment.setOnContentChangeListener(listener);
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
