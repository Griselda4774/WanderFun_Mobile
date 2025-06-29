package com.example.wanderfunmobile.presentation.ui.custom.dialog;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.databinding.DialogTripSelectionBinding;
import com.example.wanderfunmobile.domain.model.trips.Trip;
import com.example.wanderfunmobile.presentation.ui.adapter.trip.TripItemAdapter;
import com.example.wanderfunmobile.presentation.viewmodel.TripViewModel;

import java.util.ArrayList;
import java.util.List;

public class TripSelectionDialog extends Dialog {

    public interface OnTripSelectedListener {
        void onTripSelected(Trip trip);
    }

    private DialogTripSelectionBinding binding;
    private TripItemAdapter adapter;
    private final List<Trip> fullTripList = new ArrayList<>();
    private final List<Trip> filteredTripList = new ArrayList<>();
    private final OnTripSelectedListener listener;
    private final ObjectMapper objectMapper;

    public TripSelectionDialog(@NonNull Context context, OnTripSelectedListener listener, ObjectMapper objectMapper) {
        super(context);
        this.listener = listener;
        this.objectMapper = objectMapper;
        init(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DialogTripSelectionBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());

        // Dialog setup
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        setupDismissSpace();
        setupRecyclerView(context);
        setupSearchBar();

        TripViewModel tripViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(TripViewModel.class);
        tripViewModel.getAllTrips("Bearer " + SessionManager.getInstance(context.getApplicationContext()).getAccessToken());
        tripViewModel.getAllTripsResponseLiveData().observe((LifecycleOwner) context, result -> {
            if (!result.isError() && result.getData() != null) {
                fullTripList.clear();
                fullTripList.addAll(objectMapper.mapList(result.getData(), Trip.class));

                filteredTripList.clear();
                filteredTripList.addAll(fullTripList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupDismissSpace(){
        binding.dialog.setOnClickListener(v -> {
            dismiss(); // user tapped outside dialog
        });

        binding.innerContainer.setOnClickListener(v -> {
            // do nothing — prevents the click from propagating
        });
    }

    private void setupRecyclerView(Context context) {
        adapter = new TripItemAdapter(filteredTripList, trip -> {
            listener.onTripSelected(trip);
            dismiss();
        });
        binding.tripSelectionList.setLayoutManager(new LinearLayoutManager(context));
        binding.tripSelectionList.setAdapter(adapter);
    }

    private void setupSearchBar() {
        EditText searchInput = binding.searchBarLayout.findViewById(R.id.search_edittext);
        searchInput.setHint("Tìm kiếm chuyến đi");
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTrips(s.toString().trim());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterTrips(String keyword) {
        filteredTripList.clear();
        if (keyword.isEmpty()) {
            filteredTripList.addAll(fullTripList);
        } else {
            for (Trip trip : fullTripList) {
                if (trip.getName() != null && trip.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredTripList.add(trip);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
