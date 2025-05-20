package com.example.wanderfunmobile.presentation.ui.adapter.tripplace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ItemTripPlaceBinding;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.presentation.ui.activity.trip.TripPlaceCreateActivity;

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

    public void removeItem(int position) {
        tripPlaceList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position - 1);
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

            binding.removeButtonContainer.findViewById(R.id.button).setOnClickListener(v -> {
                adapter.removeItem(position);
                Log.d("TripPlaceItemAdapter", "Item removed at position: " + position);
            });

            // Swap Button Visibility Logic
            if (adapter.editMode) {
                int realPos = getBindingAdapterPosition();
                int itemCount = adapter.getItemCount();

                // Swap Button Visibility & Action
                if (itemCount > 1 && realPos != RecyclerView.NO_POSITION && realPos < itemCount - 1) {
                    binding.swapButton.setVisibility(View.VISIBLE);
                    binding.swapButton.setOnClickListener(v -> {
                        int currentPos = getBindingAdapterPosition();
                        int nextPos = currentPos + 1;

                        if (currentPos != RecyclerView.NO_POSITION && nextPos < adapter.getItemCount()) {
                            Collections.swap(adapter.tripPlaceList, currentPos, nextPos);
                            adapter.notifyItemMoved(currentPos, nextPos);

                            adapter.notifyItemChanged(currentPos);
                            adapter.notifyItemChanged(nextPos);
                        }
                    });
                } else {
                    binding.swapButton.setVisibility(View.GONE);
                    binding.swapButton.setOnClickListener(null);
                }
            }

            if (adapter.editMode) {
                binding.getRoot().setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), TripPlaceCreateActivity.class);
                    intent.putExtra("selected_place_name", tripPlace.getPlace().getName());
                    intent.putExtra("selected_place_notes", tripPlace.getPlaceNotes());
                    intent.putExtra("selected_place_start_time", DateTimeUtil.localDateToString(tripPlace.getStartTime()));
                    intent.putExtra("selected_place_end_time", DateTimeUtil.localDateToString(tripPlace.getEndTime()));
                    v.getContext().startActivity(intent);
                });
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

