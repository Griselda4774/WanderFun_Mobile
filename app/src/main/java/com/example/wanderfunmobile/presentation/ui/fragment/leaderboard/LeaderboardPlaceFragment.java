package com.example.wanderfunmobile.presentation.ui.fragment.leaderboard;

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
import com.example.wanderfunmobile.data.dto.leaderboard.LeaderboardPlaceDto;
import com.example.wanderfunmobile.databinding.FragmentLeaderboardPlaceBinding;
import com.example.wanderfunmobile.domain.model.LeaderboardPlace;
import com.example.wanderfunmobile.presentation.ui.adapter.leaderboard.LeaderboardPlaceCardAdapter;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.LeaderboardViewModel;

import java.util.ArrayList;
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

                // Handle top 3 places safely
                for (int i = 0; i < placeNames.length; i++) {
                    if (i < leaderboardPlaceList.size()) {
                        placeNames[i].setText(leaderboardPlaceList.get(i).getName());
                        placeScores[i].setText(String.valueOf(leaderboardPlaceList.get(i).getCheckInCount())); // Assuming check-in count is a number
                        if (leaderboardPlaceList.get(i).getCoverImageUrl() != null) {
                            Glide.with(placeAvatars[i])
                                    .load(leaderboardPlaceList.get(i).getCoverImageUrl())
                                    .into(placeAvatars[i]);
                        } else {
                            placeAvatars[i].setImageResource(R.drawable.ic_grey_avatar);
                        }
                    } else {
                        // Handle missing data: hide views or set placeholders
                        placeNames[i].setText("N/A");
                        placeScores[i].setText("0");
                        placeAvatars[i].setImageResource(R.drawable.ic_grey_avatar);
                    }
                }

                // Handle the remaining list for the RecyclerView
                if (leaderboardPlaceList.size() > 3) {
                    recyclerView.setAdapter(new LeaderboardPlaceCardAdapter(
                            leaderboardPlaceList.subList(3, leaderboardPlaceList.size())
                    ));
                } else {
                    // Set an empty adapter or show a placeholder message
                    recyclerView.setAdapter(new LeaderboardPlaceCardAdapter(new ArrayList<>()));
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