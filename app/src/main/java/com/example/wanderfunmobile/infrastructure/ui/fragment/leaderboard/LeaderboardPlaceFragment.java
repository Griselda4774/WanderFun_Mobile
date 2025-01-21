package com.example.wanderfunmobile.infrastructure.ui.fragment.leaderboard;

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
import com.example.wanderfunmobile.application.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.databinding.FragmentLeaderboardPlaceBinding;
import com.example.wanderfunmobile.domain.model.LeaderboardPlace;
import com.example.wanderfunmobile.infrastructure.ui.adapter.leaderboard.LeaderboardPlaceCardAdapter;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.LeaderboardViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LeaderboardPlaceFragment extends Fragment {
    @Inject
    ObjectMapper objectMapper;
    List<LeaderboardPlace> leaderboardPlaceList;
    FragmentLeaderboardPlaceBinding viewBinding;
    LeaderboardViewModel leaderboardViewModel;

    public LeaderboardPlaceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentLeaderboardPlaceBinding.inflate(inflater, container, false);
        leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = viewBinding.leaderboardPlaceRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        TextView[] placeNames = {viewBinding.firstPlaceName, viewBinding.secondPlaceName, viewBinding.thirdPlaceName};
        TextView[] placeScores = {viewBinding.firstPlaceScore, viewBinding.secondPlaceScore, viewBinding.thirdPlaceScore};
        ImageView[] placeAvatars = {viewBinding.firstPlaceAvatar, viewBinding.secondPlaceAvatar, viewBinding.thirdPlaceAvatar};

        leaderboardViewModel.getLeaderboardPlace();
        leaderboardViewModel.getLeaderboardPlaceResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (!data.isError()) {
                List<LeaderboardPlaceDto> leaderboardPlaceDtoList = data.getData();
                leaderboardPlaceList = objectMapper.mapList(leaderboardPlaceDtoList, LeaderboardPlace.class);

                for (int i = 0; i < 3; i++) {
                    placeNames[i].setText(leaderboardPlaceList.get(i).getName());
                    placeScores[i].setText(leaderboardPlaceList.get(i).getCheckInCount());
                    Glide.with(placeAvatars[i])
                            .load(leaderboardPlaceList.get(i).getCoverImageUrl())
                            .into(placeAvatars[i]);
                }

                recyclerView.setAdapter(new LeaderboardPlaceCardAdapter(leaderboardPlaceList.subList(3, leaderboardPlaceList.size())));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}