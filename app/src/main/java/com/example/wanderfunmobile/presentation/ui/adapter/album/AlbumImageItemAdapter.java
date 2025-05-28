package com.example.wanderfunmobile.presentation.ui.adapter.album;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.databinding.ItemAlbumImageBinding;
import com.example.wanderfunmobile.domain.model.albums.AlbumImage;

import java.util.List;

public class AlbumImageItemAdapter extends RecyclerView.Adapter<AlbumImageItemAdapter.AlbumImageViewHolder> {
    private final List<AlbumImage> albumImageList;

    public AlbumImageItemAdapter(List<AlbumImage> albumImageList) {
        this.albumImageList = albumImageList;
    }

    @NonNull
    @Override
    public AlbumImageItemAdapter.AlbumImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlbumImageBinding itemAlbumImageBinding = ItemAlbumImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlbumImageViewHolder(itemAlbumImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumImageItemAdapter.AlbumImageViewHolder holder, int position) {
        AlbumImage albumImage = albumImageList.get(position);
        holder.bind(albumImage);
    }

    @Override
    public int getItemCount() {
        return albumImageList.size();
    }

    public static class AlbumImageViewHolder extends RecyclerView.ViewHolder {
        final ItemAlbumImageBinding binding;

        public AlbumImageViewHolder(@NonNull ItemAlbumImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(AlbumImage albumImage) {
            Glide.with(binding.albumImage.getContext())
                    .load(albumImage.getImageUrl())
                    .into(binding.albumImage);
        }
    }
}
