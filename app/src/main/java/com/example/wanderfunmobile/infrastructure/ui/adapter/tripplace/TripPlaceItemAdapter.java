package com.example.wanderfunmobile.infrastructure.ui.adapter.tripplace;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.databinding.ItemTripPlaceBinding;
import com.example.wanderfunmobile.domain.model.TripPlace;

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
        holder.bind(tripPlace);
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

    public static class TripPlaceItemViewHolder extends RecyclerView.ViewHolder {
        final ItemTripPlaceBinding binding;

        public TripPlaceItemViewHolder(@NonNull ItemTripPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TripPlace tripPlace) {
        }

        public void enableEdit() {
        }

        public void disableEdit() {
        }
    }
}

