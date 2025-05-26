package com.example.wanderfunmobile.presentation.ui.activity.post;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.PostViewManager;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.ActivityPostDetailBinding;
import com.example.wanderfunmobile.databinding.ButtonBackRoundedBinding;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.presentation.viewmodel.PostViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PostDetailActivity extends AppCompatActivity {

    private ActivityPostDetailBinding viewBinding;
    private Post post;
    private PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getFindPostByIdLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                post = result.getData();
                bindPostData();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
        getPost();

        // Like comment button wrapper
        if (SessionManager.getInstance(viewBinding.getRoot().getContext()).isLoggedIn()) {
            viewBinding.likeCommentButtonWrapper.setVisibility(View.VISIBLE);
        } else {
            viewBinding.likeCommentButtonWrapper.setVisibility(View.GONE);
        }

        viewBinding.likeButton.setOnClickListener(v -> {
            viewBinding.likeButton.setVisibility(View.GONE);
            viewBinding.likeButtonHighlight.setVisibility(View.VISIBLE);
        });

        viewBinding.likeButtonHighlight.setOnClickListener(v -> {
            viewBinding.likeButton.setVisibility(View.VISIBLE);
            viewBinding.likeButtonHighlight.setVisibility(View.GONE);
        });

        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });
    }

    private void getPost() {
        Intent intent = getIntent();
        if (intent != null) {
            long postId = intent.getLongExtra("postId", -1);
            if (postId > 0) {
                postViewModel.findPostById(postId);
            }
        }
    }
    
    @SuppressLint("SetTextI18n")
    private void bindPostData() {
        // User info
        ImageView userAvatar = viewBinding.userAvatar;
        TextView userName = viewBinding.userName;
        if (post.getUser() != null) {
            if (post.getUser().getAvatarImage() != null) {
                Glide.with(viewBinding.getRoot())
                        .load(post.getUser().getAvatarImage().getImageUrl())
                        .error(R.drawable.ic_avatar)
                        .into(userAvatar);
            }

            userName.setText(post.getUser().getFirstName() + " " + post.getUser().getLastName());
        } else {
            userAvatar.setImageResource(R.drawable.ic_avatar);
            userName.setText("Unknown User");
        }

        // Post info
        // Date created
        TextView dateCreated = viewBinding.dateCreated;
        dateCreated.setText(DateTimeUtil.localDateTimeToString(post.getCreateAt()));

        // Place name
        TextView placeName = viewBinding.placeName;
        if (post.getPlace() != null) {
            viewBinding.placeInfo.setVisibility(View.VISIBLE);
            placeName.setText(post.getPlace().getName());
        } else {
            viewBinding.placeInfo.setVisibility(View.GONE);
        }

        // Post content
        TextView content = viewBinding.content;
        if (post.getContent() != null && !post.getContent().isEmpty()) {
            if (post.getContent().length() > 300) {
                content.setText(post.getContent().substring(0, 300) + "...");
                viewBinding.seeMore.setVisibility(View.VISIBLE);
                viewBinding.seeLess.setVisibility(View.GONE);
            } else {
                content.setText(post.getContent());
                viewBinding.seeMore.setVisibility(View.GONE);
                viewBinding.seeLess.setVisibility(View.GONE);
            }
        } else {
            content.setText("");
        }

        // See more button, see less button

        viewBinding.seeMore.setOnClickListener(v -> {
            content.setText(post.getContent());
            viewBinding.seeMore.setVisibility(View.GONE);
            viewBinding.seeLess.setVisibility(View.VISIBLE);
        });

        viewBinding.seeLess.setOnClickListener(v -> {
            content.setText(post.getContent().substring(0, 300) + "...");
            viewBinding.seeMore.setVisibility(View.VISIBLE);
            viewBinding.seeLess.setVisibility(View.GONE);
        });

        // Post image
        ImageView image = viewBinding.image;
        if (post.getImage() != null) {
            Glide.with(viewBinding.getRoot())
                    .load(post.getImage().getImageUrl())
                    .error(R.drawable.brown)
                    .into(image);
        } else {
            image.setVisibility(View.GONE);
        }

        // Post like count
        TextView likeCount = viewBinding.likeCount;
        if (post.getLikeCount() > 0) {
            likeCount.setText(String.valueOf(post.getLikeCount()));
        } else {
            likeCount.setText("0");
        }

        // Post comment count
        TextView commentCount = viewBinding.commentCount;
        if (post.getCommentCount() > 0) {
            commentCount.setText(String.valueOf(post.getCommentCount()));
        } else {
            commentCount.setText("0");
        }

        // Post deltail button
        viewBinding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(viewBinding.getRoot().getContext(), PostDetailActivity.class);
            intent.putExtra("postId", post.getId());
            viewBinding.getRoot().getContext().startActivity(intent);
        });

        // Explore place button
        viewBinding.explorePlaceButton.setOnClickListener(v -> {

        });

        // Like button
        if (post.isLiked()) {
            viewBinding.likeButton.setVisibility(View.GONE);
            viewBinding.likeButtonHighlight.setVisibility(View.VISIBLE);
        } else {
            viewBinding.likeButton.setVisibility(View.VISIBLE);
            viewBinding.likeButtonHighlight.setVisibility(View.GONE);
        }

        viewBinding.likeButton.setOnClickListener(v -> {
            viewBinding.likeButton.setVisibility(View.GONE);
            viewBinding.likeButtonHighlight.setVisibility(View.VISIBLE);
        });

        viewBinding.likeButtonHighlight.setOnClickListener(v -> {
            viewBinding.likeButton.setVisibility(View.VISIBLE);
            viewBinding.likeButtonHighlight.setVisibility(View.GONE);
        });

        // Comment button
        viewBinding.commentButton.setOnClickListener(v -> {

        });

        if (SessionManager.getInstance(viewBinding.getRoot().getContext()).isLoggedIn()) {
            viewBinding.likeCommentButtonWrapper.setVisibility(View.VISIBLE);
        } else {
            viewBinding.likeCommentButtonWrapper.setVisibility(View.GONE);
        }

        // Modify post button
        if (SessionManager.getInstance(viewBinding.getRoot().getContext()).isLoggedIn() && post.getUser() != null
                && Objects.equals(SessionManager.getInstance(viewBinding.getRoot().getContext()).getUserId(), post.getUser().getId())) {
            viewBinding.modifyButtonGroup.setVisibility(View.VISIBLE);
        } else {
            viewBinding.modifyButtonGroup.setVisibility(View.GONE);
        }
    }
}