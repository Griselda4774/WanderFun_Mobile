package com.example.wanderfunmobile.infrastructure.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wanderfunmobile.infrastructure.ui.fragment.leaderboard.PlaceLeaderboardFragment;
import com.example.wanderfunmobile.infrastructure.ui.fragment.leaderboard.UserLeaderboardFragment;

public class LeaderboardTabAdapter extends FragmentStateAdapter {
    public LeaderboardTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new PlaceLeaderboardFragment();
        }
        return new UserLeaderboardFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
