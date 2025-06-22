package com.example.wanderfunmobile.presentation.ui.activity.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.LocalDateDeserializer;
import com.example.wanderfunmobile.core.util.LocalDateSerializer;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.ActivityProfileBinding;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.domain.model.users.User;
import com.example.wanderfunmobile.presentation.ui.activity.post.AddEditPostActivity;
import com.example.wanderfunmobile.presentation.ui.adapter.posts.PostItemAdapter;
import com.example.wanderfunmobile.presentation.viewmodel.posts.PostViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding viewBinding;
    private UserViewModel userViewModel;
    private PostViewModel postViewModel;
    private PostItemAdapter postItemAdapter;
    private final List<Post> postList = new ArrayList<>();
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpActivityResultLauncher();

        setUpActivity();

        setUpAdapter();

        setUpViewModel();

        getPost();

        getSelfInfo();
    }

    private void setUpActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null ) {
                            switch (Objects.requireNonNull(data.getStringExtra("status")))
                            {
                                case "post_created":
                                case "post_updated":
                                case "post_deleted":
                                    getPost();
                                    break;
                                case "profile_updated":
                                    getSelfInfo();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    private void setUpActivity() {
        // Back button
        ConstraintLayout backButton = viewBinding.backButton.button;
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Create post input
        viewBinding.createPostInput.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditPostActivity.class);
            activityResultLauncher.launch(intent);
        });

        // No posts text
        viewBinding.noPostText.setVisibility(TextView.GONE);
    }

    private void setUpAdapter() {
        // Posts adapter
        viewBinding.postList.setLayoutManager(new LinearLayoutManager(this));
        postItemAdapter = new PostItemAdapter(postList, activityResultLauncher);
        viewBinding.postList.setAdapter(postItemAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpViewModel() {
        // Post ViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Observe post view model
        postViewModel.getFindAllPostsByUserLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                List<Post> newPosts = result.getData();
                if (newPosts.isEmpty()) {
                    viewBinding.noPostText.setVisibility(TextView.VISIBLE);
                } else {
                    postList.clear();
                    postList.addAll(newPosts);
                    postItemAdapter.notifyDataSetChanged();
                    viewBinding.noPostText.setVisibility(TextView.GONE);
                }
            } else {
                viewBinding.noPostText.setVisibility(TextView.VISIBLE);
            }
        });

        // User ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observe user view model
        userViewModel.getSelfInfoResponseLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                User user = result.getData();
                bindUserData(user);
            } else {
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getPost() {
        postViewModel.findAllPostsByUser("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());
    }

    private void getSelfInfo() {
        userViewModel.getSelfInfo("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());
    }

    @SuppressLint("SetTextI18n")
    private void bindUserData(User user) {
        // Edit button
        if (!Objects.equals(SessionManager.getInstance(getApplicationContext()).getUserId(), user.getId())) {
            viewBinding.editButton.setVisibility(View.GONE);
        } else {
            viewBinding.editButton.setVisibility(View.VISIBLE);
        }

        viewBinding.editButton.setOnClickListener(v -> {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .setDateFormat("yyyy-MM-dd")
                    .create();
            String userJson = gson.toJson(user);
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("user", userJson);
            activityResultLauncher.launch(intent);
        });

        if (user.getFirstName() != null && user.getLastName() != null) {
            viewBinding.userName.setText(user.getLastName() + " " + user.getFirstName());
            viewBinding.firstname.setText(user.getFirstName());
            viewBinding.lastname.setText(user.getLastName());
        } else {
            viewBinding.userName.setText("Không có dữ liệu");
            viewBinding.firstname.setText("Không có dữ liệu");
            viewBinding.lastname.setText("Không có dữ liệu");
        }

        if (user.getDateOfBirth() != null) {
            viewBinding.birthday.setText(DateTimeUtil.localDateToString(user.getDateOfBirth()));
        } else {
            viewBinding.birthday.setText("Không có dữ liệu");
        }

        if (user.getGender() != null) {
            viewBinding.gender.setText(user.getGender());
        }

        if (user.getPhoneNumber() != null) {
            viewBinding.phoneNumber.setText(user.getPhoneNumber());
        } else {
            viewBinding.phoneNumber.setText("Không có dữ liệu");
        }

        if (user.getAvatarImage() != null && user.getAvatarImage().getImageUrl() != null) {
            Glide.with(this)
                .load(user.getAvatarImage().getImageUrl())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(viewBinding.userAvatar);

            Glide.with(this)
                    .load(user.getAvatarImage().getImageUrl())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .into(viewBinding.userAvatarSmall);
        } else {
            viewBinding.userAvatar.setImageResource(R.drawable.ic_avatar);
            viewBinding.userAvatarSmall.setImageResource(R.drawable.ic_avatar);
        }
    }
}