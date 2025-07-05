package com.example.wanderfunmobile.presentation.ui.adapter.trip;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ItemTripBinding;
import com.example.wanderfunmobile.domain.model.trips.Trip;
import com.example.wanderfunmobile.presentation.ui.activity.trip.TripDetailActivity;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnPlaceSelectedListener;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnTripSelectedListener;

import java.util.List;

public class TripItemAdapter extends RecyclerView.Adapter<TripItemAdapter.TripItemViewHolder> {

    private final List<Trip> tripList;
    private OnTripSelectedListener onTripSelectedListener;

    public TripItemAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripItemAdapter.TripItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTripBinding binding = ItemTripBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TripItemAdapter.TripItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripItemAdapter.TripItemViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.bind(trip);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void setOnTripSelectedListener(OnTripSelectedListener listener) {
        this.onTripSelectedListener = listener;
    }

    public class TripItemViewHolder extends RecyclerView.ViewHolder {
        final ItemTripBinding binding;

        public TripItemViewHolder(@NonNull ItemTripBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Trip trip) {

            // Name
            TextView name = binding.name;
            if (trip.getName() != null && !trip.getName().isEmpty()) {
                name.setText(trip.getName());
            }

            // Start time
            TextView startTime = binding.startTime;
            if (trip.getStartTime() != null) {
                startTime.setText(DateTimeUtil.localDateToString(trip.getStartTime()));
            }

            // End time
            TextView endTime = binding.endTime;
            if (trip.getEndTime() != null) {
                endTime.setText(DateTimeUtil.localDateToString(trip.getEndTime()));
            }

            binding.getRoot().setOnClickListener(v -> {
                if (onTripSelectedListener != null) {
                    onTripSelectedListener.onTripSelected(trip);
                }
            });
        }
    }
}
