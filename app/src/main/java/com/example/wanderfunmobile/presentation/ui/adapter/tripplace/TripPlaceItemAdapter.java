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
import com.example.wanderfunmobile.data.dto.tripplace.TripPlaceDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.databinding.ItemTripPlaceBinding;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.presentation.ui.activity.trip.TripPlaceCreateActivity;

import org.parceler.Parcels;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class TripPlaceItemAdapter extends RecyclerView.Adapter<TripPlaceItemAdapter.TripPlaceItemViewHolder> {

    @Inject
    ObjectMapper objectMapper;
    private final List<TripPlace> tripPlaceList;
    private boolean editMode = false;

    private OnTripPlaceClickListener clickListener;

    public interface OnTripPlaceClickListener {
        void onClick(TripPlace tripPlace, int position);
    }

    public TripPlaceItemAdapter(List<TripPlace> tripPlaceList, ObjectMapper objectMapper, OnTripPlaceClickListener clickListener) {
        this.tripPlaceList = tripPlaceList;
        this.objectMapper = objectMapper;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TripPlaceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTripPlaceBinding binding = ItemTripPlaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TripPlaceItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripPlaceItemViewHolder holder, int position) {
        holder.bind(tripPlaceList.get(position), editMode, position);
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
        if (position >= 0 && position < tripPlaceList.size()) {
            tripPlaceList.remove(position);
            notifyItemRemoved(position);
            if (position - 1 >= 0) notifyItemChanged(position - 1);
        }
    }

    public void swapItem(int currentPos) {
        int nextPos = currentPos + 1;
        if (nextPos < tripPlaceList.size()) {
            Collections.swap(tripPlaceList, currentPos, nextPos);

            TripPlace first = tripPlaceList.get(currentPos);
            TripPlace second = tripPlaceList.get(nextPos);

            LocalDate tempStart = first.getStartTime();
            LocalDate tempEnd = first.getEndTime();

            first.setStartTime(second.getStartTime());
            first.setEndTime(second.getEndTime());

            second.setStartTime(tempStart);
            second.setEndTime(tempEnd);

            notifyItemMoved(currentPos, nextPos);
            notifyItemChanged(currentPos);
            notifyItemChanged(nextPos);
        }
    }

    class TripPlaceItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemTripPlaceBinding binding;

        public TripPlaceItemViewHolder(@NonNull ItemTripPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(TripPlace tripPlace, boolean isEditMode, int position) {
            binding.placeName.setText(
                    tripPlace.getPlace() != null ? tripPlace.getPlace().getName() : ""
            );

            binding.placeNote.setText(
                    tripPlace.getPlaceNotes() != null ? tripPlace.getPlaceNotes() : ""
            );

            binding.startTime.setText(
                    tripPlace.getStartTime() != null ? "Từ " + DateTimeUtil.localDateToString(tripPlace.getStartTime()) : ""
            );

            binding.endTime.setText(
                    tripPlace.getEndTime() != null ? "Đến " + DateTimeUtil.localDateToString(tripPlace.getEndTime()) : ""
            );

            if (tripPlace.getPlace() != null &&
                    tripPlace.getPlace().getCoverImage() != null &&
                    tripPlace.getPlace().getCoverImage().getImageUrl() != null) {
                Glide.with(binding.getRoot().getContext())
                        .load(tripPlace.getPlace().getCoverImage().getImageUrl())
                        .into(binding.placeCoverImage);
            } else {
                binding.placeCoverImage.setImageDrawable(null); // optional fallback
            }

            setupRemoveButton(isEditMode);
            setupSwapButton(isEditMode);
            setupEditClick(tripPlace, isEditMode, position);
        }

        private void setupRemoveButton(boolean isEditMode) {
            binding.removeButtonContainer.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
            binding.removeButtonContainer.findViewById(R.id.button).setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    removeItem(pos);
                    Log.d("TripPlaceItemAdapter", "Item removed at position: " + pos);
                }
            });
        }

        private void setupSwapButton(boolean isEditMode) {
            int currentPos = getBindingAdapterPosition();
            boolean canSwap = isEditMode && currentPos != RecyclerView.NO_POSITION && currentPos < getItemCount() - 1;

            binding.swapButton.setVisibility(canSwap ? View.VISIBLE : View.GONE);
            binding.swapButton.setOnClickListener(canSwap ? v -> swapItem(currentPos) : null);
        }

        private void setupEditClick(TripPlace tripPlace, boolean isEditMode, int position) {
            if (!isEditMode) {
                binding.getRoot().setOnClickListener(null);
                return;
            }

            binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onClick(tripPlace, position);
                }
            });
        }
    }
}
