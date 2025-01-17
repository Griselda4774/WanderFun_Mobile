package com.example.wanderfunmobile.infrastructure.ui.adapter.place;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.databinding.ItemRatingBarBinding;

import java.util.List;

public class RatingBarItemAdapter extends RecyclerView.Adapter<RatingBarItemAdapter.RatingBarItemViewHolder> {
    private final List<Float> ratingList;

    public RatingBarItemAdapter(List<Float> ratingList) {
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public RatingBarItemAdapter.RatingBarItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRatingBarBinding binding = ItemRatingBarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RatingBarItemAdapter.RatingBarItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingBarItemAdapter.RatingBarItemViewHolder holder, int position) {
        Float rating = ratingList.get(position);
        holder.bind(rating);
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public static class RatingBarItemViewHolder extends RecyclerView.ViewHolder {
        final ItemRatingBarBinding binding;

        public RatingBarItemViewHolder(@NonNull ItemRatingBarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Float rating) {
            View ratingBar = binding.ratingBar;
            ConstraintLayout.LayoutParams layoutParams =
                    (ConstraintLayout.LayoutParams) ratingBar.getLayoutParams();

            layoutParams.matchConstraintPercentWidth = Math.max(0f, Math.min(1f, rating));

            ratingBar.setLayoutParams(layoutParams);
        }
    }
}
