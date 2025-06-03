package com.example.wanderfunmobile.presentation.ui.adapter.posts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.ItemCommentBinding;
import com.example.wanderfunmobile.domain.model.posts.Comment;

import java.util.List;
import java.util.Objects;

public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.CommentItemViewHolder> {
    private final List<Comment> commentList;

    public CommentItemAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public interface OnCommentLongClickListener {
        void onCommentLongClick(View anchorView, int position);
    }

    private OnCommentLongClickListener longClickListener;

    public void setOnCommentLongClickListener(OnCommentLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public CommentItemAdapter.CommentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommentItemAdapter.CommentItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentItemAdapter.CommentItemViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        if (comment != null) {
            holder.bind(comment);
        }
    }

    public class CommentItemViewHolder extends RecyclerView.ViewHolder {
        final ItemCommentBinding binding;

        public CommentItemViewHolder(@NonNull ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Comment comment) {
            binding.commentContainer.setOnLongClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (longClickListener != null
                        && position != RecyclerView.NO_POSITION
                        && Objects.equals(SessionManager.getInstance(binding.getRoot().getContext().getApplicationContext()).getUserId(), comment.getUser().getId())) {
                    longClickListener.onCommentLongClick(binding.commentContainer, position);
                }
                return true;
            });

            if (comment.getUser() != null && comment.getUser().getAvatarImage() != null) {
                Glide.with(binding.getRoot().getContext())
                        .load(comment.getUser().getAvatarImage())
                        .placeholder(R.drawable.ic_avatar)
                        .error(R.drawable.ic_avatar)
                        .into(binding.userAvatar);
            } else {
                binding.userAvatar.setImageResource(R.drawable.ic_avatar);
            }

            if (comment.getUser() != null) {
                binding.userName.setText(comment.getUser().getLastName() + " " + comment.getUser().getFirstName());
            } else {
                binding.userName.setText("Unknown User");
            }

            binding.content.setText(comment.getContent());

            if (comment.getCreateAt() != null) {
                if (comment.getUpdateAt() != null && !comment.getUpdateAt().equals(comment.getCreateAt())) {
                    binding.timestamp.setText(DateTimeUtil.timeAgoLocalDateTime(comment.getUpdateAt()));
                    binding.modifiedFlag.setVisibility(View.VISIBLE);
                } else{
                    binding.timestamp.setText(DateTimeUtil.timeAgoLocalDateTime(comment.getCreateAt()));
                    binding.modifiedFlag.setVisibility(View.GONE);
                }
            } else {
                binding.timestamp.setText("Đang gửi");
                binding.modifiedFlag.setVisibility(View.GONE);
                binding.likeButton.setVisibility(View.GONE);
            }

            if (SessionManager.getInstance(binding.getRoot().getContext().getApplicationContext()).isLoggedIn()) {
                binding.likeButton.setOnClickListener(v -> {
                    v.setVisibility(View.GONE);
                    binding.likeButtonHighlight.setVisibility(View.VISIBLE);
                });

                binding.likeButtonHighlight.setOnClickListener(v -> {
                    v.setVisibility(View.GONE);
                    binding.likeButton.setVisibility(View.VISIBLE);
                });

                if (comment.isLiked()) {
                    binding.likeButton.setVisibility(View.GONE);
                    binding.likeButtonHighlight.setVisibility(View.VISIBLE);
                } else {
                    binding.likeButton.setVisibility(View.VISIBLE);
                    binding.likeButtonHighlight.setVisibility(View.GONE);
                }
            } else {
                binding.likeButton.setVisibility(View.GONE);
                binding.likeButtonHighlight.setVisibility(View.GONE);
                binding.likeButton.setOnClickListener(view -> {});
                binding.likeButtonHighlight.setOnClickListener(view -> {});
            }

            if (comment.getLikeCount() != null && comment.getLikeCount() > 0) {
                binding.likeCount.setVisibility(View.VISIBLE);
                binding.likeCount.setText(String.valueOf(comment.getLikeCount()));
                binding.likeIcon.setVisibility(View.VISIBLE);
            } else {
                binding.likeCount.setVisibility(View.GONE);
                binding.likeIcon.setVisibility(View.GONE);
            }
        }
    }
}

