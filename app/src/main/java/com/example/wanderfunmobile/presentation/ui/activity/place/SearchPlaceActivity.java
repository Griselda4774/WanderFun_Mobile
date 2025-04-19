package com.example.wanderfunmobile.presentation.ui.activity.place;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.example.wanderfunmobile.data.dto.place.PlaceDto;
import com.example.wanderfunmobile.databinding.ActivitySearchPlaceBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.adapter.place.PlaceItemAdapter;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchPlaceActivity extends AppCompatActivity {
    @Inject
    ObjectMapper objectMapper;
    private List<Place> placeList = new ArrayList<>();
    private ActivitySearchPlaceBinding viewBinding;
    private PlaceViewModel placeViewModel;
    private PlaceItemAdapter placeItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivitySearchPlaceBinding.inflate(getLayoutInflater());
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText searchPlace = viewBinding.searchBarLayout.findViewById(R.id.search_edittext);
        searchPlace.setHint("Tìm kiếm địa điểm");

        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        searchPlace.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Handle the search action
                    String query = searchPlace.getText().toString();
                    performSearch(query);
                    return true; // Consume the event
                }
                return false;
            }
        });
    }

    private void performSearch(String query) {
        // Implement your search logic here
        placeViewModel.searchPlacesByNameContaining(query);
        placeViewModel.searchPlacesByNameContainingResponseLiveData().observe(this, response -> {
            if (!response.isError()) {
                List<PlaceDto> placeListDto = response.getData();
                if (placeListDto.isEmpty()) {
                    placeListDto = new ArrayList<>();
                }
                placeList = objectMapper.mapList(placeListDto, Place.class);
                placeItemAdapter = new PlaceItemAdapter(placeList, this);

                RecyclerView recyclerView = viewBinding.searchPlaceList;
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(placeItemAdapter);
            } else {
                Toast.makeText(this, "Không tìm thấy được địa điểm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}