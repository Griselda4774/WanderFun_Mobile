package com.example.wanderfunmobile.infrastructure.ui.fragment.place;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.FragmentPlaceImageInfoBinding;

public class PlaceImageInfoFragment extends Fragment {

    private FragmentPlaceImageInfoBinding viewBinding;

    public PlaceImageInfoFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentPlaceImageInfoBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}