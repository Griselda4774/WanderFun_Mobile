package com.example.wanderfunmobile.infrastructure.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wanderfunmobile.infrastructure.ui.fragment.trip.AllTripFragment;
import com.example.wanderfunmobile.infrastructure.ui.fragment.trip.DeletedTripFragment;
import com.example.wanderfunmobile.infrastructure.ui.fragment.trip.PastTripFragment;

public class TripTabAdapter extends FragmentStateAdapter {
    public TripTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new PastTripFragment();
            case 2:
                return new DeletedTripFragment();
            default:
                return new AllTripFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
