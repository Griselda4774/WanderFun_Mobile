package com.example.wanderfunmobile.infrastructure.ui.adapter.leaderboard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.databinding.ItemLeaderboardCardBinding;
import com.example.wanderfunmobile.domain.model.LeaderboardPlace;

import java.util.List;

public class LeaderboardPlaceCardAdapter extends RecyclerView.Adapter<LeaderboardPlaceCardAdapter.LeaderboardCardViewHolder> {

    List<LeaderboardPlace> placeList;

    public LeaderboardPlaceCardAdapter(List<LeaderboardPlace> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public LeaderboardCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLeaderboardCardBinding itemLeaderboardCardBinding = ItemLeaderboardCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LeaderboardCardViewHolder(itemLeaderboardCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardCardViewHolder holder, int position) {
        LeaderboardPlace place = placeList.get(position);
        holder.bind(place);
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class LeaderboardCardViewHolder extends RecyclerView.ViewHolder {
        final ItemLeaderboardCardBinding itemLeaderboardCardBinding;

        public LeaderboardCardViewHolder(@NonNull ItemLeaderboardCardBinding itemLeaderboardCardBinding) {
            super(itemLeaderboardCardBinding.getRoot());
            this.itemLeaderboardCardBinding = itemLeaderboardCardBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(LeaderboardPlace place) {
            itemLeaderboardCardBinding.textRankNumber.setText(place.getRank());
            itemLeaderboardCardBinding.textName.setText(place.getName());
            itemLeaderboardCardBinding.textScore.setText(place.getCheckInCount());

            Glide.with(itemLeaderboardCardBinding.getRoot())
                    .load(place.getCoverImageUrl())
                    .into(itemLeaderboardCardBinding.image);
        }
    }
}
