package com.example.wanderfunmobile.presentation.ui.adapter.place;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.StringUtil;
import com.example.wanderfunmobile.databinding.ItemPlaceBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnPlaceSelectedListener;

import java.util.List;

public class PlaceItemAdapter extends RecyclerView.Adapter<PlaceItemAdapter.PlaceItemViewHolder> {
    private final List<Place> placeList;
    private OnPlaceSelectedListener onPlaceSelectedListener;

    public PlaceItemAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaceBinding binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaceItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceItemViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.bind(place);
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public void setOnPlaceSelectedListener(OnPlaceSelectedListener listener) {
        this.onPlaceSelectedListener = listener;
    }

    public class PlaceItemViewHolder extends RecyclerView.ViewHolder {
        final ItemPlaceBinding itemPlaceBinding;

        public PlaceItemViewHolder(@NonNull ItemPlaceBinding binding) {
            super(binding.getRoot());
            this.itemPlaceBinding = binding;
        }

        public void bind(Place place) {
            itemPlaceBinding.placeName.setText(place.getName());

            itemPlaceBinding.placeRating.setText(String.valueOf(place.getRating()));

            if (place.getCoverImage() != null) {
                Glide.with(itemPlaceBinding.placeCoverImage.getContext())
                        .load(place.getCoverImage().getImageUrl())
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(itemPlaceBinding.placeCoverImage);
            } else {
                itemPlaceBinding.placeCoverImage.setImageResource(R.drawable.img_placeholder);
            }

            itemPlaceBinding.placeVisitedCount.setText(String.valueOf(place.getCheckInCount()));

            if (place.getAddress() != null) {
                itemPlaceBinding.address.setText(StringUtil.formatAddressToStringNoStreet(place.getAddress()));
            } else {
                itemPlaceBinding.address.setVisibility(View.GONE);
            }

            itemPlaceBinding.getRoot().setOnClickListener(v -> {
                if (onPlaceSelectedListener != null) {
                    onPlaceSelectedListener.onPlaceSelected(place);
                }
            });
        }
    }
}
