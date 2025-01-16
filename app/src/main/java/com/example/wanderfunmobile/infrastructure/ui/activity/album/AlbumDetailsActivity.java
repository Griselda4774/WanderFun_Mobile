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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.application.dto.albumimage.AlbumImageDto;
import com.example.wanderfunmobile.databinding.ActivityAlbumDetailsBinding;
import com.example.wanderfunmobile.domain.model.AlbumImage;
import com.example.wanderfunmobile.infrastructure.ui.adapter.album.AlbumImageAdapter;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlbumDetailsActivity extends AppCompatActivity {
    List<AlbumImage> albumList = new ArrayList<>();
    @Inject
    ObjectMapper objectMapper;
    AlbumImageAdapter albumImageAdapter;
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

        TextView deleteButton = viewBinding.deleteButton.findViewById(R.id.button);
        deleteButton.setText("Xóa");
        deleteButton.setOnClickListener(v -> {
            //albumViewModel.deleteAlbumById("Bearer " + SessionManager.getInstance(this).getAccessToken(), getIntent().getLongExtra("albumId", 0));
            finish();
        });

        TextView editButton = viewBinding.editButton.findViewById(R.id.button);
        editButton.setText("Chỉnh sửa");

        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });


        albumViewModel.getAlbumById("Bearer " + SessionManager.getInstance(this).getAccessToken(), getIntent().getLongExtra("albumId", 0));
        albumViewModel.getAlbumByIdResponseLiveData().observe(this, albumResponseDto -> {
            if (!albumResponseDto.isError()) {
                List<AlbumImageDto> albumListDto = albumResponseDto.getData().getAlbumImages();

                albumList = objectMapper.mapList(albumListDto, AlbumImage.class);


                albumImageAdapter = new AlbumImageAdapter(albumList);
                recyclerView.setAdapter(albumImageAdapter);

                viewBinding.headerTitle.setText(albumResponseDto.getData().getName());
                viewBinding.description.setText(albumResponseDto.getData().getDescription());
                viewBinding.placeName.setText(albumResponseDto.getData().getPlaceName());

            }

        });
    }
}