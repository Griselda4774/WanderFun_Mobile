package com.example.wanderfunmobile.infrastructure.ui.adapter.place;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ItemPlaceDescriptionBinding;
import com.example.wanderfunmobile.domain.model.Section;

import java.util.List;

public class PlaceDescriptionItemAdapter extends RecyclerView.Adapter<PlaceDescriptionItemAdapter.PlaceDescriptionItemViewHolder> {
    private final List<Section> descriptionList;

    public PlaceDescriptionItemAdapter(List<Section> descriptionList) {
        this.descriptionList = descriptionList;
    }

    @NonNull
    @Override
    public PlaceDescriptionItemAdapter.PlaceDescriptionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaceDescriptionBinding binding = ItemPlaceDescriptionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaceDescriptionItemAdapter.PlaceDescriptionItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceDescriptionItemAdapter.PlaceDescriptionItemViewHolder holder, int position) {
        Section description = descriptionList.get(position);
        holder.bind(description);
    }

    @Override
    public int getItemCount() {
        return descriptionList.size();
    }

    public static class PlaceDescriptionItemViewHolder extends RecyclerView.ViewHolder {
        final ItemPlaceDescriptionBinding binding;

        public PlaceDescriptionItemViewHolder(@NonNull ItemPlaceDescriptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Section description) {
            TextView title = binding.title;
            if (description.getTitle() != null) {
                title.setText(description.getTitle());
            }

            TextView content = binding.content;
            if (description.getContent() != null) {
                content.setText(description.getContent());
            }

            ImageView image = binding.image;
            if (description.getImageUrl() != null) {
                Glide.with(binding.getRoot())
                        .load(description.getImageUrl())
                        .error(R.drawable.brown)
                        .into(image);
                image.setVisibility(ImageView.VISIBLE);
            }
        }
    }
}
