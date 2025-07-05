package com.example.wanderfunmobile.presentation.ui.activity.checkin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.ActivityCheckInHistoryBinding;
import com.example.wanderfunmobile.domain.model.checkins.CheckIn;
import com.example.wanderfunmobile.presentation.ui.adapter.checkin.CheckInItemAdapter;
import com.example.wanderfunmobile.presentation.viewmodel.CheckInViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CheckInHistoryActivity extends AppCompatActivity {
    private ActivityCheckInHistoryBinding viewBinding;
    private final List<CheckIn> checkInList = new ArrayList<>();
    private CheckInItemAdapter checkInItemAdapter;
    private CheckInViewModel checkInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityCheckInHistoryBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpViewModel();
        setUpAdapter();
        setUpActivity();
        fetchData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpViewModel() {
        checkInViewModel = new ViewModelProvider(this).get(CheckInViewModel.class);
        checkInViewModel.getFindAllByUserLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                if (result.getData().isEmpty()) {
                    viewBinding.noCheckInText.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.noCheckInText.setVisibility(View.GONE);
                    checkInList.clear();
                    checkInList.addAll(result.getData());
                    checkInItemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setUpAdapter() {
        checkInItemAdapter = new CheckInItemAdapter(checkInList);
        viewBinding.checkInList.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.checkInList.setAdapter(checkInItemAdapter);
    }

    private void setUpActivity() {
        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });
    }

    private void fetchData() {
        checkInViewModel.findAllByUser("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());
    }
}