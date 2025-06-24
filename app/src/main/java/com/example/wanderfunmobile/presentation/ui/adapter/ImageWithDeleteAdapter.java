package com.example.wanderfunmobile.presentation.ui.adapter;

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
    private OnImageListChangedListener changeListener;

    public interface OnImageListChangedListener {
        void onImageListChanged(int newSize);
    }

    public void setOnImageListChangedListener(OnImageListChangedListener listener) {
        this.changeListener = listener;
    }

    public ImageWithDeleteAdapter(List<Uri> imageList) {
        this.imageList = imageList;
    }

    public void addImage(Uri uri) {
        imageList.add(uri);
        notifyItemInserted(imageList.size() - 1);
        if (changeListener != null) {
            changeListener.onImageListChanged(imageList.size());
        }
    }

    public void addImages(List<Uri> uris) {
        int start = imageList.size();
        imageList.addAll(uris);
        notifyItemRangeInserted(start, uris.size());
        if (changeListener != null) {
            changeListener.onImageListChanged(imageList.size());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setImages(List<Uri> images) {
        imageList.clear();
        imageList.addAll(images);
        notifyDataSetChanged();
        if (changeListener != null) {
            changeListener.onImageListChanged(imageList.size());
        }
    }

    public List<Uri> getImageList() {
        return imageList;
    }

    @NonNull
    @Override
    public ImageWithDeleteAdapter.ImageWithDeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageWithDeleteBinding itemImageWithDeleteBinding = ItemImageWithDeleteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageWithDeleteViewHolder(itemImageWithDeleteBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageWithDeleteAdapter.ImageWithDeleteViewHolder holder, int position) {
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

                    if (changeListener != null) {
                        changeListener.onImageListChanged(imageList.size());
                    }
                }
            });
        }
    }
}

