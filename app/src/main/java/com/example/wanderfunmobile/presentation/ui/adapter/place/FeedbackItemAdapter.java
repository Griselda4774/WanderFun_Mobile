package com.example.wanderfunmobile.presentation.ui.adapter.place;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ItemFeedbackBinding;
import com.example.wanderfunmobile.domain.model.places.Feedback;
import com.example.wanderfunmobile.presentation.ui.custom.starrating.StarRatingView;
import com.example.wanderfunmobile.core.util.DateTimeUtil;

import java.util.List;

public class FeedbackItemAdapter extends RecyclerView.Adapter<FeedbackItemAdapter.FeedbackItemViewHolder> {
    private final List<Feedback> feedbackList;

    public FeedbackItemAdapter(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeedbackBinding binding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FeedbackItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackItemViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.bind(feedback);
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackItemViewHolder extends RecyclerView.ViewHolder {
        final ItemFeedbackBinding binding;

        public FeedbackItemViewHolder(@NonNull ItemFeedbackBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Feedback feedback) {
            // User avatar
            ImageView userAvatar = binding.feedbackUserAvatar;
            if (feedback.getUserAvatar() != null) {
                Glide.with(binding.getRoot())
                        .load(feedback.getUserAvatar())
                        .error(R.drawable.brown)
                        .into(userAvatar);
            }

            // User name
            TextView userName = binding.feedbackUserName;
            if (feedback.getUserName() != null && !feedback.getUserName().isEmpty()) {
                userName.setText(feedback.getUserName());
            }


            // Rating
            StarRatingView ratingView = binding.feedbackRating;
            int roundedRating = (int) Math.floor(feedback.getRating());
            ratingView.setRating(roundedRating);

            // Time
            TextView time = binding.feedbackTime;
            if (feedback.getTime() != null) {
                time.setText(DateTimeUtil.dateToString(feedback.getTime()));
            }

            // Comment
            TextView comment = binding.feedbackComment;
            if (feedback.getComment() != null && !feedback.getComment().isEmpty()) {
                comment.setVisibility(View.VISIBLE);
                comment.setText(feedback.getComment());
            } else {
                comment.setVisibility(View.GONE);
            }

            // Image
            ImageView image = binding.feedbackImage;
            if (feedback.getImageUrl() != null && !feedback.getImageUrl().isEmpty()) {
                Glide.with(binding.getRoot())
                        .load(feedback.getImageUrl())
                        .error(R.drawable.brown)
                        .into(image);
                image.setVisibility(ImageView.VISIBLE);
            }
        }
    }
}
