package com.example.wanderfunmobile.presentation.ui.custom.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.DialogCheckInBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.adapter.place.PlaceItemAdapter;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnPlaceSelectedListener;
import com.example.wanderfunmobile.presentation.viewmodel.CheckInViewModel;

import java.util.ArrayList;
import java.util.List;

public class CheckInDialog extends Dialog {
    private DialogCheckInBinding binding;
    private OnPlaceSelectedListener onPlaceSelectedListener;
    private PlaceItemAdapter adapter;
    private final List<Place> placeList = new ArrayList<>();
    private final CheckInViewModel checkInViewModel;
    private LifecycleOwner lifecycleOwner;
    private final Double userLng;
    private final Double userLat;
    public CheckInDialog(@NonNull Context context, @NonNull LifecycleOwner lifecycleOwner,
                         CheckInViewModel checkInViewModel, Double userLng, Double userLat) {
        super(context);
        this.lifecycleOwner = lifecycleOwner;
        this.checkInViewModel = checkInViewModel;
        this.userLng = userLng;
        this.userLat = userLat;
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DialogCheckInBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());

        setUpDialog();

        setupDismissSpace();

        setupRecyclerView(context);

        setUpViewModel(context);

        fetchEligiblePlaces(context);
    }

    private void setUpDialog() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        binding.loading.setVisibility(View.VISIBLE);
        binding.selectPlaceText.setVisibility(View.GONE);
        binding.noPlaceText.setVisibility(View.GONE);
    }

    private void setupDismissSpace(){
        binding.dialog.setOnClickListener(v -> {
            dismiss();
        });

        binding.innerContainer.setOnClickListener(v -> {
        });
    }

    private void setupRecyclerView(Context context) {
        adapter = new PlaceItemAdapter(placeList);
        adapter.setOnPlaceSelectedListener(place -> {
            if (onPlaceSelectedListener != null) {
                onPlaceSelectedListener.onPlaceSelected(place);
            }
            dismiss();
        });

        binding.placeList.setLayoutManager(new LinearLayoutManager(context));
        binding.placeList.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpViewModel(Context context) {
        checkInViewModel.getFindAllEligiblePlacesLiveData().observe(lifecycleOwner, result -> {
            binding.loading.setVisibility(View.GONE);
            if (!result.isError() && result.getData() != null) {
                placeList.clear();
                if (!result.getData().isEmpty()) {
                    placeList.addAll(result.getData());
                    adapter.notifyDataSetChanged();
                    binding.selectPlaceText.setVisibility(View.VISIBLE);
                    binding.noPlaceText.setVisibility(View.GONE);
                } else {
                    binding.selectPlaceText.setVisibility(View.GONE);
                    binding.noPlaceText.setVisibility(View.VISIBLE);
                }
            } else {
                binding.selectPlaceText.setVisibility(View.GONE);
                binding.noPlaceText.setVisibility(View.GONE);
                Toast.makeText(context, "Không thể tải danh sách địa điểm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchEligiblePlaces(Context context) {
        checkInViewModel.findAllEligiblePlaces("Bearer " + SessionManager.getInstance(context.getApplicationContext()).getAccessToken(), userLng, userLat);
        binding.loading.setVisibility(View.VISIBLE);
    }

    public void setOnPlaceSelectedListener(OnPlaceSelectedListener listener) {
        this.onPlaceSelectedListener = listener;
    }
}
