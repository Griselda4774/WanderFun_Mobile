package com.example.wanderfunmobile.presentation.ui.adapter.leaderboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wanderfunmobile.presentation.ui.fragment.ranking.LeaderboardPlaceFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.ranking.LeaderboardUserFragment;

public class LeaderboardTabAdapter extends FragmentStateAdapter {
    public LeaderboardTabAdapter(@NonNull FragmentActivity fa) {
        super(fa);
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
