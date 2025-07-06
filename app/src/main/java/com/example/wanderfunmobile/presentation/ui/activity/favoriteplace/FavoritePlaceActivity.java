package com.example.wanderfunmobile.presentation.ui.activity.favoriteplace;

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

import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.ActivityFavoritePlaceBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.adapter.place.PlaceItemAdapter;
import com.example.wanderfunmobile.presentation.viewmodel.FavoritePlaceViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavoritePlaceActivity extends AppCompatActivity {
    private ActivityFavoritePlaceBinding viewBinding;
    private final List<Place> favoritePlaceList = new ArrayList<>();
    private PlaceItemAdapter placeItemAdapter;
    private FavoritePlaceViewModel favoritePlaceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityFavoritePlaceBinding.inflate(getLayoutInflater());
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
        favoritePlaceViewModel = new ViewModelProvider(this).get(FavoritePlaceViewModel.class);
        favoritePlaceViewModel.getFindAllByUserLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                if (result.getData().isEmpty()) {
                    viewBinding.noFavoritePlaceText.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.noFavoritePlaceText.setVisibility(View.GONE);
                    favoritePlaceList.clear();
                    favoritePlaceList.addAll(result.getData());
                    placeItemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setUpAdapter() {
        placeItemAdapter = new PlaceItemAdapter(favoritePlaceList);
        viewBinding.favoritePlaceList.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.favoritePlaceList.setAdapter(placeItemAdapter);
    }

    private void setUpActivity() {
        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });
    }

    private void fetchData() {
        favoritePlaceViewModel.findAllByUser("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());
    }
}