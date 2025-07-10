package com.example.wanderfunmobile.presentation.ui.adapter.searchs;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wanderfunmobile.databinding.ItemSearchPlaceBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnSearchPlaceItemClickListener;

import java.util.List;

public class SearchPlaceItemAdapter extends RecyclerView.Adapter<SearchPlaceItemAdapter.SearchPlaceItemViewHolder> {
    private final List<Place> placeList;
    private OnSearchPlaceItemClickListener onSearchPlaceItemClickListener;

    public SearchPlaceItemAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public SearchPlaceItemAdapter.SearchPlaceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchPlaceBinding binding = ItemSearchPlaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchPlaceItemAdapter.SearchPlaceItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPlaceItemAdapter.SearchPlaceItemViewHolder holder, int position) {
        Place Place = placeList.get(position);
        holder.bind(Place);
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public void setOnSearchPlaceItemClickListener(OnSearchPlaceItemClickListener listener) {
        this.onSearchPlaceItemClickListener = listener;
    }

    public class SearchPlaceItemViewHolder extends RecyclerView.ViewHolder {
        final ItemSearchPlaceBinding binding;

        public SearchPlaceItemViewHolder(@NonNull ItemSearchPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Place place) {
            binding.itemSearchPlace.setText(place.getName());

            binding.itemSearchPlace.setOnClickListener(v -> {
                if (onSearchPlaceItemClickListener != null) {
                    onSearchPlaceItemClickListener.onSearchPlaceItemClick(place);
                }
            });
        }
    }
}