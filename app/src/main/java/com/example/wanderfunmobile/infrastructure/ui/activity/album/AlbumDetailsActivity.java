package com.example.wanderfunmobile.infrastructure.ui.activity.album;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.application.dto.albumimage.AlbumImageDto;
import com.example.wanderfunmobile.databinding.ActivityAlbumDetailsBinding;
import com.example.wanderfunmobile.domain.model.Album;
import com.example.wanderfunmobile.domain.model.AlbumImage;
import com.example.wanderfunmobile.infrastructure.ui.adapter.album.AlbumImageAdapter;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlbumDetailsActivity extends AppCompatActivity {
    @Inject
    ObjectMapper objectMapper;
    AlbumImageAdapter albumImageAdapter;
    private Album album;
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

        RecyclerView recyclerView = viewBinding.albumImageList.findViewById(R.id.album_image_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        albumViewModel.getAlbumById("Bearer " + SessionManager.getInstance(this).getAccessToken(), getIntent().getLongExtra("albumId", 0));
        albumViewModel.getAlbumByIdResponseLiveData().observe(this, albumResponseDto -> {
            if (!albumResponseDto.isError()) {
                List<AlbumImageDto> albumImageListDto = albumResponseDto.getData().getAlbumImages();
                album = objectMapper.map(albumResponseDto.getData(), Album.class);
                album.setAlbumImages(objectMapper.mapList(albumImageListDto, AlbumImage.class));

                albumImageAdapter = new AlbumImageAdapter(album.getAlbumImages());
                recyclerView.setAdapter(albumImageAdapter);

                viewBinding.headerTitle.setText(albumResponseDto.getData().getName());
                viewBinding.description.setText(albumResponseDto.getData().getDescription());
                viewBinding.placeName.setText(albumResponseDto.getData().getPlaceName());

            }
        });

        TextView deleteButton = viewBinding.deleteButton.findViewById(R.id.button);
        deleteButton.setText("Xóa");
        deleteButton.setOnClickListener(v -> {
            albumViewModel.deleteAlbumById("Bearer " + SessionManager.getInstance(this).getAccessToken(), album.getId());
            albumViewModel.deleteAlbumResponseLiveData().observe(this, response -> {
                if (!response.isError()) {
                    Toast.makeText(this, "Xóa album thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(this, "Xóa album thất bại", Toast.LENGTH_SHORT).show();
            });
            finish();
        });

        TextView editButton = viewBinding.editButton.findViewById(R.id.button);
        editButton.setText("Chỉnh sửa");
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditAlbumActivity.class);
            intent.putExtra("albumId", album.getId());
            startActivity(intent);
            finish();
        });

        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }
}