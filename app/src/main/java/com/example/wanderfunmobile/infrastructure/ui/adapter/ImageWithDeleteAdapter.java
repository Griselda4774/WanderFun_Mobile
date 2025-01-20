package com.example.wanderfunmobile.infrastructure.ui.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ItemImageWithDeleteBinding;

import java.util.List;

public class ImageWithDeleteAdapter extends RecyclerView.Adapter<ImageWithDeleteAdapter.ImageWithDeleteViewHolder> {
    private final List<Uri> imageList;

    public ImageWithDeleteAdapter(List<Uri> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageWithDeleteAdapter.ImageWithDeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageWithDeleteBinding itemImageWithDeleteBinding = ItemImageWithDeleteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageWithDeleteViewHolder(itemImageWithDeleteBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.wanderfunmobile.infrastructure.ui.adapter.ImageWithDeleteAdapter.ImageWithDeleteViewHolder holder, int position) {
        Uri image = imageList.get(position);
        holder.bind(image);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageWithDeleteViewHolder extends RecyclerView.ViewHolder {
        final ItemImageWithDeleteBinding itemImageWithDeleteBinding;

        public ImageWithDeleteViewHolder(@NonNull ItemImageWithDeleteBinding itemImageWithDeleteBinding) {
            super(itemImageWithDeleteBinding.getRoot());
            this.itemImageWithDeleteBinding = itemImageWithDeleteBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Uri image) {
            Glide.with(itemImageWithDeleteBinding.image.getContext())
                    .load(image)
                    .into(itemImageWithDeleteBinding.image);

            itemImageWithDeleteBinding.removeButtonContainer.findViewById(R.id.button).setOnClickListener(v -> {
                int adapterPosition = getBindingAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    imageList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, imageList.size());
                }
            });
        }
    }
}

