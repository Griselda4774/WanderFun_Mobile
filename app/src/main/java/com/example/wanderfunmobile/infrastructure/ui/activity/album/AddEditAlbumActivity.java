package com.example.wanderfunmobile.infrastructure.ui.activity.album;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.application.dto.album.AlbumCreateDto;
import com.example.wanderfunmobile.databinding.ActivityAddEditAlbumBinding;
import com.example.wanderfunmobile.domain.model.Album;
import com.example.wanderfunmobile.infrastructure.ui.activity.place.SearchPlaceActivity;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditAlbumActivity extends AppCompatActivity {
    AlbumCreateDto albumCreateDto;
    private Album album;
    private ActivityAddEditAlbumBinding viewBinding;
    private AlbumViewModel albumViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String placeName = getIntent().getStringExtra("selected_place");

        Long placeId = getIntent().getLongExtra("selected_place_id", 0);

        viewBinding = ActivityAddEditAlbumBinding.inflate(getLayoutInflater());
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        EditText albumName = viewBinding.albumNameLayout.findViewById(R.id.text_edittext);

        EditText albumDescription = viewBinding.albumDescriptionLayout.findViewById(R.id.content_edittext);

        EditText albumPlace = viewBinding.albumPlaceLayout.findViewById(R.id.content_edittext);
        if (placeName != null) {
            albumPlace.setText(placeName);
        }

        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        TextView searchPlaceButton = viewBinding.searchPlaceButton.findViewById(R.id.button);
        searchPlaceButton.setText("Chọn");
        searchPlaceButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchPlaceActivity.class);
            startActivity(intent);
        });

        TextView cancelButton = viewBinding.cancelButton.findViewById(R.id.button);
        cancelButton.setText("Hủy bỏ");
        cancelButton.setOnClickListener(v -> {
            finish();
        });

        TextView saveButton = viewBinding.saveButton.findViewById(R.id.button);
        saveButton.setText("Lưu");
        saveButton.setOnClickListener(v -> {
            //albumViewModel.createAlbum("Bearer " + SessionManager.getInstance(this).getAccessToken(), );
        });
    }

    
}