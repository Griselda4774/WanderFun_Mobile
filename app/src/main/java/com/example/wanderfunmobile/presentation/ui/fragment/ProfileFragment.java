package com.example.wanderfunmobile.presentation.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.FavoritePlaceManager;
import com.example.wanderfunmobile.core.util.PostViewManager;
import com.example.wanderfunmobile.databinding.FragmentProfileBinding;
import com.example.wanderfunmobile.domain.model.users.User;
import com.example.wanderfunmobile.presentation.ui.activity.auth.LoginActivity;
import com.example.wanderfunmobile.presentation.ui.activity.album.MyAlbumActivity;
import com.example.wanderfunmobile.presentation.ui.activity.checkin.CheckInHistoryActivity;
import com.example.wanderfunmobile.presentation.ui.activity.favoriteplace.FavoritePlaceActivity;
import com.example.wanderfunmobile.presentation.ui.activity.leaderboard.LeaderboardActivity;
import com.example.wanderfunmobile.presentation.ui.activity.profile.ProfileActivity;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    private FragmentProfileBinding viewBinding;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;

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

        initViewModel();

        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeViewModel();

        setUpView();

        fetchUserData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void observeViewModel() {
        authViewModel.getLogoutResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.isError()) {
                Toast.makeText(requireActivity().getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                SessionManager.getInstance(requireActivity().getApplicationContext()).logout();
                PostViewManager.getInstance(requireActivity().getApplicationContext()).reset();
                FavoritePlaceManager.getInstance(requireActivity().getApplicationContext()).clear();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            } else {
                Toast.makeText(requireActivity().getApplicationContext(), "Đăng xuất không thành công", Toast.LENGTH_SHORT).show();
            }
        });

        userViewModel.getMiniSelfInfoResponseLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!result.isError() && result.getData() != null) {
                bindUserData(result.getData());
            } else {
                Toast.makeText(requireActivity().getApplicationContext(), "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpView() {
        // Profile
        viewBinding.profileSection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        // Favorite Places
        viewBinding.favoritePlaceSection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FavoritePlaceActivity.class);
            startActivity(intent);
        });

        // My Albums
        viewBinding.albumSection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyAlbumActivity.class);
            startActivity(intent);
        });

        // Check-in history
        viewBinding.checkInHistorySection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CheckInHistoryActivity.class);
            startActivity(intent);
        });

        // Leaderboard
        viewBinding.leaderboardSection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LeaderboardActivity.class);
            startActivity(intent);
        });

        // Logout
        viewBinding.logOutSection.setOnClickListener(v -> {
            authViewModel.logout("Bearer " + SessionManager.getInstance(requireActivity().getApplicationContext().getApplicationContext()).getAccessToken());
        });
    }

    @SuppressLint("SetTextI18n")
    private void bindUserData(User user) {
        viewBinding.userName.setText(user.getLastName() + " " + user.getFirstName());

        if (user.getAvatarImage() != null) {
            Glide.with(requireContext())
                    .load(user.getAvatarImage().getImageUrl())
                    .placeholder(R.drawable.ic_avatar)
                    .error(R.drawable.ic_avatar)
                    .into(viewBinding.userAvatar);
        } else {
            viewBinding.userAvatar.setImageResource(R.drawable.ic_avatar);
        }
    }

    private void fetchUserData() {
        userViewModel.getMiniSelfInfo("Bearer " + SessionManager.getInstance(requireActivity().getApplicationContext()).getAccessToken());
    }
}