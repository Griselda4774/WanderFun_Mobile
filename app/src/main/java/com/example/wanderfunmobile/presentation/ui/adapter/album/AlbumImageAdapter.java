package com.example.wanderfunmobile.presentation.ui.adapter.album;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.databinding.ItemAlbumImageBinding;
import com.example.wanderfunmobile.domain.model.AlbumImage;

import java.util.List;

public class AlbumImageAdapter extends RecyclerView.Adapter<AlbumImageAdapter.AlbumImageViewHolder> {
    private final List<AlbumImage> albumsList;

    public AlbumImageAdapter(List<AlbumImage> albumsList) {
        this.albumsList = albumsList;
    }

    @NonNull
    @Override
    public AlbumImageAdapter.AlbumImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlbumImageBinding itemAlbumImageBinding = ItemAlbumImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlbumImageAdapter.AlbumImageViewHolder(itemAlbumImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumImageAdapter.AlbumImageViewHolder holder, int position) {
        AlbumImage albumImage = albumsList.get(position);
        holder.bind(albumImage);
    }

    @Override
    public int getItemCount() {
        return albumsList.size();
    }

    public static class AlbumImageViewHolder extends RecyclerView.ViewHolder {
        final ItemAlbumImageBinding itemAlbumImageBinding;

        public AlbumImageViewHolder(@NonNull ItemAlbumImageBinding itemAlbumImageBinding) {
            super(itemAlbumImageBinding.getRoot());
            this.itemAlbumImageBinding = itemAlbumImageBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(AlbumImage albumImage) {
            Glide.with(itemAlbumImageBinding.albumImage.getContext())
                    .load(albumImage.getImageUrl())
                    .into(itemAlbumImageBinding.albumImage);
        }
    }
}
