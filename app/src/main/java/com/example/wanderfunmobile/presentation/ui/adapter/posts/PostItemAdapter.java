package com.example.wanderfunmobile.presentation.ui.adapter.posts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.ItemPostBinding;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.presentation.ui.activity.post.PostDetailActivity;

import java.util.List;

public class PostItemAdapter extends RecyclerView.Adapter<PostItemAdapter.PostItemViewHolder> {
    private final List<Post> postList;

    public PostItemAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostItemAdapter.PostItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostItemAdapter.PostItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostItemAdapter.PostItemViewHolder holder, int position) {
        Post post = postList.get(position);
        if (post != null) {
            holder.bind(post);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostItemViewHolder extends RecyclerView.ViewHolder {
        final ItemPostBinding binding;

        public PostItemViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Post post) {
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(binding.getRoot().getContext(), PostDetailActivity.class);
                intent.putExtra("postId", post.getId());
                binding.getRoot().getContext().startActivity(intent);
            });

            // User info
            ImageView userAvatar = binding.userAvatar;
            TextView userName = binding.userName;
            if (post.getUser() != null) {
                if (post.getUser().getAvatarImage() != null) {
                    Glide.with(binding.getRoot())
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
            TextView dateCreated = binding.dateCreated;
            dateCreated.setText(DateTimeUtil.localDateTimeToString(post.getCreateAt()));

            // Place
            TextView placeName = binding.placeName;
            if (post.getPlace() != null) {
                binding.placeName.setVisibility(View.VISIBLE);
                binding.placeCheckInStatus.setVisibility(View.VISIBLE);
                binding.place.getRoot().setVisibility(View.VISIBLE);
                placeName.setText(post.getPlace().getName());
                binding.tripShareStatus.setVisibility(View.GONE);
                binding.trip.getRoot().setVisibility(View.GONE);
            } else {
                binding.placeName.setVisibility(View.GONE);
                binding.placeCheckInStatus.setVisibility(View.GONE);
                binding.place.getRoot().setVisibility(View.GONE);
            }

            // Trip
            if (post.getTrip() != null) {
                binding.placeName.setVisibility(View.GONE);
                binding.placeCheckInStatus.setVisibility(View.GONE);
                binding.place.getRoot().setVisibility(View.GONE);
                binding.tripShareStatus.setVisibility(View.VISIBLE);
                binding.trip.getRoot().setVisibility(View.VISIBLE);
            } else {
                binding.tripShareStatus.setVisibility(View.GONE);
                binding.trip.getRoot().setVisibility(View.GONE);
            }

            // Post content
            TextView content = binding.content;
            if (post.getContent() != null && !post.getContent().isEmpty()) {
                if (post.getContent().length() > 100) {
                    content.setText(post.getContent().substring(0, 100) + "...");
                    binding.seeMore.setVisibility(View.VISIBLE);
                    binding.seeLess.setVisibility(View.GONE);
                } else {
                    content.setText(post.getContent());
                    binding.seeMore.setVisibility(View.GONE);
                    binding.seeLess.setVisibility(View.GONE);
                }
            } else {
                content.setText("");
                binding.seeMore.setVisibility(View.GONE);
                binding.seeLess.setVisibility(View.GONE);
            }

            // See more button, see less button

            binding.seeMore.setOnClickListener(v -> {
                content.setText(post.getContent());
                binding.seeMore.setVisibility(View.GONE);
                binding.seeLess.setVisibility(View.VISIBLE);
            });

            binding.seeLess.setOnClickListener(v -> {
                content.setText(post.getContent().substring(0, 100) + "...");
                binding.seeMore.setVisibility(View.VISIBLE);
                binding.seeLess.setVisibility(View.GONE);
            });

            // Post image
            ImageView image = binding.image;
            if (post.getImage() != null) {
                Glide.with(binding.getRoot())
                        .load(post.getImage().getImageUrl())
                        .error(R.drawable.brown)
                        .into(image);
            } else {
                image.setVisibility(View.GONE);
            }

            // Post like count
            TextView likeCount = binding.likeCount;
            if (post.getLikeCount() > 0) {
                likeCount.setText(String.valueOf(post.getLikeCount()));
            } else {
                likeCount.setText("0");
            }

            // Post comment count
            TextView commentCount = binding.commentCount;
            if (post.getCommentCount() > 0) {
                commentCount.setText(String.valueOf(post.getCommentCount()));
            } else {
                commentCount.setText("0");
            }

            // Post deltail button
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(binding.getRoot().getContext(), PostDetailActivity.class);
                intent.putExtra("postId", post.getId());
                binding.getRoot().getContext().startActivity(intent);
            });

            // Like button
            if (post.isLiked()) {
                binding.likeButton.setVisibility(View.GONE);
                binding.likeButtonHighlight.setVisibility(View.VISIBLE);
            } else {
                binding.likeButton.setVisibility(View.VISIBLE);
                binding.likeButtonHighlight.setVisibility(View.GONE);
            }

            binding.likeButton.setOnClickListener(v -> {
                binding.likeButton.setVisibility(View.GONE);
                binding.likeButtonHighlight.setVisibility(View.VISIBLE);
            });

            binding.likeButtonHighlight.setOnClickListener(v -> {
                binding.likeButton.setVisibility(View.VISIBLE);
                binding.likeButtonHighlight.setVisibility(View.GONE);
            });

            // Comment button
            binding.commentButton.setOnClickListener(v -> {
                Intent intent = new Intent(binding.getRoot().getContext(), PostDetailActivity.class);
                intent.putExtra("postId", post.getId());
                binding.getRoot().getContext().startActivity(intent);
            });

            if (SessionManager.getInstance(binding.getRoot().getContext()).isLoggedIn()) {
                binding.likeCommentButtonWrapper.setVisibility(View.VISIBLE);
            } else {
                binding.likeCommentButtonWrapper.setVisibility(View.GONE);
            }
        }
    }
}
