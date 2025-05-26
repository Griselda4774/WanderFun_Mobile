package com.example.wanderfunmobile.presentation.ui.activity.post;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.ActivityAddEditPostBinding;
import com.example.wanderfunmobile.databinding.BottomSheetPostOptionHorizontalBinding;
import com.example.wanderfunmobile.databinding.BottomSheetPostOptionVerticalBinding;
import com.example.wanderfunmobile.domain.model.users.User;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.viewmodel.PostViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditPostActivity extends AppCompatActivity {

    private ActivityAddEditPostBinding viewBinding;

    private BottomSheetPostOptionHorizontalBinding postOptionHorizontalBottomSheet;

    private BottomSheetPostOptionVerticalBinding postOptionVerticalBottomSheet;

    private BottomSheetBehavior<ConstraintLayout> postOptionHorizontalBottomSheetBehavior;
    private BottomSheetBehavior<ConstraintLayout> postOptionVerticalBottomSheetBehavior;

    private Long postId;
    private UserViewModel userViewModel;
    private PostViewModel postViewModel;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    private Uri imageUri;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityAddEditPostBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Observe view model
        userViewModel.miniSelfInfoResponseLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                User user = result.getData();
                bindUserData(user);
            } else {
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Get postId from intent
        postId = getIntent().getLongExtra("postId", -1L);

        // Fetch user info
        userViewModel.getMiniSelfInfo("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());

        // Initialize bottom sheet
        initBottomSheet();

        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });
    }

    private void initBottomSheet() {
        postOptionHorizontalBottomSheet = viewBinding.postOptionHorizontal;
        postOptionHorizontalBottomSheetBehavior = BottomSheetBehavior.from(postOptionHorizontalBottomSheet.getRoot());
        postOptionHorizontalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        postOptionVerticalBottomSheet = viewBinding.postOptionVertical;
        postOptionVerticalBottomSheetBehavior = BottomSheetBehavior.from(postOptionVerticalBottomSheet.getRoot());
        postOptionVerticalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        postOptionHorizontalBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    postOptionVerticalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    postOptionVerticalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        postOptionVerticalBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    postOptionHorizontalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    postOptionHorizontalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void bindUserData(User user) {
        // User name
        viewBinding.userName.setText(user.getFirstName() + " " + user.getLastName());

        // User avatar
        if (user.getAvatarImage() != null) {
            Glide.with(viewBinding.getRoot())
                    .load(user.getAvatarImage().getImageUrl())
                    .error(R.drawable.ic_avatar)
                    .into(viewBinding.userAvatar);
        } else {
            viewBinding.userAvatar.setImageResource(R.drawable.ic_avatar);
        }
    }
}