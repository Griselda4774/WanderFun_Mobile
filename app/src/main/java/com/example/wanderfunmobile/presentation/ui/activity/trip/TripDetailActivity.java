package com.example.wanderfunmobile.presentation.ui.activity.trip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityTripDetailBinding;
import com.example.wanderfunmobile.domain.model.trips.Trip;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;
import com.example.wanderfunmobile.presentation.ui.adapter.tripplace.TripPlaceItemAdapter;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.SelectionDialog;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.TripViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class TripDetailActivity extends AppCompatActivity {

    private ActivityTripDetailBinding viewBinding;
    private TripViewModel tripViewModel;
    private Trip trip;
    private List<TripPlace> tripPlaceList = new ArrayList<>();
    private TripPlaceItemAdapter tripPlaceItemAdapter;
    @Inject
    ObjectMapper objectMapper;
    private LoadingDialog loadingDialog;
    private SelectionDialog selectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityTripDetailBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ViewModel
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getTripByIdResponseLiveData().observe(this, data -> {
            if (data != null && !data.isError()) {
                trip = objectMapper.map(data.getData(), Trip.class);
                setInfo();
            }
        });
        tripViewModel.deleteTripByIdResponseLiveData().observe(this, data -> {
            if (data != null && !data.isError()) {
                Toast.makeText(getApplicationContext(), "Xóa chuyến đi thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Xóa chuyến đi thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        // Get trip info
        Long tripId = getIntent().getLongExtra("tripId", -1);
        if (tripId != -1) {
            tripViewModel.getTripById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), tripId);
        }


        // Back button
        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Selection dialog
        selectionDialog = viewBinding.selectionDialog;
        selectionDialog.hide();
        selectionDialog.setVisibility(View.GONE);
        selectionDialog.setOnAcceptListener(() -> {
            tripViewModel.deleteTripById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), trip.getId());
            selectionDialog.hide();
            Log.d("SelectionDialog", "Accept");
        });

        selectionDialog.setOnRejectListener(() -> {
            selectionDialog.hide();
            Log.d("SelectionDialog", "Reject");
        });

        // Loading dialog
        loadingDialog = viewBinding.loadingDialog;
        tripViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                loadingDialog.show();
                loadingDialog.setVisibility(View.VISIBLE);
            } else {
                loadingDialog.hide();
                loadingDialog.setVisibility(View.GONE);
            }
        });

        RecyclerView recyclerView = viewBinding.tripPlaceList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripPlaceItemAdapter = new TripPlaceItemAdapter(tripPlaceList);
        recyclerView.setAdapter(tripPlaceItemAdapter);

        // Delete button
        TextView deleteButton = viewBinding.deleteButton.findViewById(R.id.button);
        deleteButton.setText("Xóa");
        deleteButton.setOnClickListener(v -> {
            selectionDialog.setVisibility(View.VISIBLE);
            selectionDialog.show("Xóa chuyến đi",
                    "Bạn có chắc chắn muốn xóa chuyến đi này?",
                    "Thao tác này không thể hoàn tác",
                    "Xóa",
                    "Hủy");
        });

        // Edit button
        TextView editButton = viewBinding.editButton.findViewById(R.id.button);
        editButton.setText("Sửa");
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTripActivity.class);
            intent.putExtra("tripId", trip.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (trip != null) {
            tripViewModel.getTripById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), trip.getId());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setInfo() {
        if (trip != null && trip.getName() != null) {
            viewBinding.name.setText(trip.getName());
        }

        if (trip != null && trip.getStartTime() != null) {
            viewBinding.startTime.setText(DateTimeUtil.localDateToString(trip.getStartTime()));
        }

        if (trip != null && trip.getEndTime() != null) {
            viewBinding.endTime.setText(DateTimeUtil.localDateToString(trip.getEndTime()));
        }

        if (trip != null && !trip.getTripPlaceList().isEmpty()) {
            tripPlaceList.clear();
            tripPlaceList.addAll(trip.getTripPlaceList());
            tripPlaceItemAdapter.setEditMode(false);
            tripPlaceItemAdapter.notifyDataSetChanged();
        }

//        if (trip != null && !trip.getImageUrl().isEmpty()) {
//            Glide.with(this).load(trip.getImageUrl()).into(viewBinding.image);
//        }
    }
}