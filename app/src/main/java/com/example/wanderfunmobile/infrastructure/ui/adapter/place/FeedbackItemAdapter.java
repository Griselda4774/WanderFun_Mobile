package com.example.wanderfunmobile.infrastructure.ui.adapter.place;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.databinding.ItemFeedbackBinding;
import com.example.wanderfunmobile.domain.model.Feedback;

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
        }
    }
}
