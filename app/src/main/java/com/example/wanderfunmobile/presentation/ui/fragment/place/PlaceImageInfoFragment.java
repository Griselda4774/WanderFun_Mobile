package com.example.wanderfunmobile.presentation.ui.fragment.place;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wanderfunmobile.databinding.FragmentPlaceImageInfoBinding;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnContentChangeListener;

public class PlaceImageInfoFragment extends Fragment {
    private FragmentPlaceImageInfoBinding viewBinding;
    private Long placeId;
    private OnContentChangeListener contentChangeListener;

    public PlaceImageInfoFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placeId = getArguments().getLong("place_id");
        }
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

    public void setOnContentChangeListener(OnContentChangeListener listener) {
        this.contentChangeListener = listener;
    }

    private void notifyContentChanged() {
        if (contentChangeListener != null) {
            contentChangeListener.onContentChanged();
        }
    }
}