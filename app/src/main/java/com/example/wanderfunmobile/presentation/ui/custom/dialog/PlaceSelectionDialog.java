package com.example.wanderfunmobile.presentation.ui.custom.dialog;

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
import com.example.wanderfunmobile.databinding.DialogPlaceSelectionBinding;
import com.example.wanderfunmobile.domain.model.checkins.CheckIn;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.adapter.place.PlaceItemAdapter;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnPlaceSelectedListener;
import com.example.wanderfunmobile.presentation.viewmodel.CheckInViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlaceSelectionDialog extends Dialog {

    private DialogPlaceSelectionBinding binding;
    private PlaceItemAdapter adapter;
    private final List<Place> fullPlaceList = new ArrayList<>();
    private final List<Place> filteredPlaceList = new ArrayList<>();
    private final OnPlaceSelectedListener listener;
    private final ObjectMapper objectMapper;

    public PlaceSelectionDialog(@NonNull Context context, OnPlaceSelectedListener listener, ObjectMapper objectMapper) {
        super(context);
        this.listener = listener;
        this.objectMapper = objectMapper;
        init(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DialogPlaceSelectionBinding.inflate(LayoutInflater.from(context));
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

        CheckInViewModel checkInViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CheckInViewModel.class);
        checkInViewModel.findAllByUser("Bearer " + SessionManager.getInstance(context.getApplicationContext()).getAccessToken());
        checkInViewModel.getFindAllByUserLiveData().observe((LifecycleOwner) context, result -> {
            if (!result.isError() && result.getData() != null) {

                List<CheckIn> checkIns = objectMapper.mapList(result.getData(), CheckIn.class);
                fullPlaceList.clear();
                List<Place> list = new ArrayList<>();
                for (CheckIn checkIn : checkIns) {
                    Place place = checkIn.getPlace();
                    list.add(place);
                }
                fullPlaceList.addAll(list);

                filteredPlaceList.clear();
                filteredPlaceList.addAll(fullPlaceList);
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
        adapter = new PlaceItemAdapter(filteredPlaceList);
        adapter.setOnPlaceSelectedListener(place -> {
            if (listener != null) {
                listener.onPlaceSelected(place);
            }
            dismiss();
        });
        binding.placeSelectionList.setLayoutManager(new LinearLayoutManager(context));
        binding.placeSelectionList.setAdapter(adapter);
    }

    private void setupSearchBar() {
        EditText searchInput = binding.searchBarLayout.findViewById(R.id.search_edittext);
        searchInput.setHint("Tìm kiếm chuyến đi");
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPlaces(s.toString().trim());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterPlaces(String keyword) {
        filteredPlaceList.clear();
        if (keyword.isEmpty()) {
            filteredPlaceList.addAll(fullPlaceList);
        } else {
            for (Place place : fullPlaceList) {
                if (place.getName() != null && place.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredPlaceList.add(place);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
