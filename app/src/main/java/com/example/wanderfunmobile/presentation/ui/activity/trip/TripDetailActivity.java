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

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.data.dto.trip.TripDto;
import com.example.wanderfunmobile.databinding.ActivityTripDetailBinding;
import com.example.wanderfunmobile.domain.model.trips.Trip;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;
import com.example.wanderfunmobile.presentation.ui.activity.post.AddEditPostActivity;
import com.example.wanderfunmobile.presentation.ui.adapter.tripplace.TripPlaceItemAdapter;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.SelectionDialog;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.TripViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TripDetailActivity extends AppCompatActivity {

    private ActivityTripDetailBinding binding;
    private TripViewModel tripViewModel;
    private Trip trip;
    private final List<TripPlace> tripPlaceList = new ArrayList<>();
    private TripPlaceItemAdapter tripPlaceItemAdapter;

    @Inject ObjectMapper objectMapper;

    private LoadingDialog loadingDialog;
    private SelectionDialog selectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityTripDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViewModel();
        setupDialogs();
        setupRecyclerView();
        handleIntentData();
        setupUIEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (trip != null) {
            fetchTripById(trip.getId());
        }
    }

    //region Setup Methods
    private void initViewModel() {
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        tripViewModel.getTripByIdResponseLiveData().observe(this, response -> {
            if (response != null && !response.isError()) {
                trip = objectMapper.map(response.getData(), Trip.class);
                updateTripInfo();
            }
        });

        tripViewModel.deleteTripByIdResponseLiveData().observe(this, response -> {
            String message = (response != null && !response.isError())
                    ? "Xóa chuyến đi thành công"
                    : "Xóa chuyến đi thất bại";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if (response != null && !response.isError()) finish();
        });

        tripViewModel.getIsLoading().observe(this, isLoading -> {
            loadingDialog.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) loadingDialog.show();
            else loadingDialog.hide();
        });
    }

    private void setupDialogs() {
        loadingDialog = binding.loadingDialog;

        selectionDialog = binding.selectionDialog;
        selectionDialog.hide();
        selectionDialog.setVisibility(View.GONE);

        selectionDialog.setOnAcceptListener(() -> {
            fetchTripDelete(trip.getId());
            selectionDialog.hide();
        });

        selectionDialog.setOnRejectListener(selectionDialog::hide);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.tripPlaceList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripPlaceItemAdapter = new TripPlaceItemAdapter(tripPlaceList, objectMapper, null);
        recyclerView.setAdapter(tripPlaceItemAdapter);
    }

    private void handleIntentData() {
        Long tripId = getIntent().getLongExtra("tripId", -1);

        if (tripId != -1) {
            fetchTripById(tripId);
        }
    }

    private void setupUIEvents() {
        binding.backButton.findViewById(R.id.button).setOnClickListener(v -> finish());

        binding.mapViewButton.findViewById(R.id.button).setOnClickListener(v ->
                Toast.makeText(this, "Tạm thời cứ thế thôi hẹ hẹ hẹ", Toast.LENGTH_SHORT).show());

        binding.deleteButton.findViewById(R.id.button).setOnClickListener(v ->
                selectionDialog.show("Xóa chuyến đi",
                        "Bạn có chắc chắn muốn xóa chuyến đi này?",
                        "Thao tác này không thể hoàn tác",
                        "Xóa",
                        "Hủy"));

        binding.editButton.findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTripActivity.class);
            intent.putExtra("tripId", trip.getId());
            startActivity(intent);
        });

        binding.cloneButton.findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTripActivity.class);
            intent.putExtra("tripId", trip.getId());
            intent.putExtra("isCloned", true);
            startActivity(intent);
        });

        binding.shareButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditPostActivity.class);
            TripDto tripDto = objectMapper.map(trip, TripDto.class);
            intent.putExtra("shared_trip", Parcels.wrap(tripDto));
            startActivity(intent);
        });
    }

    //endregion

    //region Business Logic

    private void fetchTripById(Long tripId) {
        String token = "Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken();
        tripViewModel.getTripById(token, tripId);
    }

    private void fetchTripDelete(Long tripId) {
        String token = "Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken();
        tripViewModel.deleteTripById(token, tripId);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void updateTripInfo() {
        if (trip == null) return;

        TextView editButton = binding.editButton.findViewById(R.id.button);
        editButton.setText("Chỉnh sửa chuyến đi");
        TextView deleteButton = binding.deleteButton.findViewById(R.id.button);
        deleteButton.setText("Xóa chuyến đi");
        TextView cloneButton = binding.cloneButton.findViewById(R.id.button);
        cloneButton.setText("Sao chép chuyến đi");
        TextView mapViewButton = binding.mapViewButton.findViewById(R.id.button);
        mapViewButton.setText("Xem trên bản đồ");

        binding.name.setText(trip.getName());
        binding.startTime.setText(DateTimeUtil.localDateToString(trip.getStartTime()));
        binding.endTime.setText(DateTimeUtil.localDateToString(trip.getEndTime()));

        tripPlaceList.clear();
        tripPlaceList.addAll(trip.getTripPlaceList());
        tripPlaceItemAdapter.setEditMode(false);
        tripPlaceItemAdapter.notifyDataSetChanged();

        boolean isOwner = Objects.equals(trip.getUserId(), SessionManager.getInstance(this).getUserId());
        binding.editButton.setVisibility(isOwner ? View.VISIBLE : View.GONE);
        binding.deleteButton.setVisibility(isOwner ? View.VISIBLE : View.GONE);
        binding.cloneButton.setVisibility(isOwner ? View.GONE : View.VISIBLE);
    }

    //endregion
}
