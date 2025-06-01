package com.example.wanderfunmobile.presentation.ui.activity.post;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.PostViewManager;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.ActivityPostDetailBinding;
import com.example.wanderfunmobile.databinding.ButtonBackRoundedBinding;
import com.example.wanderfunmobile.domain.model.posts.Comment;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.presentation.ui.adapter.posts.CommentItemAdapter;
import com.example.wanderfunmobile.presentation.ui.adapter.posts.PostItemAdapter;
import com.example.wanderfunmobile.presentation.viewmodel.CommentViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PostDetailActivity extends AppCompatActivity {

    private ActivityPostDetailBinding viewBinding;
    private long postId;
    private Post post;
    private PostViewModel postViewModel;
    private CommentViewModel commentViewModel;
    private CommentItemAdapter commentItemAdapter;
    private final List<Comment> commentList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            boolean isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime());

            View inputBar = viewBinding.commentContainer;
            int paddingBottom = isKeyboardVisible
                    ? imeInsets.bottom
                    : (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8,
                    inputBar.getResources().getDisplayMetrics()
            );
            inputBar.setPadding(
                    inputBar.getPaddingLeft(),
                    inputBar.getPaddingTop(),
                    inputBar.getPaddingRight(),
                    paddingBottom
            );

            return insets;
        });

        viewBinding.commentList.setLayoutManager(new LinearLayoutManager(this));
        commentItemAdapter = new CommentItemAdapter(commentList);
        viewBinding.commentList.setAdapter(commentItemAdapter);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getFindPostByIdLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                post = result.getData();
                bindPostData();
                hideLoadingDialog();
            } else {
                hideLoadingDialog();
                Toast.makeText(getApplicationContext(), "Bài viết đã bị xóa!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        postViewModel.getDeletePostLiveData().observe(this, result -> {
            if (!result.isError()) {
                hideLoadingDialog();
                Toast.makeText(getApplicationContext(), "Xóa bài viết thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                hideLoadingDialog();
                Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        commentViewModel.getFindAllCommentsByPostIdLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                commentList.clear();
                commentList.addAll(result.getData());
                commentItemAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "Không thể tải bình luận!", Toast.LENGTH_SHORT).show();
            }
        });

        getPost();
        getComment();

        // Like comment button wrapper, comment container
        if (SessionManager.getInstance(viewBinding.getRoot().getContext()).isLoggedIn()) {
            viewBinding.likeCommentButtonWrapper.setVisibility(View.VISIBLE);
            viewBinding.commentContainer.setVisibility(View.VISIBLE);
        } else {
            viewBinding.likeCommentButtonWrapper.setVisibility(View.GONE);
            viewBinding.commentContainer.setVisibility(View.GONE);
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

        viewBinding.editButton.setOnClickListener(v -> {
            if (post != null) {
                Intent intent = new Intent(viewBinding.getRoot().getContext(), AddEditPostActivity.class);
                intent.putExtra("postId", post.getId());
                startActivity(intent);
            }
        });

        viewBinding.deleteButton.setOnClickListener(v -> {
            viewBinding.selectionDialog.show("Xóa bài viết",
                    "Bạn chắc chắn chứ?",
                    "Bài viết sẽ bị xóa vĩnh viễn và không thể khôi phục lại.",
                    "Hủy",
                    "Vẫn xóa");
        });

        // Selection dialog
        viewBinding.selectionDialog.setOnAcceptListener(() -> {
            viewBinding.selectionDialog.hide();
            Log.d("SelectionDialog", "Accept");
        });

        viewBinding.selectionDialog.setOnRejectListener(() -> {
            if (postId > 0) {
                showLoadingDialog();
                postViewModel.deletePost("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), postId);
            }
            viewBinding.selectionDialog.hide();
            Log.d("SelectionDialog", "Reject");
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (postId > 0) {
            postViewModel.findPostById(postId);
        }
    }

    private void getPost() {
        showLoadingDialog();
        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getLongExtra("postId", -1);
            if (postId > 0) {
                postViewModel.findPostById(postId);
            }
        }
    }

    private void getComment() {
        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getLongExtra("postId", -1);
            if (postId > 0) {
                String bearerToken = "Bearer " + SessionManager.getInstance(viewBinding.getRoot().getContext()).getAccessToken();
                commentViewModel.findAllCommentsByPostId(bearerToken ,postId);
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

            userName.setText(post.getUser().getLastName() + " " + post.getUser().getFirstName());
        } else {
            userAvatar.setImageResource(R.drawable.ic_avatar);
            userName.setText("Unknown User");
        }

        // Post info
        // Date created
        TextView dateCreated = viewBinding.dateCreated;
        dateCreated.setText(DateTimeUtil.localDateTimeToString(post.getCreateAt()));

        // Place
        TextView placeName = viewBinding.placeName;
        if (post.getPlace() != null) {
            viewBinding.placeName.setVisibility(View.VISIBLE);
            viewBinding.placeCheckInStatus.setVisibility(View.VISIBLE);
            viewBinding.place.getRoot().setVisibility(View.VISIBLE);
            placeName.setText(post.getPlace().getName());
            viewBinding.tripShareStatus.setVisibility(View.GONE);
            viewBinding.trip.getRoot().setVisibility(View.GONE);
        } else {
            viewBinding.placeName.setVisibility(View.GONE);
            viewBinding.placeCheckInStatus.setVisibility(View.GONE);
            viewBinding.place.getRoot().setVisibility(View.GONE);
        }

        // Trip
        if (post.getTrip() != null) {
            viewBinding.placeName.setVisibility(View.GONE);
            viewBinding.placeCheckInStatus.setVisibility(View.GONE);
            viewBinding.place.getRoot().setVisibility(View.GONE);
            viewBinding.tripShareStatus.setVisibility(View.VISIBLE);
            viewBinding.trip.getRoot().setVisibility(View.VISIBLE);
        } else {
            viewBinding.tripShareStatus.setVisibility(View.GONE);
            viewBinding.trip.getRoot().setVisibility(View.GONE);
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
            viewBinding.seeMore.setVisibility(View.GONE);
            viewBinding.seeLess.setVisibility(View.GONE);
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

    private void showLoadingDialog() {
        viewBinding.loadingDialog.setVisibility(View.VISIBLE);
        viewBinding.loadingDialog.show();
    }

    private void hideLoadingDialog() {
        viewBinding.loadingDialog.setVisibility(View.GONE);
        viewBinding.loadingDialog.hide();
    }
}