package com.example.wanderfunmobile.presentation.ui.activity.trip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.data.dto.cloudinary.CloudinaryImageDto;
import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;
import com.example.wanderfunmobile.data.dto.trip.TripCreateDto;
import com.example.wanderfunmobile.data.dto.trip.TripDto;
import com.example.wanderfunmobile.data.dto.tripplace.TripPlaceCreateDto;
import com.example.wanderfunmobile.data.dto.tripplace.TripPlaceDto;
import com.example.wanderfunmobile.databinding.ActivityAddEditTripBinding;
import com.example.wanderfunmobile.domain.model.images.Image;
import com.example.wanderfunmobile.domain.model.trips.Trip;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;
import com.example.wanderfunmobile.presentation.ui.adapter.tripplace.TripPlaceItemAdapter;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.core.util.CloudinaryUtil;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.TripViewModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditTripActivity extends AppCompatActivity {

    private ActivityAddEditTripBinding viewBinding;
    private Long tripId;
    private Trip trip;
    private TripViewModel tripViewModel;
    private LinearLayout imagePicker;
    private Uri imageUri;
    private TripPlaceItemAdapter tripPlaceItemAdapter;
    private final List<TripPlace> tripPlaceList = new ArrayList<>();
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private LoadingDialog loadingDialog;

    @Inject
    ObjectMapper objectMapper;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityAddEditTripBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Loading dialog
        loadingDialog = viewBinding.loadingDialog;
        hideLoadingDialog();

        PlaceViewModel placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getTripByIdResponseLiveData().observe(this, data -> {
            if (data != null && !data.isError() && data.getData() != null) {
                trip = objectMapper.map(data.getData(), Trip.class);
                List<TripPlace> tripPlaces = objectMapper.mapList(data.getData().getTripPlaceList(), TripPlace.class);
                setTripInfo();
                tripPlaceList.clear();
                tripPlaceList.addAll(tripPlaces);
                tripPlaceItemAdapter.notifyDataSetChanged();
            }
        });
        tripViewModel.createTripResponseLiveData().observe(this, data -> {
            if (data != null && !data.isError()) {
                hideLoadingDialog();
                Toast.makeText(getApplicationContext(), "Tạo chuyến đi thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                hideLoadingDialog();
                Toast.makeText(getApplicationContext(), "Tạo chuyến đi thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        tripViewModel.updateTripByIdResponseLiveData().observe(this, data -> {
            if (data != null && !data.isError()) {
                hideLoadingDialog();
                Toast.makeText(getApplicationContext(), "Cập nhật chuyến đi thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                hideLoadingDialog();
                Toast.makeText(getApplicationContext(), "Cập nhật chuyến đi thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button
        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Get trip id from intent
        Intent intent = getIntent();
        tripId = intent.getLongExtra("tripId", 0);

        // Header title
        if (tripId == 0) {
            viewBinding.headerTitle.setText("Tạo chuyến đi");
        } else {
            viewBinding.headerTitle.setText("Chỉnh sửa chuyến đi");
            tripViewModel.getTripById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), tripId);
        }

        // Image
        ImageView image = viewBinding.image;
        image.setVisibility(ImageView.GONE);

        // Remove image button
        ConstraintLayout removeImageButton = viewBinding.removeImageButton.findViewById(R.id.button);
        removeImageButton.setVisibility(View.GONE);
        removeImageButton.setOnClickListener(v -> {
            removeImageButton.setVisibility(View.GONE);
            image.setVisibility(ImageView.GONE);
            image.setImageDrawable(null);
            imageUri = null;
            imagePicker.setVisibility(View.VISIBLE);
        });

        // Pick image
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                imageUri = uri;
                Glide.with(this).load(uri).into(viewBinding.image);
                image.setVisibility(ImageView.VISIBLE);
                removeImageButton.setVisibility(View.VISIBLE);
                imagePicker.setVisibility(View.GONE);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        // Image picker
        imagePicker = viewBinding.imagePicker;
        imagePicker.setVisibility(ImageView.VISIBLE);
        imagePicker.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        // Trip place list
        RecyclerView recyclerView = viewBinding.tripPlaceList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tripPlaceItemAdapter = new TripPlaceItemAdapter(tripPlaceList);
        recyclerView.setAdapter(tripPlaceItemAdapter);

        // Add place button
        TextView addPlaceButton = viewBinding.addPlaceButton.findViewById(R.id.button);
        addPlaceButton.setText("Thêm địa điểm");
        int paddingVertical = getResources().getDimensionPixelSize(R.dimen.button_vertical_padding_small);
        addPlaceButton.setPadding(
                addPlaceButton.getPaddingLeft(),
                paddingVertical,
                addPlaceButton.getPaddingRight(),
                paddingVertical
        );

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                // Handle the returned data
                Long placeId = result.getData().getLongExtra("place_id", 0);
                String placeName = result.getData().getStringExtra("place_name");
                String placeCoverImage = result.getData().getStringExtra("place_cover_image");
                String startTime = result.getData().getStringExtra("start_time");
                String endTime = result.getData().getStringExtra("end_time");

                TripPlaceDto tripPlaceDto = new TripPlaceDto();
                tripPlaceDto.setPlace(new MiniPlaceDto());
                tripPlaceDto.getPlace().setCoverImage(new Image());

                tripPlaceDto.getPlace().setId(placeId);
                tripPlaceDto.getPlace().setName(placeName);
                tripPlaceDto.getPlace().getCoverImage().setImageUrl(placeCoverImage);

                if (startTime != null && !startTime.isEmpty()) {
                    tripPlaceDto.setStartTime(DateTimeUtil.stringToLocalDate(startTime));
                }

                if (endTime != null && !endTime.isEmpty()) {
                    tripPlaceDto.setEndTime(DateTimeUtil.stringToLocalDate(endTime));
                }
                tripPlaceList.add(objectMapper.map(tripPlaceDto, TripPlace.class));
                tripPlaceItemAdapter.notifyDataSetChanged();
            }
        });

        addPlaceButton.setOnClickListener(v -> {
            Intent addIntent = new Intent(this, TripPlaceCreateActivity.class);
            if (!tripPlaceList.isEmpty()) {
                addIntent.putExtra("limit_start_time", DateTimeUtil.localDateToString(tripPlaceList.get(tripPlaceList.size() - 1).getEndTime()));
            }
            activityResultLauncher.launch(addIntent);
        });

        EditText name = viewBinding.name.findViewById(R.id.text_edittext);
        name.setHint("Nhập tên chuyến đi");

        TextView cancelButton = viewBinding.cancelButton.findViewById(R.id.button);
        cancelButton.setText("Hủy");
        cancelButton.setOnClickListener(v -> finish());

        TextView confirmButton = viewBinding.confirmButton.findViewById(R.id.button);
        confirmButton.setText("Xong");
        confirmButton.setOnClickListener(v -> {
            showLoadingDialog();
            TripCreateDto tripCreateDto = new TripCreateDto();
            tripCreateDto.setName(name.getText().toString());
            List<TripPlaceCreateDto> tripPlaceCreateList = new ArrayList<>(objectMapper.mapList(tripPlaceList, TripPlaceCreateDto.class));
            for (int i = 0; i < tripPlaceCreateList.size(); i++) {
                tripPlaceCreateList.get(i).setPlaceId(tripPlaceList.get(i).getPlace().getId());
            }
            tripCreateDto.setTripPlaceList(tripPlaceCreateList);

            if (image.getDrawable() != null && imageUri != null) {
                String folderName = "/wanderfun/trips/" + name.getText().toString().replaceAll("\\s", "") + "/images";
                String fileName = "image_" + System.currentTimeMillis();
                CloudinaryUtil.uploadImageToCloudinary(getApplicationContext(), imageUri, fileName, folderName, new CloudinaryUtil.CloudinaryCallback() {
                    @Override
                    public void onSuccess(CloudinaryImageDto result) {
                        tripCreateDto.setImageUrl(result.getUrl());
                        tripCreateDto.setImagePublicId(result.getPublicId());
                        doAddOrUpdate(tripCreateDto);
                    }

                    @Override
                    public void onError(String error) {
                        tripCreateDto.setImagePublicId(null);
                        tripCreateDto.setImageUrl(null);
                        doAddOrUpdate(tripCreateDto);
                    }
                });
            } else {
                doAddOrUpdate(tripCreateDto);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setTripInfo() {
        if (trip != null && !trip.getName().isEmpty()) {
            EditText name = viewBinding.name.findViewById(R.id.text_edittext);
            name.setText(trip.getName());
            tripPlaceItemAdapter.setEditMode(true);
        }

//        if (trip != null && trip.getImageUrl() != null) {
//            viewBinding.image.setVisibility(View.VISIBLE);
//            viewBinding.removeImageButton.findViewById(R.id.button).setVisibility(View.VISIBLE);
//            Glide.with(this).load(trip.getImageUrl()).into(viewBinding.image);
//            viewBinding.imagePicker.setVisibility(View.GONE);
//        }

        if (trip != null && trip.getTripPlaceList() != null) {
            //tripPlaceList.clear();
            tripPlaceList.addAll(trip.getTripPlaceList());
            tripPlaceItemAdapter.notifyDataSetChanged();
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

    private void doAddOrUpdate(TripCreateDto tripCreateDto) {
        if (tripId == 0) {
            tripViewModel.createTrip("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), tripCreateDto);
        } else {
            tripViewModel.updateTripById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), tripId, tripCreateDto);
        }
    }
}