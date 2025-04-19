package com.example.wanderfunmobile.presentation.ui.fragment.leaderboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardUserDto;
import com.example.wanderfunmobile.databinding.FragmentLeaderboardUserBinding;
import com.example.wanderfunmobile.domain.model.LeaderboardUser;
import com.example.wanderfunmobile.presentation.ui.adapter.leaderboard.LeaderboardUserCardAdapter;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.LeaderboardViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LeaderboardUserFragment extends Fragment {
    @Inject
    ObjectMapper objectMapper;
    List<LeaderboardUser> leaderboardUserList;
    FragmentLeaderboardUserBinding viewBinding;
    LeaderboardViewModel leaderboardViewModel;

    public LeaderboardUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentLeaderboardUserBinding.inflate(inflater, container, false);
        leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        return viewBinding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = viewBinding.leaderboardUserRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView[] userNames = {viewBinding.firstPlaceName, viewBinding.secondPlaceName, viewBinding.thirdPlaceName};
        TextView[] userScores = {viewBinding.firstPlaceScore, viewBinding.secondPlaceScore, viewBinding.thirdPlaceScore};
        ImageView[] userAvatars = {viewBinding.firstPlaceAvatar, viewBinding.secondPlaceAvatar, viewBinding.thirdPlaceAvatar};

        leaderboardViewModel.getLeaderboardUser();
        leaderboardViewModel.getLeaderboardUserResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (!data.isError()) {
                List<LeaderboardUserDto> leaderboardUserDtoList = data.getData();
                leaderboardUserList = objectMapper.mapList(leaderboardUserDtoList, LeaderboardUser.class);

                // Handle top 3 users safely
                for (int i = 0; i < userNames.length; i++) {
                    if (i < leaderboardUserList.size()) {
                        userNames[i].setText(leaderboardUserList.get(i).getFirstName() + " " + leaderboardUserList.get(i).getLastName());
                        userScores[i].setText(leaderboardUserList.get(i).getPoint() + " điểm");
                        if (leaderboardUserList.get(i).getAvatarUrl() != null) {
                            Glide.with(userAvatars[i])
                                    .load(leaderboardUserList.get(i).getAvatarUrl())
                                    .into(userAvatars[i]);
                        } else {
                            userAvatars[i].setImageResource(R.drawable.ic_grey_avatar);
                        }
                    } else {
                        userNames[i].setText("N/A");
                        userScores[i].setText("0");
                        userAvatars[i].setImageResource(R.drawable.ic_grey_avatar);
                    }
                }

                // Handle the remaining list for the RecyclerView
                if (leaderboardUserList.size() > 3) {
                    recyclerView.setAdapter(new LeaderboardUserCardAdapter(
                            leaderboardUserList.subList(3, leaderboardUserList.size())
                    ));
                } else {
                    // Set an empty adapter or show a placeholder message
                    recyclerView.setAdapter(new LeaderboardUserCardAdapter(new ArrayList<>()));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}