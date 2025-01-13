package com.example.wanderfunmobile.infrastructure.ui.adapter.leaderboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wanderfunmobile.infrastructure.ui.fragment.leaderboard.LeaderboardPlaceFragment;
import com.example.wanderfunmobile.infrastructure.ui.fragment.leaderboard.LeaderboardUserFragment;

public class LeaderboardTabAdapter extends FragmentStateAdapter {
    public LeaderboardTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new LeaderboardPlaceFragment();
        }
        return new LeaderboardUserFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
