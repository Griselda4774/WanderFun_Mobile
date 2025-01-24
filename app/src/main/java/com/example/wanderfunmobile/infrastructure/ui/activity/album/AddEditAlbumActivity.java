package com.example.wanderfunmobile.infrastructure.ui.activity.album;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.application.dto.album.AlbumCreateDto;
import com.example.wanderfunmobile.application.dto.album.AlbumDto;
import com.example.wanderfunmobile.application.dto.albumimage.AlbumImageCreateDto;
import com.example.wanderfunmobile.application.dto.albumimage.AlbumImageDto;
import com.example.wanderfunmobile.application.dto.cloudinary.CloudinaryImageDto;
import com.example.wanderfunmobile.application.dto.place.PlaceDto;
import com.example.wanderfunmobile.databinding.ActivityAddEditAlbumBinding;
import com.example.wanderfunmobile.infrastructure.ui.activity.place.SearchPlaceActivity;
import com.example.wanderfunmobile.infrastructure.ui.adapter.ImageWithDeleteAdapter;
import com.example.wanderfunmobile.infrastructure.util.CloudinaryUtil;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.viewmodel.AlbumViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditAlbumActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SEARCH_PLACE = 1;
    AlbumCreateDto albumCreateDto = new AlbumCreateDto();
    List<Uri> imageList = new ArrayList<>();
    private AlbumViewModel albumViewModel;
    private PlaceViewModel placeViewModel;
    private ImageWithDeleteAdapter imageWithDeleteAdapter;
    private EditText albumPlace;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long albumId = getIntent().getLongExtra("albumId", 0);

        ActivityAddEditAlbumBinding viewBinding = ActivityAddEditAlbumBinding.inflate(getLayoutInflater());
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText albumName = viewBinding.albumNameLayout.findViewById(R.id.text_edittext);

        EditText albumDescription = viewBinding.albumDescriptionLayout.findViewById(R.id.content_edittext);

        albumPlace = viewBinding.albumPlaceLayout.findViewById(R.id.content_edittext);

        RecyclerView recyclerView = viewBinding.albumImageList.findViewById(R.id.album_image_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                // Handle the returned data
                Long placeId = result.getData().getLongExtra("selected_place", 0);
                if (placeId != 0) {
                    placeViewModel.getPlaceById(placeId);
                    placeViewModel.getPlaceByIdResponseLiveData().observe(this, response -> {
                        if (!response.isError()) {
                            PlaceDto placeDto = response.getData();

                            albumCreateDto.setPlaceId(placeDto.getId());
                            albumCreateDto.setPlaceName(placeDto.getName());
                            albumCreateDto.setPlaceCoverImageUrl(placeDto.getCoverImageUrl());
                            albumCreateDto.setPlaceLatitude(placeDto.getLatitude());
                            albumCreateDto.setPlaceLongitude(placeDto.getLongitude());

                            albumPlace.setText(placeDto.getName());
                        }
                    });
                }
            }
        });

        pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(9), uris -> {
            if (!uris.isEmpty()) {
                int previousSize = imageList.size(); // Track current size
                imageList.addAll(uris); // Add new images to the list
                if (imageWithDeleteAdapter == null) {
                    // Initialize the adapter only once
                    imageWithDeleteAdapter = new ImageWithDeleteAdapter(imageList);
                    recyclerView.setAdapter(imageWithDeleteAdapter);
                } else {
                    // Notify adapter of new items added
                    imageWithDeleteAdapter.notifyItemRangeInserted(previousSize, uris.size());
                }
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        TextView headerTitle = viewBinding.headerTitle;

        if (albumId != 0) {
            albumViewModel.getAlbumById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), albumId);
            albumViewModel.getAlbumByIdResponseLiveData().observe(this, response -> {
                if (!response.isError()) {
                    AlbumDto albumDto = response.getData();
                    albumName.setText(albumDto.getName());
                    albumDescription.setText(albumDto.getDescription());
                    albumPlace.setText(albumDto.getPlaceName());

                    for (AlbumImageDto albumImageDto : albumDto.getAlbumImages()) {
                        imageList.add(Uri.parse(albumImageDto.getImageUrl()));
                    }

                    imageWithDeleteAdapter = new ImageWithDeleteAdapter(imageList);
                    recyclerView.setAdapter(imageWithDeleteAdapter);
                }
            });
            headerTitle.setText("Chỉnh sửa album");
        } else {
            headerTitle.setText("Tạo album mới");
        }

        TextView searchPlaceButton = viewBinding.searchPlaceButton.findViewById(R.id.button);
        searchPlaceButton.setText("Chọn");


        searchPlaceButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchPlaceActivity.class);
            activityResultLauncher.launch(intent);
        });

        TextView addImageButton = viewBinding.addImageButton.findViewById(R.id.button);
        addImageButton.setText("Thêm ảnh");
        addImageButton.setOnClickListener(v -> {
            pickMultipleMedia.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        });

        TextView cancelButton = viewBinding.cancelButton.findViewById(R.id.button);
        cancelButton.setText("Hủy bỏ");
        cancelButton.setOnClickListener(v -> {
            finish();
        });

        TextView saveButton = viewBinding.saveButton.findViewById(R.id.button);
        saveButton.setText("Lưu");
        saveButton.setOnClickListener(v -> {

            albumCreateDto.setName(albumName.getText().toString());
            albumCreateDto.setDescription(albumDescription.getText().toString());

            if (albumId != 0) {
                updateAlbum(albumCreateDto, albumId);
            } else {
                createAlbum(albumCreateDto);
            }
        });
    }

    private void createAlbum(AlbumCreateDto albumCreateDto) {

        if (imageList.size() > 0) {
            String folderName = "/wanderfun/user/" + SessionManager.getInstance(getApplicationContext()).getUserId().toString() + "/albums/" + albumCreateDto.getName();
            String fileName = "album_user_" + SessionManager.getInstance(getApplicationContext()).getUserId().toString() + "_" + System.currentTimeMillis();
            List<AlbumImageCreateDto> albumImageCreateDtoList = new ArrayList<>();
            AtomicInteger completedUploads = new AtomicInteger(0); // Track completed uploads
            int totalUploads = imageList.size(); // Total images to upload

            for (Uri uri : imageList) {
                CloudinaryUtil.uploadImageToCloudinary(getApplicationContext(), uri, fileName, folderName, new CloudinaryUtil.CloudinaryCallback() {
                    @Override
                    public void onSuccess(CloudinaryImageDto result) {
                        AlbumImageCreateDto albumImageCreateDto = new AlbumImageCreateDto();
                        albumImageCreateDto.setImageUrl(result.getUrl());
                        albumImageCreateDto.setImagePublicId(result.getPublicId());
                        albumImageCreateDtoList.add(albumImageCreateDto);

                        // Increment counter and check if all uploads are complete
                        if (completedUploads.incrementAndGet() == totalUploads) {
                            // This runs only after all uploads are done
                            albumCreateDto.setAlbumImages(albumImageCreateDtoList);
                            albumViewModel.createAlbum("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), albumCreateDto);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(AddEditAlbumActivity.this, "Tạo album thất bại, lỗi Cloudinary!", Toast.LENGTH_SHORT).show();

                        // Increment counter even on error to prevent deadlocks
                        if (completedUploads.incrementAndGet() == totalUploads) {
                            // This runs if all uploads are either complete or errored
                            albumCreateDto.setAlbumImages(albumImageCreateDtoList);
                            albumViewModel.createAlbum("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), albumCreateDto);
                        }
                    }
                });
            }
        }

        albumViewModel.createAlbumResponseLiveData().observe(this, response -> {
            if (!response.isError()) {
                Toast.makeText(this, "Tạo album thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
            Toast.makeText(this, "Tạo album thất bại", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateAlbum(AlbumCreateDto albumCreateDto, Long albumId) {
        if (imageList.size() > 0) {
            String folderName = "/wanderfun/user/" + SessionManager.getInstance(getApplicationContext()).getUserId().toString() + "/albums/" + albumCreateDto.getName().replaceAll("\\s+", "");
            String fileName = "album_user_" + SessionManager.getInstance(getApplicationContext()).getUserId().toString() + "_" + System.currentTimeMillis();
            List<AlbumImageCreateDto> albumImageCreateDtoList = new ArrayList<>();
            AtomicInteger completedUploads = new AtomicInteger(0); // Track completed uploads
            int totalUploads = imageList.size(); // Total images to upload

            for (Uri uri : imageList) {
                if (uri.toString().startsWith("http")) {
                    // Case 1: Existing Cloudinary image, add its details directly
                    AlbumImageCreateDto existingImage = new AlbumImageCreateDto();
                    existingImage.setImageUrl(uri.toString()); // URL is already set
                    existingImage.setImagePublicId(extractPublicIdFromUrl(uri.toString())); // Extract public ID from the URL
                    albumImageCreateDtoList.add(existingImage);

                    // Increment counter and check if all images are processed
                    if (completedUploads.incrementAndGet() == totalUploads) {
                        albumCreateDto.setAlbumImages(albumImageCreateDtoList);
                        albumViewModel.updateAlbumById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), albumId, albumCreateDto);
                    }
                } else {
                    // Case 2: New image, upload to Cloudinary
                    CloudinaryUtil.uploadImageToCloudinary(getApplicationContext(), uri, fileName, folderName, new CloudinaryUtil.CloudinaryCallback() {
                        @Override
                        public void onSuccess(CloudinaryImageDto result) {
                            AlbumImageCreateDto newImage = new AlbumImageCreateDto();
                            newImage.setImageUrl(result.getUrl());
                            newImage.setImagePublicId(result.getPublicId());
                            albumImageCreateDtoList.add(newImage);

                            // Increment counter and check if all images are processed
                            if (completedUploads.incrementAndGet() == totalUploads) {
                                albumCreateDto.setAlbumImages(albumImageCreateDtoList);
                                albumViewModel.updateAlbumById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), albumId, albumCreateDto);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(AddEditAlbumActivity.this, "Tạo album thất bại, lỗi Cloudinary!", Toast.LENGTH_SHORT).show();
                            finish();

                            // Increment counter to prevent deadlocks
                            if (completedUploads.incrementAndGet() == totalUploads) {
                                albumCreateDto.setAlbumImages(albumImageCreateDtoList);
                                albumViewModel.updateAlbumById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), albumId, albumCreateDto);
                            }
                        }
                    });
                }
            }
            albumViewModel.updateAlbumResponseLiveData().observe(this, response -> {
                if (!response.isError()) {
                    Toast.makeText(this, "Cập nhật album thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Cập nhật album thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String extractPublicIdFromUrl(String url) {
        // Find the index where "wanderfun" starts in the URL
        int startIndex = url.indexOf("wanderfun");
        if (startIndex != -1) {
            // Extract substring from "wanderfun" to the end of the URL
            String publicIdWithExtension = url.substring(startIndex);
            // Remove the file extension (e.g., ".jpg")
            return publicIdWithExtension.split("\\.")[0];
        }
        // Return null or an empty string if "wanderfun" is not found
        return null;
    }
}