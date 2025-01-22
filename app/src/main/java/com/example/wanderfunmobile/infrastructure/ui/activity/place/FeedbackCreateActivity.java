package com.example.wanderfunmobile.infrastructure.ui.activity.place;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.application.dto.cloudinary.CloudinaryImageDto;
import com.example.wanderfunmobile.application.dto.feedback.FeedbackCreateDto;
import com.example.wanderfunmobile.databinding.ActivityFeedbackCreateBinding;
import com.example.wanderfunmobile.domain.model.User;
import com.example.wanderfunmobile.infrastructure.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.infrastructure.ui.custom.starrating.StarRatingOutlineView;
import com.example.wanderfunmobile.infrastructure.util.CloudinaryUtil;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.UserViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FeedbackCreateActivity extends AppCompatActivity {

    private ActivityFeedbackCreateBinding viewBinding;
    private PlaceViewModel placeViewModel;
    private UserViewModel userViewModel;
    private User user;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Uri imageUri;
    private LoadingDialog loadingDialog;
    @Inject
    ObjectMapper objectMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Binding
        viewBinding = ActivityFeedbackCreateBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.feedback_create_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getSelfInfo("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());
        userViewModel.getSelfInfoResponseLiveData().observe(this, data -> {
            if (data != null && !data.isError()) {
                user = objectMapper.map(data.getData(), User.class);
                setInfo();
            }
        });

        // Loading dialog
        loadingDialog = viewBinding.loadingDialog;
        hideLoadingDialog();

        // Feedback image
        ImageView feedbackImage = viewBinding.feedbackImage;

        // Remove image button
        ConstraintLayout removeImageButton = viewBinding.removeButton.findViewById(R.id.button);
        removeImageButton.setVisibility(View.GONE);
        removeImageButton.setOnClickListener(v -> {
            removeImageButton.setVisibility(View.GONE);
            feedbackImage.setVisibility(ImageView.GONE);
            feedbackImage.setImageDrawable(null);
            imageUri = null;
        });

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                imageUri = uri;
                Glide.with(this).load(uri).into(viewBinding.feedbackImage);
                feedbackImage.setVisibility(ImageView.VISIBLE);
                removeImageButton.setVisibility(View.VISIBLE);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        // Back button
        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Start rating
        StarRatingOutlineView starRating = viewBinding.starRatingOutlineView;
        starRating.disableIntent();
        Intent intent = getIntent();
        int rating = intent.getIntExtra("rating", 1);
        starRating.setRating(rating);

        // Comment
        EditText comment = viewBinding.commentContainer.findViewById(R.id.content_edittext);

        // Add image button
        TextView addImageButton = viewBinding.addImageButton.findViewById(R.id.button);
        addImageButton.setText("Chọn ảnh");
        int paddingVertical = getResources().getDimensionPixelSize(R.dimen.button_vertical_padding_small);
        addImageButton.setPadding(
                addImageButton.getPaddingLeft(),
                paddingVertical,
                addImageButton.getPaddingRight(),
                paddingVertical
        );
        addImageButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        // Get place Id
        Long placeId = intent.getLongExtra("placeId", 0L);

        // Get place name
        String placeName = intent.getStringExtra("placeName");

        // Header title
        TextView headerTitle = viewBinding.headerTitle;
        if (placeName != null && !placeName.isEmpty()) {
            headerTitle.setText(placeName);
        }

        // Submit button
        TextView submitButton = viewBinding.submitButton.findViewById(R.id.button);
        submitButton.setText("Đăng");
        submitButton.setOnClickListener(v -> {
            showLoadingDialog();
            FeedbackCreateDto feedbackCreateDto = new FeedbackCreateDto();
            feedbackCreateDto.setRating(starRating.getRating());
            if (comment.getText() != null)
                feedbackCreateDto.setComment(comment.getText().toString());
            if (feedbackImage.getDrawable() != null && imageUri != null && placeName != null) {
                String folderName = "/wanderfun/places/" + placeName.replaceAll("\\s", "") + "/feedbacks/user_" + user.getId().toString();
                String fileName = "feedback_user_" + user.getId().toString() + "_" + System.currentTimeMillis();
                CloudinaryUtil.uploadImageToCloudinary(getApplicationContext(), imageUri, fileName, folderName, new CloudinaryUtil.CloudinaryCallback() {
                    @Override
                    public void onSuccess(CloudinaryImageDto result) {
                        feedbackCreateDto.setImageUrl(result.getUrl());
                        feedbackCreateDto.setImagePublicId(result.getPublicId());
                        placeViewModel.createFeedback("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), feedbackCreateDto, placeId);
                    }

                    @Override
                    public void onError(String error) {
                        feedbackCreateDto.setImagePublicId(null);
                        feedbackCreateDto.setImageUrl(null);
                        placeViewModel.createFeedback("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), feedbackCreateDto, placeId);
                    }
                });
            } else {
                placeViewModel.createFeedback("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), feedbackCreateDto, placeId);
            }
        });

        placeViewModel.createFeedbackResponseLiveData().observe(this, data -> {
            if (data != null && !data.isError()) {
                hideLoadingDialog();
                Toast.makeText(getApplicationContext(), "Tạo đánh giá thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                hideLoadingDialog();
                Toast.makeText(getApplicationContext(), "Tạo đánh giá thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setInfo() {
        // User name
        TextView userName = viewBinding.userName;
        if (user != null) {
            userName.setText(user.getLastName() + " " + user.getFirstName());
        }
        // Avatar
        ImageView userAvatar = viewBinding.userAvatar;
        if (user != null && user.getAvatarUrl() != null) {
            Glide.with(viewBinding.getRoot())
                    .load(user.getAvatarUrl())
                    .error(R.drawable.brown)
                    .into(userAvatar);
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
}