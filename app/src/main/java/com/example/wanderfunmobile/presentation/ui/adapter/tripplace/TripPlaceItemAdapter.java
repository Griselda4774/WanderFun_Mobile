package com.example.wanderfunmobile.presentation.ui.adapter.tripplace;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.databinding.ItemTripPlaceBinding;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;
import com.example.wanderfunmobile.core.util.DateTimeUtil;

import java.util.Collections;
import java.util.List;

public class TripPlaceItemAdapter extends RecyclerView.Adapter<TripPlaceItemAdapter.TripPlaceItemViewHolder> {
    private final List<TripPlace> tripPlaceList;
    private boolean editMode = false;

    public TripPlaceItemAdapter(List<TripPlace> tripPlaceList) {
        this.tripPlaceList = tripPlaceList;
    }

    @NonNull
    @Override
    public TripPlaceItemAdapter.TripPlaceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTripPlaceBinding binding = ItemTripPlaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TripPlaceItemAdapter.TripPlaceItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripPlaceItemAdapter.TripPlaceItemViewHolder holder, int position) {
        TripPlace tripPlace = tripPlaceList.get(position);
        if (editMode) {
            holder.enableEdit();
        } else {
            holder.disableEdit();
        }
        holder.bind(tripPlace, position, this);
    }

    @Override
    public int getItemCount() {
        return tripPlaceList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public void swapItems(int fromPosition, int toPosition) {
        Collections.swap(tripPlaceList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void removeItem(int position) {
        tripPlaceList.remove(position);
        notifyItemRemoved(position);
    }

    public static class TripPlaceItemViewHolder extends RecyclerView.ViewHolder {
        final ItemTripPlaceBinding binding;

        public TripPlaceItemViewHolder(@NonNull ItemTripPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(TripPlace tripPlace, int position, TripPlaceItemAdapter adapter) {
            if (tripPlace.getPlace() != null && tripPlace.getPlace().getName() != null) {
                binding.placeName.setText(tripPlace.getPlace().getName());
            }
            if (tripPlace.getStartTime() != null) {
                binding.startTime.setText("Từ " + DateTimeUtil.localDateToString(tripPlace.getStartTime()));
            }
            if (tripPlace.getEndTime() != null) {
                binding.endTime.setText("Đến " + DateTimeUtil.localDateToString(tripPlace.getEndTime()));
            }
            if (tripPlace.getPlace() != null && tripPlace.getPlace().getCoverImage() != null && tripPlace.getPlace().getCoverImage().getImageUrl() != null) {
                Glide.with(binding.getRoot().getContext()).load(tripPlace.getPlace().getCoverImage().getImageUrl()).into(binding.placeCoverImage);
            }

            binding.removeButtonContainer.setOnClickListener(v -> {
                adapter.removeItem(position);
                Log.d("TripPlaceItemAdapter", "Item removed at position: " + position);
            });

            // 🔀 Swap Button Visibility Logic
            if (adapter.getItemCount() > 1 && position != adapter.getItemCount() - 1) {
                binding.swapButton.setVisibility(View.VISIBLE);
                binding.swapButton.setOnClickListener(v -> {
                    int nextPos = position + 1;
                    if (nextPos < adapter.getItemCount()) {
                        Collections.swap(adapter.tripPlaceList, position, nextPos);
                        adapter.notifyItemMoved(position, nextPos);
                    }
                });
            } else {
                binding.swapButton.setVisibility(View.GONE);
            }

        }

        public void enableEdit() {
            binding.removeButtonContainer.setVisibility(View.VISIBLE);
        }

        public void disableEdit() {
            binding.removeButtonContainer.setVisibility(View.GONE);
        }
    }
}

