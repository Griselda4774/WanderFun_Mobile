package com.example.wanderfunmobile.infrastructure.ui.adapter.place;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.databinding.ItemPlaceBinding;
import com.example.wanderfunmobile.domain.model.Place;
import com.example.wanderfunmobile.infrastructure.ui.activity.album.AddEditAlbumActivity;

import java.util.List;

public class PlaceItemAdapter extends RecyclerView.Adapter<PlaceItemAdapter.PlaceItemViewHolder> {
    private final List<Place> placeList;

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

    public static class PlaceItemViewHolder extends RecyclerView.ViewHolder {
        final ItemPlaceBinding itemPlaceBinding;

        Context context;

        public PlaceItemViewHolder(@NonNull ItemPlaceBinding binding) {
            super(binding.getRoot());
            this.itemPlaceBinding = binding;
            this.context = binding.getRoot().getContext();
        }

        public void bind(Place place) {
            itemPlaceBinding.placeName.setText(place.getName());
            itemPlaceBinding.placeRating.setText(String.valueOf(place.getAverageRating()));

            String imageUrl = place.getCoverImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemPlaceBinding.placeCoverImage.getContext())
                        .load(place.getCoverImageUrl())
                        .into(itemPlaceBinding.placeCoverImage);
            }

            itemPlaceBinding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, AddEditAlbumActivity.class);
                intent.putExtra("selected_place", place.getName());
                context.startActivity(intent);
            });
        }
    }
}
