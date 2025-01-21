package com.example.wanderfunmobile.infrastructure.ui.activity.place;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.application.dto.feedback.FeedbackCreateDto;
import com.example.wanderfunmobile.databinding.ActivityFeedbackCreateBinding;
import com.example.wanderfunmobile.domain.model.User;
import com.example.wanderfunmobile.infrastructure.ui.activity.MainActivity;
import com.example.wanderfunmobile.infrastructure.ui.custom.starrating.StarRatingOutlineView;
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
            if (!data.isError()) {
                user = objectMapper.map(data.getData(), User.class);
                setInfo();
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

        EditText comment = viewBinding.commentContainer.findViewById(R.id.content_edittext);

        TextView addImageButton = viewBinding.addImageButton.findViewById(R.id.button);
        addImageButton.setText("Thêm ảnh");

        Long placeId = intent.getLongExtra("placeId", 0L);

        // Submit button
        TextView submitButton = viewBinding.submitButton.findViewById(R.id.button);
        submitButton.setText("Đăng");
        submitButton.setOnClickListener(v -> {
            FeedbackCreateDto feedbackCreateDto = new FeedbackCreateDto();
            feedbackCreateDto.setRating(starRating.getRating());
            if (comment.getText() != null)
                feedbackCreateDto.setComment(comment.getText().toString());
            placeViewModel.createFeedback("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), feedbackCreateDto, placeId);
            placeViewModel.createFeedbackResponseLiveData().observe(this, data -> {
                if (!data.isError()) {
                    Intent intent1 = new Intent(this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
            });
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
}