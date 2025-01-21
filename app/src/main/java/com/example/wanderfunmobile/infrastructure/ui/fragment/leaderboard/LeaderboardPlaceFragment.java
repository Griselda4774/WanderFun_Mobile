package com.example.wanderfunmobile.infrastructure.ui.fragment.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.application.dto.leaderboard.LeaderboardUserDto;
import com.example.wanderfunmobile.databinding.FragmentLeaderboardPlaceBinding;
import com.example.wanderfunmobile.domain.model.LeaderboardUser;
import com.example.wanderfunmobile.infrastructure.ui.adapter.leaderboard.LeaderboardUserCardAdapter;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.LeaderboardViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LeaderboardPlaceFragment extends Fragment {
    @Inject
    ObjectMapper objectMapper;
    List<LeaderboardUser> leaderboardUserList;
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

        leaderboardViewModel.getLeaderboardUser();
        leaderboardViewModel.getLeaderboardUserResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.isError()) {
                List<LeaderboardUserDto> leaderboardUserDtoList = data.getData();
                leaderboardUserList = objectMapper.mapList(leaderboardUserDtoList, LeaderboardUser.class);
                recyclerView.setAdapter(new LeaderboardUserCardAdapter(leaderboardUserList));

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}