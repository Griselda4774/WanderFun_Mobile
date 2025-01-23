package com.example.wanderfunmobile.infrastructure.ui.adapter.leaderboard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ItemLeaderboardCardBinding;
import com.example.wanderfunmobile.domain.model.LeaderboardUser;

import java.util.List;

public class LeaderboardUserCardAdapter extends RecyclerView.Adapter<LeaderboardUserCardAdapter.LeaderboardCardViewHolder> {

    List<LeaderboardUser> userList;

    public LeaderboardUserCardAdapter(List<LeaderboardUser> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public LeaderboardCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLeaderboardCardBinding itemLeaderboardCardBinding = ItemLeaderboardCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LeaderboardCardViewHolder(itemLeaderboardCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardCardViewHolder holder, int position) {
        LeaderboardUser user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class LeaderboardCardViewHolder extends RecyclerView.ViewHolder {
        final ItemLeaderboardCardBinding itemLeaderboardCardBinding;

        public LeaderboardCardViewHolder(@NonNull ItemLeaderboardCardBinding itemLeaderboardCardBinding) {
            super(itemLeaderboardCardBinding.getRoot());
            this.itemLeaderboardCardBinding = itemLeaderboardCardBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(LeaderboardUser user) {
            itemLeaderboardCardBinding.textRankNumber.setText("#" + user.getRank());
            itemLeaderboardCardBinding.textName.setText(user.getFirstName() + " " + user.getLastName());
            itemLeaderboardCardBinding.textScore.setText(user.getPoint() + " điểm");

            if (user.getAvatarUrl() != null) {
                Glide.with(itemLeaderboardCardBinding.getRoot())
                        .load(user.getAvatarUrl())
                        .into(itemLeaderboardCardBinding.image);
            } else {
                itemLeaderboardCardBinding.image.setImageResource(R.drawable.ic_grey_avatar);
            }

        }
    }
}
