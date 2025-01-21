package com.example.wanderfunmobile.infrastructure.ui.activity.trip;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityTripDetailBinding;
import com.example.wanderfunmobile.domain.model.Trip;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.viewmodel.TripViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class TripDetailActivity extends AppCompatActivity {

    private ActivityTripDetailBinding viewBinding;
    private TripViewModel tripViewModel;
    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityTripDetailBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ViewModel
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        // Get trip info
        Long tripId = getIntent().getLongExtra("tripId", -1);
        if (tripId != -1) {
            tripViewModel.getTripById("Bearer " + SessionManager.getInstance(getApplicationContext()), tripId);
        }


        // Back button
        viewBinding.backButton.setOnClickListener(v -> finish());

        // Header title

    }
}