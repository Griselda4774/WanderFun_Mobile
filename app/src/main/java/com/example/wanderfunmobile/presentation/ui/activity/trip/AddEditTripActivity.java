package com.example.wanderfunmobile.presentation.ui.activity.trip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.data.dto.trip.TripCreateDto;
import com.example.wanderfunmobile.data.dto.tripplace.TripPlaceCreateDto;
import com.example.wanderfunmobile.data.dto.tripplace.TripPlaceDto;
import com.example.wanderfunmobile.databinding.ActivityAddEditTripBinding;
import com.example.wanderfunmobile.domain.model.trips.Trip;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;
import com.example.wanderfunmobile.presentation.ui.adapter.tripplace.TripPlaceItemAdapter;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.TripViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditTripActivity extends AppCompatActivity {

    private ActivityAddEditTripBinding binding;
    private Long tripId;
    private Trip trip;
    private boolean isCloned = false;
    private TripViewModel tripViewModel;
    private TripPlaceItemAdapter tripPlaceItemAdapter;
    private final List<TripPlace> tripPlaceList = new ArrayList<>();
    private ActivityResultLauncher<Intent> tripPlaceLauncher, editTripPlaceLauncher;
    private LoadingDialog loadingDialog;

    @Inject
    ObjectMapper objectMapper;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddEditTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViewModels();
        setupRecyclerView();
        setupHeader();
        setupButtonEvents();
        setupTripPlaceResultLauncher();
        setupTripPlaceUpdateResultLauncher();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initViewModels() {
        loadingDialog = binding.loadingDialog;
        hideLoadingDialog();

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        tripViewModel.getTripByIdResponseLiveData().observe(this, response -> {
            if (response != null && !response.isError() && response.getData() != null) {
                trip = objectMapper.map(response.getData(), Trip.class);
                List<TripPlace> places = objectMapper.mapList(response.getData().getTripPlaceList(), TripPlace.class);
                tripPlaceList.clear();
                tripPlaceList.addAll(places);
                setTripInfo();
                tripPlaceItemAdapter.notifyDataSetChanged();
            }
        });

        tripViewModel.createTripResponseLiveData().observe(this, response -> {
            hideLoadingDialog();
            showToast(response.isError() ? "Tạo chuyến đi thất bại" : "Tạo chuyến đi thành công");
            if (!response.isError()) finish();
        });

        tripViewModel.updateTripByIdResponseLiveData().observe(this, response -> {
            hideLoadingDialog();
            showToast(response.isError() ? "Cập nhật chuyến đi thất bại" : "Cập nhật chuyến đi thành công");
            if (!response.isError()) finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void setupHeader() {
        ConstraintLayout backButton = binding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> finish());

        tripId = getIntent().getLongExtra("tripId", 0);
        isCloned = getIntent().getBooleanExtra("isCloned", false);
        if (tripId == 0 || isCloned) {
            binding.headerTitle.setText("Tạo chuyến đi");
        } else {
            binding.headerTitle.setText("Chỉnh sửa chuyến đi");
        }
        if (tripId != 0) {
            String token = "Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken();
            tripViewModel.getTripById(token, tripId);
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.tripPlaceList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tripPlaceItemAdapter = new TripPlaceItemAdapter(tripPlaceList, objectMapper, (tripPlace, position) -> {
            Intent intent = new Intent(this, TripPlaceCreateActivity.class);
            intent.putExtra("is_edit", true);
            intent.putExtra("selected_place", Parcels.wrap(objectMapper.map(tripPlace, TripPlaceDto.class)));
            intent.putExtra("position", position);
            editTripPlaceLauncher.launch(intent);
        });
        recyclerView.setAdapter(tripPlaceItemAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupTripPlaceResultLauncher() {
        tripPlaceLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                TripPlace createdPlace =  objectMapper.map(Parcels.unwrap(result.getData().getParcelableExtra("created_place")), TripPlace.class);
                tripPlaceList.add(createdPlace);
                tripPlaceItemAdapter.notifyDataSetChanged();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupTripPlaceUpdateResultLauncher() {
        editTripPlaceLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        TripPlace updatedPlace = objectMapper.map(Parcels.unwrap(result.getData().getParcelableExtra("updated_place")), TripPlace.class);
                        int position = result.getData().getIntExtra("position", -1);
                        if (position != -1 && updatedPlace != null) {
                            tripPlaceList.set(position, updatedPlace);
                            tripPlaceItemAdapter.notifyItemChanged(position);
                        }
                    }
                }
        );
    }

    @SuppressLint("SetTextI18n")
    private void setupButtonEvents() {
        TextView addPlaceButton = binding.addPlaceButton.findViewById(R.id.button);
        addPlaceButton.setText("Thêm địa điểm");
        addPlaceButton.setPadding(
                addPlaceButton.getPaddingLeft(),
                getResources().getDimensionPixelSize(R.dimen.button_vertical_padding_small),
                addPlaceButton.getPaddingRight(),
                getResources().getDimensionPixelSize(R.dimen.button_vertical_padding_small)
        );

        addPlaceButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TripPlaceCreateActivity.class);
            if (!tripPlaceList.isEmpty()) {
                String limitStart = DateTimeUtil.localDateToString(tripPlaceList.get(tripPlaceList.size() - 1).getEndTime());
                intent.putExtra("limit_start_time", limitStart);
            }
            tripPlaceLauncher.launch(intent);
        });

        EditText tripNameInput = binding.name.findViewById(R.id.text_edittext);
        tripNameInput.setHint("Nhập tên chuyến đi");

        TextView cancelButton = binding.cancelButton.findViewById(R.id.button);
        cancelButton.setText("Hủy");
        cancelButton.setOnClickListener(v -> finish());

        TextView confirmButton = binding.confirmButton.findViewById(R.id.button);
        confirmButton.setText(tripId == 0 ? "Tạo" : "Lưu");
        confirmButton.setOnClickListener(v -> {
            showLoadingDialog();
            TripCreateDto dto = new TripCreateDto();
            dto.setName(tripNameInput.getText().toString());
            List<TripPlaceCreateDto> places = new ArrayList<>(objectMapper.mapList(tripPlaceList, TripPlaceCreateDto.class));
            for (int i = 0; i < places.size(); i++) {
                places.get(i).setPlaceId(tripPlaceList.get(i).getPlace().getId());
            }
            dto.setTripPlaceList(places);
            doAddOrUpdate(dto);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setTripInfo() {
        if (trip != null && trip.getName() != null) {
            EditText nameInput = binding.name.findViewById(R.id.text_edittext);
            nameInput.setText(trip.getName());
            tripPlaceItemAdapter.setEditMode(true);
        }
    }

    private void showLoadingDialog() {
        loadingDialog.setVisibility(View.VISIBLE);
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        loadingDialog.setVisibility(View.GONE);
        loadingDialog.hide();
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void doAddOrUpdate(TripCreateDto tripDto) {
        String token = "Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken();
        if (tripId == 0 || isCloned) {
            tripViewModel.createTrip(token, tripDto);
        } else {
            tripViewModel.updateTripById(token, tripId, tripDto);
        }
    }
}
