package com.example.wanderfunmobile.presentation.ui.activity.album;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.data.dto.album.AlbumCreateDto;
import com.example.wanderfunmobile.data.dto.album.AlbumDto;
import com.example.wanderfunmobile.data.dto.album.AlbumImageDto;
import com.example.wanderfunmobile.data.dto.cloudinary.CloudinaryImageDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.databinding.ActivityAddEditAlbumBinding;
import com.example.wanderfunmobile.domain.model.albums.Album;
import com.example.wanderfunmobile.domain.model.albums.AlbumImage;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.activity.place.SearchPlaceActivity;
import com.example.wanderfunmobile.presentation.ui.adapter.ImageWithDeleteAdapter;
import com.example.wanderfunmobile.core.util.CloudinaryUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditAlbumActivity extends AppCompatActivity {
    private ActivityAddEditAlbumBinding viewBinding;
    private AlbumCreateDto albumCreateDto = new AlbumCreateDto();
    private List<Uri> imageList = new ArrayList<>();
    private Uri coverImageUri;
    private Album album;
    private Long albumId;
    private Place selectedPlace;
    private AlbumViewModel albumViewModel;
    private PlaceViewModel placeViewModel;
    private ImageWithDeleteAdapter imageWithDeleteAdapter;
    private ActivityResultLauncher<PickVisualMediaRequest> pickAlbumImages, pickCoverImage;
    private ActivityResultLauncher<Intent> placePickerLauncher;
    private LoadingDialog loadingDialog;
    @Inject
    ObjectMapper objectMapper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = ActivityAddEditAlbumBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText albumName = viewBinding.albumNameLayout.findViewById(R.id.text_edittext);

        EditText albumDescription = viewBinding.albumDescriptionLayout.findViewById(R.id.content_edittext);

        RecyclerView recyclerView = viewBinding.albumImageList.findViewById(R.id.album_image_list);

        initViewModels();
        setupHeader();
        setupRecyclerView(recyclerView);
        setUpButtonEvents();
        setupPickMultipleMedia();

        if (albumId != 0) {
            albumViewModel.getAlbumById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), albumId);
            albumViewModel.getAlbumByIdResponseLiveData().observe(this, response -> {
                if (!response.isError()) {
                    AlbumDto albumDto = response.getData();
                    albumName.setText(albumDto.getName());
                    albumDescription.setText(albumDto.getDescription());
                    selectedPlace = objectMapper.map(albumDto.getPlace(), Place.class);
                    viewBinding.placeName.setText(selectedPlace.getName());

                    imageList.clear();
                    for (AlbumImageDto albumImageDto : albumDto.getAlbumImageList()) {
                        imageList.add(Uri.parse(albumImageDto.getImageUrl()));
                    }

                    imageWithDeleteAdapter = new ImageWithDeleteAdapter(imageList);
                    recyclerView.setAdapter(imageWithDeleteAdapter);
                }
            });
        }

        TextView saveButton = viewBinding.saveButton.findViewById(R.id.button);
        saveButton.setText("Lưu");
        saveButton.setOnClickListener(v -> {

            albumCreateDto.setName(albumName.getText().toString());
            albumCreateDto.setDescription(albumDescription.getText().toString());

            saveAlbum(albumCreateDto);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initViewModels() {
        loadingDialog = new LoadingDialog(this); // Initialize loading dialog
        hideLoadingDialog();

        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);

        albumViewModel.getAlbumByIdResponseLiveData().observe(this, response -> {
            if (response != null && !response.isError() && response.getData() != null) {
                album = objectMapper.map(response.getData(), Album.class);
                List<AlbumImage> albumImages = objectMapper.mapList(response.getData().getAlbumImageList(), AlbumImage.class);
                imageList.clear();
                for (AlbumImage image : albumImages) {
                    imageList.add(Uri.parse(image.getImageUrl()));
                }
                imageWithDeleteAdapter.notifyDataSetChanged();
            }
        });

        albumViewModel.createAlbumResponseLiveData().observe(this, response -> {
            hideLoadingDialog();
            showToast(response.isError() ? "Tạo album thất bại" : "Tạo album thành công");
            if (!response.isError()) finish();
        });

        albumViewModel.updateAlbumResponseLiveData().observe(this, response -> {
            hideLoadingDialog();
            showToast(response.isError() ? "Cập nhật album thất bại" : "Cập nhật album thành công");
            if (!response.isError()) finish();
        });

        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        placeViewModel.getFindPlaceByIdResponseLiveData().observe(this, result -> {
            if (!result.isError()) {
                selectedPlace = result.getData();
                viewBinding.placeName.setText(selectedPlace.getName());
            }
        });
    }

    private void setupHeader() {
        albumId = getIntent().getLongExtra("albumId", 0);
        viewBinding.headerTitle.setText(albumId == 0 ? "Tạo album" : "Chỉnh sửa album");
        if (albumId != 0) {
            String token = "Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken();
            showLoadingDialog();
            albumViewModel.getAlbumById(token, albumId);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imageWithDeleteAdapter = new ImageWithDeleteAdapter(imageList); // Initialize with unified list
        recyclerView.setAdapter(imageWithDeleteAdapter);
    }

    private void setUpButtonEvents() {
        viewBinding.addImageButton.findViewById(R.id.button).setOnClickListener(v -> {
            pickAlbumImages.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        });

        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        TextView cancelButton = viewBinding.cancelButton.findViewById(R.id.button);
        cancelButton.setText("Hủy");
        cancelButton.findViewById(R.id.button).setOnClickListener(v -> {
            finish();
        });

        TextView addImageBtn = viewBinding.addImageButton.findViewById(R.id.button);
        addImageBtn.setText("Thêm ảnh");

        // Feedback image
        ImageView coverImage = viewBinding.coverImage;
        coverImage.setOnClickListener(v -> {
            pickCoverImage.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        });

        // Remove image button
        ConstraintLayout removeImageButton = viewBinding.removeButton.findViewById(R.id.button);
        removeImageButton.setVisibility(View.GONE);
        removeImageButton.setOnClickListener(v -> {
            removeImageButton.setVisibility(View.GONE);
            coverImage.setImageDrawable(null);
            coverImageUri = null;
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupPickMultipleMedia() {
        pickAlbumImages = registerForActivityResult(
                new ActivityResultContracts.PickMultipleVisualMedia(9),
                uris -> {
                    if (uris != null) {
                        imageList.addAll(uris);
                        imageWithDeleteAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("AddEditAlbumActivity", "No media selected");
                    }
                }
        );

        pickCoverImage = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        coverImageUri = uri;
                        Glide.with(this).load(uri).into(viewBinding.coverImage);
                        viewBinding.removeButton.findViewById(R.id.button).setVisibility(View.VISIBLE);
                    } else {
                        Log.d("AddEditAlbumActivity", "No cover image selected");
                    }
                }
        );

        placePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        long placeId = result.getData().getLongExtra("selected_place", 0);
                        if (placeId != 0) placeViewModel.findPlaceById(placeId);
                    }
                });

        TextView selectPlaceBtn = viewBinding.selectPlaceButton.findViewById(R.id.button);
        selectPlaceBtn.setText("Chọn");
        int padding = getResources().getDimensionPixelSize(R.dimen.button_vertical_padding_extra_small);
        selectPlaceBtn.setPadding(selectPlaceBtn.getPaddingLeft(), padding, selectPlaceBtn.getPaddingRight(), padding);
        selectPlaceBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchPlaceActivity.class);
            placePickerLauncher.launch(intent);
        });

    }


    private void saveAlbum(AlbumCreateDto albumCreateDto) {
        String token = "Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken();
        boolean isUpdate = albumId != 0;

        albumCreateDto.setPlaceId(selectedPlace.getId());
        uploadImages(albumCreateDto.getName(), albumImageDtoList -> {
            albumCreateDto.setAlbumImageList(albumImageDtoList);
            if (isUpdate) {
                albumViewModel.updateAlbumById(token, albumId, albumCreateDto);
            } else {
                albumViewModel.createAlbum(token, albumCreateDto);
            }
        });
    }

    private void uploadImages(String albumName, ImageUploadCallback callback) {
        if (imageList.isEmpty()) {
            callback.onComplete(new ArrayList<>());
            return;
        }

        String folderName = "/wanderfun/user/" + SessionManager.getInstance(getApplicationContext()).getAccountId() + "/albums/" + albumName.replaceAll("\\s+", "");
        List<AlbumImageDto> albumImageDtoList = new ArrayList<>();
        AtomicInteger completedUploads = new AtomicInteger(0);
        int totalUploads = imageList.size();

        for (int i = 0; i < imageList.size(); i++) {
            Uri uri = imageList.get(i);
            String fileName = "album_user_" + SessionManager.getInstance(getApplicationContext()).getAccountId() + "_" + System.currentTimeMillis() + "_" + i;

            if (uri.toString().startsWith("http")) {
                AlbumImageDto existingImage = new AlbumImageDto();
                existingImage.setImageUrl(uri.toString());
                existingImage.setImagePublicId(extractPublicIdFromUrl(uri.toString()));
                albumImageDtoList.add(existingImage);

                if (completedUploads.incrementAndGet() == totalUploads) {
                    callback.onComplete(albumImageDtoList);
                }
            } else {
                CloudinaryUtil.uploadImageToCloudinary(getApplicationContext(), uri, fileName, folderName, new CloudinaryUtil.CloudinaryCallback() {
                    @Override
                    public void onSuccess(CloudinaryImageDto result) {
                        AlbumImageDto newImage = new AlbumImageDto();
                        newImage.setImageUrl(result.getUrl());
                        newImage.setImagePublicId(result.getPublicId());
                        albumImageDtoList.add(newImage);

                        if (completedUploads.incrementAndGet() == totalUploads) {
                            callback.onComplete(albumImageDtoList);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(AddEditAlbumActivity.this, "Lỗi tải ảnh lên Cloudinary: " + error, Toast.LENGTH_SHORT).show();
                        if (completedUploads.incrementAndGet() == totalUploads) {
                            callback.onComplete(albumImageDtoList);
                        }
                    }
                });
            }
        }

        if (coverImageUri != null) {
            String coverFileName = "album_user_" + SessionManager.getInstance(getApplicationContext()).getAccountId() + "_" + System.currentTimeMillis() + "_cover";
            CloudinaryUtil.uploadImageToCloudinary(getApplicationContext(), coverImageUri, coverFileName, folderName, new CloudinaryUtil.CloudinaryCallback() {
                @Override
                public void onSuccess(CloudinaryImageDto result) {
                    AlbumImageDto coverImage = new AlbumImageDto();
                    coverImage.setImageUrl(result.getUrl());
                    coverImage.setImagePublicId(result.getPublicId());
                    albumImageDtoList.add(coverImage);

                    if (completedUploads.incrementAndGet() == totalUploads) {
                        callback.onComplete(albumImageDtoList);
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(AddEditAlbumActivity.this, "Lỗi tải ảnh bìa lên Cloudinary: " + error, Toast.LENGTH_SHORT).show();
                    if (completedUploads.incrementAndGet() == totalUploads) {
                        callback.onComplete(albumImageDtoList);
                    }
                }
            });
        }
    }

    private void showLoadingDialog() {
        loadingDialog.setVisibility(View.VISIBLE);
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        loadingDialog.setVisibility(View.GONE);
        loadingDialog.hide();
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private String extractPublicIdFromUrl(String url) {
        int startIndex = url.indexOf("wanderfun");
        if (startIndex != -1) {
            String publicIdWithExtension = url.substring(startIndex);
            return publicIdWithExtension.split("\\.")[0];
        }
        return "";
    }

    @FunctionalInterface
    interface ImageUploadCallback {
        void onComplete(List<AlbumImageDto> images);
    }
}