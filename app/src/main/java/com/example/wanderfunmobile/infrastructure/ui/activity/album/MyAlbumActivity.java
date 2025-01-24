package com.example.wanderfunmobile.infrastructure.ui.activity.album;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.application.dto.album.AlbumDto;
import com.example.wanderfunmobile.databinding.ActivityMyAlbumsBinding;
import com.example.wanderfunmobile.domain.model.Album;
import com.example.wanderfunmobile.infrastructure.ui.adapter.album.AlbumItemAdapter;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyAlbumActivity extends AppCompatActivity {
    List<Album> albumList = new ArrayList<>();
    @Inject
    ObjectMapper objectMapper;
    private ActivityMyAlbumsBinding viewBinding;
    private AlbumItemAdapter albumItemAdapter;
    private AlbumViewModel albumViewModel;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMyAlbumsBinding.inflate(getLayoutInflater());
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);

        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = viewBinding.albumCardList.findViewById(R.id.album_card_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        albumItemAdapter = new AlbumItemAdapter(albumList);
        recyclerView.setAdapter(albumItemAdapter);

        fetchAlbumData();

        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        LinearLayout addAlbumButton = viewBinding.footer.findViewById(R.id.button);
        addAlbumButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditAlbumActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAlbumData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchAlbumData() {
        albumViewModel.getAllAlbums("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());
        albumViewModel.getAllAlbumsResponseLiveData().observe(this, albumResponseDto -> {
            if (!albumResponseDto.isError()) {
                List<AlbumDto> albumListDto = albumResponseDto.getData();
                if (albumListDto.isEmpty()) {
                    albumListDto = new ArrayList<>();
                }
                albumList.clear();
                albumList.addAll(objectMapper.mapList(albumListDto, Album.class));
                albumItemAdapter.notifyDataSetChanged();
            }
        });
    }
}
