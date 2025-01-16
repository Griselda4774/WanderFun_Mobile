package com.example.wanderfunmobile.infrastructure.ui.activity.album;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityAddEditAlbumBinding;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditAlbumActivity extends AppCompatActivity {
    ActivityAddEditAlbumBinding viewBinding;
    AlbumViewModel albumViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = ActivityAddEditAlbumBinding.inflate(getLayoutInflater());
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_album);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}