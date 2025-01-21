package com.example.wanderfunmobile.infrastructure.ui.fragment.trip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.databinding.FragmentAllTripBinding;
import com.example.wanderfunmobile.domain.model.Trip;
import com.example.wanderfunmobile.infrastructure.ui.adapter.trip.TripItemAdapter;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.TripViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AllTripFragment extends Fragment {

    FragmentAllTripBinding viewBinding;
    private TripViewModel tripViewModel;
    private List<Trip> tripList;

    @Inject
    ObjectMapper objectMapper;

    public AllTripFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentAllTripBinding.inflate(inflater, container, false);
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tripViewModel.getAllTrips("Bearer " + SessionManager.getInstance(requireActivity().getApplicationContext()).getAccessToken());
        tripViewModel.getAllTripsResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (!data.isError()) {
                tripList = objectMapper.mapList(data.getData(), Trip.class);
                showTripList();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }

    private void showTripList() {
        RecyclerView recyclerView = viewBinding.tripList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TripItemAdapter tripItemAdapter = new TripItemAdapter(tripList);
        recyclerView.setAdapter(tripItemAdapter);
    }
}