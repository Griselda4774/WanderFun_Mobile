package com.example.wanderfunmobile.infrastructure.ui.activity.trip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityAddEditTripBinding;
import com.example.wanderfunmobile.domain.model.Trip;
import com.example.wanderfunmobile.presentation.viewmodel.TripViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

public class AddEditTripActivity extends AppCompatActivity {

    private ActivityAddEditTripBinding viewBinding;
    private Trip trip;
    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityAddEditTripBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getTripByIdResponseLiveData().observe(this, data -> {

        });

        // Back button
        viewBinding.backButton.setOnClickListener(v -> finish());

        // Get trip id from intent
        Intent intent = getIntent();
        Long tripId = intent.getLongExtra("tripId", -1L);

        // Header title
        if (tripId == -1L) {
            setAddPlaceActivityInfo();
        } else {
            setEditPlaceActivityInfo();

        }

        // Date picker
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Chọn ngày");
        builder.setTheme(R.style.CustomMaterialDatePicker);
        MaterialDatePicker<Long> datePicker = builder.build();
    }

    private void setAddPlaceActivityInfo() {
        // Header title
        viewBinding.headerTitle.setText("Tạo chuyến đi");

        // Image picker
        viewBinding.imagePicker.setVisibility(View.VISIBLE);
    }

    private void setEditPlaceActivityInfo() {
        viewBinding.headerTitle.setText("Chỉnh sửa chuyến đi");

    }
}