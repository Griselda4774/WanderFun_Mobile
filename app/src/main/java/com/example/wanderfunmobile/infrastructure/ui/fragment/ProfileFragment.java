package com.example.wanderfunmobile.infrastructure.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.FragmentProfileBinding;
import com.example.wanderfunmobile.infrastructure.ui.activity.LoginActivity;
import com.example.wanderfunmobile.infrastructure.ui.activity.profile.MyProfileActivity;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding viewBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false);

        ConstraintLayout profileSection = viewBinding.profileSection;
        profileSection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyProfileActivity.class);
            startActivity(intent);
        });

        ConstraintLayout logoutSection = viewBinding.logOutSection;
        logoutSection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}