package com.example.wanderfunmobile.presentation.ui.activity.album;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.wanderfunmobile.data.dto.album.AlbumImageDto;
import com.example.wanderfunmobile.databinding.ActivityAlbumDetailsBinding;
import com.example.wanderfunmobile.domain.model.albums.Album;
import com.example.wanderfunmobile.domain.model.albums.AlbumImage;
import com.example.wanderfunmobile.presentation.ui.adapter.album.AlbumImageItemAdapter;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.SelectionDialog;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlbumDetailsActivity extends AppCompatActivity {
    @Inject
    ObjectMapper objectMapper;
    AlbumImageItemAdapter albumImageItemAdapter;
    private Album album;
    private ActivityAlbumDetailsBinding viewBinding;
    private AlbumViewModel albumViewModel;

    private LoadingDialog loadingDialog;
    private SelectionDialog selectionDialog;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityAlbumDetailsBinding.inflate(getLayoutInflater());
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);

        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = viewBinding.albumImageList.findViewById(R.id.album_image_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));


        albumViewModel.getAlbumById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), getIntent().getLongExtra("albumId", 0));
        albumViewModel.getAlbumByIdResponseLiveData().observe(this, albumResponseDto -> {
            if (!albumResponseDto.isError()) {
                List<AlbumImageDto> albumImageListDto = albumResponseDto.getData().getAlbumImageList();
                album = objectMapper.map(albumResponseDto.getData(), Album.class);
                album.setAlbumImageList(objectMapper.mapList(albumImageListDto, AlbumImage.class));

                if(album.getAlbumImageList().isEmpty()) {
                    viewBinding.albumImageList.setVisibility(View.GONE);
                } else {
                    albumImageItemAdapter = new AlbumImageItemAdapter(album.getAlbumImageList());
                    recyclerView.setAdapter(albumImageItemAdapter);
                }

                viewBinding.headerTitle.setText(albumResponseDto.getData().getName());
                viewBinding.description.setText(albumResponseDto.getData().getDescription());
                viewBinding.placeName.setText(albumResponseDto.getData().getPlace().getName());

            }
        });

        albumViewModel.deleteAlbumResponseLiveData().observe(this, response -> {
            if (!response.isError()) {
                Toast.makeText(getApplicationContext(), "Xóa album thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Xóa album thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        selectionDialog = viewBinding.selectionDialog;
        selectionDialog.hide();
        selectionDialog.setVisibility(View.GONE);
        selectionDialog.setOnAcceptListener(() -> {
            selectionDialog.hide();
            Log.d("SelectionDialog", "Reject");
        });

        selectionDialog.setOnRejectListener(() -> {
            albumViewModel.deleteAlbumById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), album.getId());
            selectionDialog.hide();
            Log.d("SelectionDialog", "Accept");
        });

        // Loading dialog
        loadingDialog = new LoadingDialog(this);
        albumViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                loadingDialog.show();
            } else {
                loadingDialog.hide();
            }
        });

        TextView deleteButton = viewBinding.deleteButton.findViewById(R.id.button);
        deleteButton.setText("Xóa");
        deleteButton.setOnClickListener(v -> {
            selectionDialog.setVisibility(View.VISIBLE);
            selectionDialog.show("Xóa album",
                    "Bạn có chắc chắn muốn xóa album này?",
                    "Thao tác này không thể hoàn tác",
                    "Hủy",
                    "Vẫn xóa");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}