package com.example.wanderfunmobile.presentation.ui.activity.album;

import android.annotation.SuppressLint;
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
import androidx.core.content.ContextCompat;
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
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.databinding.ActivityAddEditAlbumBinding;
import com.example.wanderfunmobile.domain.model.albums.AlbumImage;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.activity.place.SearchPlaceActivity;
import com.example.wanderfunmobile.presentation.ui.adapter.ImageWithDeleteAdapter;
import com.example.wanderfunmobile.core.util.CloudinaryUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.places.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditAlbumActivity extends AppCompatActivity {
    private ActivityAddEditAlbumBinding viewBinding;
    private final AlbumCreateDto albumCreateDto = new AlbumCreateDto();
    private Uri coverImageUri;
    private Long albumId;
    private Place selectedPlace;
    private AlbumViewModel albumViewModel;
    private PlaceViewModel placeViewModel;
    private ImageWithDeleteAdapter imageWithDeleteAdapter;
    private ActivityResultLauncher<PickVisualMediaRequest> pickAlbumImages, pickCoverImage;
    private ActivityResultLauncher<Intent> placePickerLauncher;
    @Inject
    ObjectMapper objectMapper;
    private LoadingDialog loadingDialog;

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

        loadingDialog = new LoadingDialog(this);

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

                    imageWithDeleteAdapter = new ImageWithDeleteAdapter(new ArrayList<>());
                    for (AlbumImageDto albumImageDto : albumDto.getAlbumImageList()) {
                        imageWithDeleteAdapter.addImage(Uri.parse(albumImageDto.getImageUrl()));
                    }

                    recyclerView.setAdapter(imageWithDeleteAdapter);
                    loadingDialog.hide();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initViewModels() {
        loadingDialog.hide();

        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);

        albumViewModel.getAlbumByIdResponseLiveData().observe(this, response -> {
            if (response != null && !response.isError() && response.getData() != null) {
                List<AlbumImage> albumImages = objectMapper.mapList(response.getData().getAlbumImageList(), AlbumImage.class);

                for (AlbumImage image : albumImages) {
                    imageWithDeleteAdapter.addImage(Uri.parse(image.getImageUrl()));
                }
                imageWithDeleteAdapter.notifyDataSetChanged();
                updateRecyclerViewVisibility();
                loadingDialog.hide();
            }
        });

        albumViewModel.createAlbumResponseLiveData().observe(this, response -> {
            loadingDialog.hide();
            showToast(response.isError() ? "Tạo album thất bại" : "Tạo album thành công");
            if (!response.isError()) finish();
        });

        albumViewModel.updateAlbumResponseLiveData().observe(this, response -> {
            loadingDialog.hide();
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
            loadingDialog.show();
            albumViewModel.getAlbumById(token, albumId);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imageWithDeleteAdapter = new ImageWithDeleteAdapter(new ArrayList<>());
        imageWithDeleteAdapter.setOnImageListChangedListener(newSize -> {
            viewBinding.albumImageListPlaceholderText.setVisibility(newSize > 0 ? View.GONE : View.VISIBLE);
        });
        recyclerView.setAdapter(imageWithDeleteAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void setUpButtonEvents() {
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
        addImageBtn.findViewById(R.id.button).setOnClickListener(v -> {
            pickAlbumImages.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        });

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
            resetCoverImageLayout();
        });
    }

    @SuppressLint("SetTextI18n")
    private void setupPickMultipleMedia() {
        pickAlbumImages = registerForActivityResult(
                new ActivityResultContracts.PickMultipleVisualMedia(9),
                uris -> {
                    if (uris != null) {
                        imageWithDeleteAdapter.addImages(uris);
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
                        Glide.with(this)
                                .load(uri)
                                .into(viewBinding.coverImage);
                        viewBinding.removeButton.findViewById(R.id.button).setVisibility(View.VISIBLE);
                        setNonEmptyCoverImageLayout();
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
        loadingDialog.show();
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
        if (imageWithDeleteAdapter.getImageList().isEmpty()) {
            callback.onComplete(new ArrayList<>());
            return;
        }

        List<Uri> finalList = imageWithDeleteAdapter.getImageList();

        String folderName = "/wanderfun/user/" + SessionManager.getInstance(getApplicationContext()).getAccountId() + "/albums/" + albumName.replaceAll("\\s+", "");
        List<AlbumImageDto> albumImageDtoList = new ArrayList<>();
        AtomicInteger completedUploads = new AtomicInteger(0);
        int totalUploads = finalList.size();

        for (int i = 0; i < finalList.size(); i++) {
            Uri uri = finalList.get(i);
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

    private void updateRecyclerViewVisibility() {
        RecyclerView recyclerView = viewBinding.albumImageList;
        if (imageWithDeleteAdapter.getImageList().isEmpty()) {
            recyclerView.setVisibility(View.GONE); // Hide RecyclerView
        } else {
            recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void setNonEmptyCoverImageLayout() {
        ImageView coverImage = viewBinding.coverImage;
        coverImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        coverImage.setImageTintList(null);
    }

    private void resetCoverImageLayout() {
        ImageView coverImage = viewBinding.coverImage;
        coverImage.setImageResource(R.drawable.ic_add_image); // Set a default blank image
        coverImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        coverImage.setImageTintList(ContextCompat.getColorStateList(this, R.color.blue2));
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