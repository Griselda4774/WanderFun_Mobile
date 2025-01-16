package com.example.wanderfunmobile.infrastructure.ui.activity.album;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityAlbumDetailsBinding;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlbumDetailsActivity extends AppCompatActivity {

    private ActivityAlbumDetailsBinding viewBinding;
    private AlbumViewModel albumViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityAlbumDetailsBinding.inflate(getLayoutInflater());
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);

        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        albumViewModel.getAlbumById("Bearer " + SessionManager.getInstance(this).getAccessToken(), getIntent().getLongExtra("albumId", 0));

        TextView deleteButton = viewBinding.deleteButton.findViewById(R.id.button);
        deleteButton.setText("Delete");

        TextView editButton = viewBinding.editButton.findViewById(R.id.button);
        editButton.setText("Edit");

        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

    }
}