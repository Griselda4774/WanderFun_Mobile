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
import android.widget.TextView;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.FragmentNotSignedInBinding;
import com.example.wanderfunmobile.infrastructure.ui.activity.LoginActivity;
import com.example.wanderfunmobile.infrastructure.ui.activity.RegisterActivity;

public class NotSignedInFragment extends Fragment {

    FragmentNotSignedInBinding viewBinding;

    public NotSignedInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentNotSignedInBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Login button
        TextView loginButton = viewBinding.loginButton.findViewById(R.id.button);
        loginButton.setText("Đăng nhập");
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        // Register button
        TextView registerButton = viewBinding.registerButton.findViewById(R.id.button);
        registerButton.setText("Đăng ký");
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}