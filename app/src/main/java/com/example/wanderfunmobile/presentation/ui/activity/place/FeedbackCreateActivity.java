package com.example.wanderfunmobile.presentation.ui.activity.place;

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
import com.example.wanderfunmobile.data.dto.cloudinary.CloudinaryImageDto;
import com.example.wanderfunmobile.databinding.ActivityFeedbackCreateBinding;
import com.example.wanderfunmobile.domain.model.images.Image;
import com.example.wanderfunmobile.domain.model.places.Feedback;
import com.example.wanderfunmobile.domain.model.users.User;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.ui.custom.starrating.StarRatingOutlineView;
import com.example.wanderfunmobile.core.util.CloudinaryUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.places.FeedbackViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.places.PlaceViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.UserViewModel;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FeedbackCreateActivity extends AppCompatActivity {

    private ActivityFeedbackCreateBinding viewBinding;
    private FeedbackViewModel feedbackViewModel;
    private UserViewModel userViewModel;
    private User user;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Uri imageUri;
    private LoadingDialog loadingDialog;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    Gson gson;
    private Long placeId;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Binding
        viewBinding = ActivityFeedbackCreateBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpMediaLauncher();

        setUpViewModel();

        bindDataFromIntent();

        setUpView();

        fetchUserData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void setUpMediaLauncher() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                imageUri = uri;
                Glide.with(this).load(uri).into(viewBinding.feedbackImage);
                viewBinding.feedbackImage.setVisibility(ImageView.VISIBLE);
                viewBinding.removeButton.button.setVisibility(View.VISIBLE);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
    }

    private void setUpViewModel() {
        // Feedback ViewModel
        feedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
        feedbackViewModel.getCreateFeedbackLiveData().observe(this, data -> {
            if (data != null && !data.isError()) {
                loadingDialog.hide();
                Toast.makeText(getApplicationContext(), "Tạo đánh giá thành công", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("status", "feedback_created");
                resultIntent.putExtra("feedback_json", gson.toJson(data.getData()));
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                loadingDialog.hide();
                Toast.makeText(this, "Tạo đánh giá thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        // User ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getMiniSelfInfoResponseLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                user = result.getData();
                bindUserData();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindDataFromIntent() {
        Intent intent = getIntent();
        placeName = intent.getStringExtra("place_name");
        placeId = intent.getLongExtra("place_id", 0L);

        // Start rating
        StarRatingOutlineView starRating = viewBinding.starRatingOutlineView;
        starRating.disableIntent();
        int rating = intent.getIntExtra("rating", 1);
        starRating.setRating(rating);

        // Header title
        TextView headerTitle = viewBinding.headerTitle;
        if (placeName != null && !placeName.isEmpty()) {
            headerTitle.setText(placeName);
        }
    }

    private void setUpView() {
        // Loading dialog
        loadingDialog = new LoadingDialog(this);

        // Feedback image
        ImageView feedbackImage = viewBinding.feedbackImage;
        feedbackImage.setVisibility(ImageView.GONE);

        // Remove image button
        ConstraintLayout removeImageButton = viewBinding.removeButton.button;
        removeImageButton.setVisibility(View.GONE);
        removeImageButton.setOnClickListener(v -> {
            removeImageButton.setVisibility(View.GONE);
            feedbackImage.setVisibility(ImageView.GONE);
            feedbackImage.setImageDrawable(null);
            imageUri = null;
        });

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

        // Back button
        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Submit button
        TextView submitButton = viewBinding.submitButton.findViewById(R.id.button);
        submitButton.setText("Đăng");
        submitButton.setOnClickListener(v -> {
            loadingDialog.show();
            Feedback feedback = new Feedback();
            feedback.setRating(viewBinding.starRatingOutlineView.getRating());
            if (comment.getText() != null)
                feedback.setContent(comment.getText().toString());
            feedback.setImage(new Image());
            if (feedbackImage.getDrawable() != null && imageUri != null && placeName != null) {
                String folderName = "/wanderfun/places/" + placeName.replaceAll("\\s", "") + "/feedbacks/user_" + user.getId().toString();
                String fileName = "feedback_user_" + user.getId().toString() + "_" + System.currentTimeMillis();
                CloudinaryUtil.uploadImageToCloudinary(getApplicationContext(), imageUri, fileName, folderName, new CloudinaryUtil.CloudinaryCallback() {
                    @Override
                    public void onSuccess(CloudinaryImageDto result) {
                        feedback.getImage().setImageUrl(result.getUrl());
                        feedback.getImage().setImagePublicId(result.getPublicId());
                        feedbackViewModel.createFeedback("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), placeId, feedback);
                    }

                    @Override
                    public void onError(String error) {
                        feedback.getImage().setImagePublicId(null);
                        feedback.getImage().setImageUrl(null);
                        feedbackViewModel.createFeedback("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), placeId, feedback);
                    }
                });
            } else {
                feedbackViewModel.createFeedback("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), placeId, feedback);
            }
        });
    }

    private void fetchUserData() {
        userViewModel.getMiniSelfInfo("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());
    }

    @SuppressLint("SetTextI18n")
    private void bindUserData() {
        // User name
        TextView userName = viewBinding.userName;
        if (user != null) {
            userName.setText(user.getLastName() + " " + user.getFirstName());
        }
        // Avatar
        ImageView userAvatar = viewBinding.userAvatar;
        if (user != null && user.getAvatarImage() != null && user.getAvatarImage().getImageUrl() != null) {
            Glide.with(viewBinding.getRoot())
                    .load(user.getAvatarImage().getImageUrl())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .into(userAvatar);
        }
    }
}