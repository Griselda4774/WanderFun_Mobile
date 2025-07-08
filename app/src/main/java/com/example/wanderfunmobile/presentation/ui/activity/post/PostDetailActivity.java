package com.example.wanderfunmobile.presentation.ui.activity.post;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.core.util.StringUtil;
import com.example.wanderfunmobile.databinding.ActivityPostDetailBinding;
import com.example.wanderfunmobile.databinding.PopupCommentMenuBinding;
import com.example.wanderfunmobile.domain.model.posts.Comment;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.domain.model.users.User;
import com.example.wanderfunmobile.presentation.ui.adapter.posts.CommentItemAdapter;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.viewmodel.posts.CommentViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.posts.PostViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    private Comment oldComment;
    private int clickedCommentPosition = -1;
    private View dimBackground;
    private PopupWindow popupWindow;
    private LoadingDialog loadingDialog;

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

        setupActivity();

        setupAdapter();

        setupViewModel();

        getPost();

        getComment();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (postId > 0) {
            postViewModel.findPostById(postId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void setupActivity() {
        // Dim background
        dimBackground = viewBinding.dimBackground;
        dimBackground.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return true;
            }
        });

        // Loading dialog
        loadingDialog = new LoadingDialog(this);

        // Back button
        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });

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

        // Edit and delete buttons
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
                loadingDialog.show();
                postViewModel.deletePost("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), postId);
            }
            viewBinding.selectionDialog.hide();
            Log.d("SelectionDialog", "Reject");
        });

        // Comment input && send button
        if (SessionManager.getInstance(getApplicationContext()).isLoggedIn()) {
            viewBinding.commentContainer.setVisibility(View.VISIBLE);
        } else {
            viewBinding.commentContainer.setVisibility(View.GONE);
        }

        ImageViewCompat.setImageTintList(viewBinding.sendCommentButton,
                ContextCompat.getColorStateList(this, R.color.black1));
        viewBinding.sendCommentButton.setClickable(false);

        viewBinding.commentInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewBinding.sendCommentButton.setEnabled(s.length() > 0);
                ImageViewCompat.setImageTintList(viewBinding.sendCommentButton,
                        ContextCompat.getColorStateList(PostDetailActivity.this, s.length() > 0 ? R.color.blue2 : R.color.black1));
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        viewBinding.sendCommentButton.setOnClickListener(v -> {
            View currentFocus = this.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
            }

            String commentContent = viewBinding.commentInput.getText().toString().trim();
            if (commentContent.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Nội dung bình luận không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            if (oldComment != null && clickedCommentPosition >= 0) {
                prepareUpdateComment(commentContent);
            } else {
                prepareCreateComment(commentContent);
            }
        });
    }

    private void setupAdapter() {
        viewBinding.commentList.setLayoutManager(new LinearLayoutManager(this));
        commentItemAdapter = new CommentItemAdapter(commentList);
        commentItemAdapter.setOnCommentLongClickListener((anchorView, position) -> {
            clickedCommentPosition = position;
            showPopupMenu(anchorView, position);
        });
        viewBinding.commentList.setAdapter(commentItemAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupViewModel() {
        // Post ViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Observe find post by ID
        postViewModel.getFindPostByIdLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                post = result.getData();
                bindPostData();
                loadingDialog.hide();
            } else {
                loadingDialog.hide();
                Toast.makeText(getApplicationContext(), "Bài viết đã bị xóa!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Observe delete post by id
        postViewModel.getDeletePostLiveData().observe(this, result -> {
            if (!result.isError()) {
                loadingDialog.hide();
                Toast.makeText(getApplicationContext(), "Xóa bài viết thành công", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("status", "post_deleted");
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                loadingDialog.hide();
                Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        // Comment ViewModel
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        // Observe find all comments by post ID
        commentViewModel.getFindAllCommentsByPostIdLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                commentList.clear();
                commentList.addAll(result.getData());
                commentItemAdapter.notifyDataSetChanged();
                viewBinding.commentCount.setText(String.valueOf(commentList.size()));
            } else {
                Toast.makeText(getApplicationContext(), "Không thể tải bình luận!", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe create comment
        commentViewModel.getCreateCommentLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                Comment newComment = result.getData();

                for (int i = 0; i < commentList.size(); i++) {
                    Comment curentComment = commentList.get(i);
                    if (curentComment.isPending() && newComment.getLocalId().equals(curentComment.getLocalId())) {
                        commentList.set(i, newComment);
                        commentItemAdapter.notifyItemChanged(i);
                        break;
                    }
                }

                Toast.makeText(getApplicationContext(), "Tạo bình luận thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Không thể tạo bình luận!", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < commentList.size(); i++) {
                    Comment currentComment = commentList.get(i);
                    if (currentComment.isPending()) {
                        commentList.remove(i);
                        commentItemAdapter.notifyItemRemoved(i);
                        viewBinding.commentCount.setText(String.valueOf(commentList.size()));
                        break;
                    }
                }
            }
        });

        // Observe update comment
        commentViewModel.getUpdateCommentLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                Comment newComment = result.getData();

                for (int i = 0; i < commentList.size(); i++) {
                    Comment curentComment = commentList.get(i);
                    if (curentComment.isPending() && newComment.getLocalId().equals(curentComment.getLocalId())) {
                        commentList.set(i, newComment);
                        commentItemAdapter.notifyItemChanged(i);
                        break;
                    }
                }

                Toast.makeText(getApplicationContext(), "Sửa bình luận thành công", Toast.LENGTH_SHORT).show();
                oldComment = null; // Clear old comment after successful update
            } else {
                Toast.makeText(getApplicationContext(), "Không thể sửa bình luận!", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < commentList.size(); i++) {
                    Comment currentComment = commentList.get(i);
                    if (currentComment.isPending()) {
                        commentList.set(i, oldComment);
                        commentItemAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        });

        // Observe delete comment
        commentViewModel.getDeleteCommentLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                String deletedLocalId = result.getData().getLocalId();

                for (int i = 0; i < commentList.size(); i++) {
                    Comment curentComment = commentList.get(i);
                    if (deletedLocalId.equals(curentComment.getLocalId())) {
                        commentList.remove(i);
                        commentItemAdapter.notifyItemRemoved(i);
                        viewBinding.commentCount.setText(String.valueOf(commentList.size()));
                        break;
                    }
                }
                Toast.makeText(getApplicationContext(), "Xóa bình luận thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Không thể xóa bình luận!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPost() {
        loadingDialog.show();
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
            viewBinding.placeLayout.setVisibility(View.VISIBLE);
            viewBinding.placeName.setVisibility(View.VISIBLE);
            viewBinding.placeCheckInStatus.setVisibility(View.VISIBLE);
            viewBinding.place.getRoot().setVisibility(View.VISIBLE);

            viewBinding.place.placeName.setText(post.getPlace().getName());
            viewBinding.place.placeRating.setText(String.valueOf(post.getPlace().getRating()));
            viewBinding.place.address.setText(StringUtil.formatAddressToStringNoStreet(post.getPlace().getAddress()));
            if (post.getPlace().getCoverImage() != null) {
                Glide.with(viewBinding.getRoot())
                        .load(post.getPlace().getCoverImage().getImageUrl())
                        .error(R.drawable.img_placeholder)
                        .into(viewBinding.place.placeCoverImage);
            }

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
            viewBinding.tripLayout.setVisibility(View.VISIBLE);
            viewBinding.tripShareStatus.setVisibility(View.VISIBLE);
            viewBinding.trip.getRoot().setVisibility(View.VISIBLE);

            viewBinding.trip.name.setText(post.getTrip().getName());
            viewBinding.trip.startTime.setText(DateTimeUtil.localDateToString(post.getTrip().getStartTime()));
            viewBinding.trip.endTime.setText(DateTimeUtil.localDateToString(post.getTrip().getEndTime()));
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
            viewBinding.likeCount.setText(String.valueOf(post.getLikeCount() + 1));
        });

        viewBinding.likeButtonHighlight.setOnClickListener(v -> {
            viewBinding.likeButton.setVisibility(View.VISIBLE);
            viewBinding.likeButtonHighlight.setVisibility(View.GONE);
            viewBinding.likeCount.setText(String.valueOf(post.getLikeCount()));
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

    private void showPopupMenu(View anchorView, int position) {
        @SuppressLint("InflateParams")
        PopupCommentMenuBinding binding = PopupCommentMenuBinding.inflate(LayoutInflater.from(this));


        popupWindow = new PopupWindow(binding.getRoot(),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        dimBackground.setVisibility(View.VISIBLE);

        LinearLayout editOption = binding.menuEdit;
        LinearLayout deleteOption = binding.menuDelete;

        editOption.setOnClickListener(v -> {
            oldComment = commentList.get(position);
            popupWindow.dismiss();

            viewBinding.sendCommentButton.setEnabled(false);
            ImageViewCompat.setImageTintList(viewBinding.sendCommentButton,
                    ContextCompat.getColorStateList(PostDetailActivity.this, R.color.black1));

            viewBinding.commentInput.setText(commentList.get(position).getContent());
            viewBinding.commentInput.setSelection(viewBinding.commentInput.getText().length());
            viewBinding.commentInput.requestFocus();
            viewBinding.commentInput.post(() -> {
                InputMethodManager imm = (InputMethodManager) viewBinding.getRoot().getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(viewBinding.commentInput, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        });

        deleteOption.setOnClickListener(v -> {
            String bearerToken = "Bearer " + SessionManager.getInstance(viewBinding.getRoot().getContext()).getAccessToken();

            String localId = UUID.randomUUID().toString();

            commentList.get(position).setLocalId(localId);

            commentViewModel.deleteComment(bearerToken, commentList.get(position).getId(), localId);

            popupWindow.dismiss();
        });

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);

        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorX = location[0];
        int anchorY = location[1];
        int anchorWidth = anchorView.getWidth();

        binding.getRoot().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = binding.getRoot().getMeasuredWidth();
        int popupHeight = binding.getRoot().getMeasuredHeight();

        // Calculate the xOffset to align the popup with the right edge of the anchor view
        int xOffset = anchorX + anchorWidth - popupWidth;

        // Always show the popup above the anchor view
        int yOffset = anchorY - popupHeight;

        popupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, xOffset, yOffset);

        popupWindow.setOnDismissListener(() -> dimBackground.setVisibility(View.GONE));
    }

    private void prepareCreateComment(String commentContent) {
        String bearerToken = "Bearer " + SessionManager.getInstance(viewBinding.getRoot().getContext()).getAccessToken();

        String localId = UUID.randomUUID().toString();

        Comment tempComment = new Comment();
        tempComment.setContent(commentContent);
        tempComment.setLocalId(localId);
        tempComment.setPending(true);
        tempComment.setUser(new User());
        tempComment.getUser().setId(SessionManager.getInstance(viewBinding.getRoot().getContext()).getUserId());

        commentList.add(0, tempComment);
        commentItemAdapter.notifyItemInserted(0);
        viewBinding.commentCount.setText(String.valueOf(commentList.size()));

        viewBinding.scrollView.post(() -> {
            TextView target = viewBinding.commentPart;

            int[] location = new int[2];
            target.getLocationOnScreen(location);
            int targetYOnScreen = location[1];

            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

            int targetCenterYOnScreen = targetYOnScreen + (target.getHeight() / 2);
            int scrollOffset = targetCenterYOnScreen - (screenHeight / 2);

            viewBinding.scrollView.smoothScrollBy(0, scrollOffset);
        });

        commentViewModel.createComment(bearerToken, postId, tempComment, localId);

        clickedCommentPosition = -1;
        viewBinding.commentInput.setText("");
    }

    private void prepareUpdateComment(String commentContent) {
        String bearerToken = "Bearer " + SessionManager.getInstance(viewBinding.getRoot().getContext()).getAccessToken();

        String localId = UUID.randomUUID().toString();

        Comment tempComment = new Comment();
        tempComment.setLocalId(localId);
        tempComment.setContent(commentContent);
        tempComment.setPending(true);
        tempComment.setUser(new User());
        tempComment.getUser().setId(SessionManager.getInstance(viewBinding.getRoot().getContext()).getUserId());

        commentList.set(clickedCommentPosition, tempComment);
        commentItemAdapter.notifyItemChanged(clickedCommentPosition);

        viewBinding.scrollView.post(() -> {
            View target = Objects.requireNonNull(viewBinding.commentList.findViewHolderForAdapterPosition(clickedCommentPosition)).itemView;

            int[] location = new int[2];
            target.getLocationOnScreen(location);
            int targetYOnScreen = location[1];

            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

            int targetCenterYOnScreen = targetYOnScreen + (target.getHeight() / 2);
            int scrollOffset = targetCenterYOnScreen - (screenHeight / 2);

            viewBinding.scrollView.smoothScrollBy(0, scrollOffset);
        });

        commentViewModel.updateComment(bearerToken, oldComment.getId(), tempComment, localId);

        viewBinding.commentInput.setText("");
    }
}