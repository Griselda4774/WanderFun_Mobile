package com.example.wanderfunmobile.presentation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.core.util.PostViewManager;
import com.example.wanderfunmobile.databinding.FragmentProfileBinding;
import com.example.wanderfunmobile.presentation.ui.activity.LoginActivity;
import com.example.wanderfunmobile.presentation.ui.activity.album.MyAlbumActivity;
import com.example.wanderfunmobile.presentation.ui.activity.leaderboard.LeaderboardActivity;
import com.example.wanderfunmobile.presentation.ui.activity.profile.ProfileActivity;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding viewBinding;

    private AuthViewModel authViewModel;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Profile
        ConstraintLayout profileSection = viewBinding.profileSection;
        profileSection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        // My Albums
        ConstraintLayout myAlbumsSection = viewBinding.albumSection;
        myAlbumsSection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyAlbumActivity.class);
            startActivity(intent);
        });

        // Leaderboard
        ConstraintLayout leaderboardSection = viewBinding.leaderboardSection;
        leaderboardSection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LeaderboardActivity.class);
            startActivity(intent);
        });

        // Logout
        ConstraintLayout logoutSection = viewBinding.logOutSection;
        logoutSection.setOnClickListener(v -> {
            authViewModel.logout("Bearer " + SessionManager.getInstance(requireActivity().getApplicationContext().getApplicationContext()).getAccessToken());
            SessionManager.getInstance(requireActivity().getApplicationContext()).logout();
            PostViewManager.getInstance(requireActivity().getApplicationContext()).reset();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        authViewModel.getLogoutResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.isError()) {
                Toast.makeText(requireActivity().getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
//                SessionManager.getInstance(requireActivity().getApplicationContext()).logout();
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//                requireActivity().finish();
            } else {
                Toast.makeText(requireActivity().getApplicationContext(), "Đăng xuất không thành công", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}