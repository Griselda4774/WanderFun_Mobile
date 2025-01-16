package com.example.wanderfunmobile.infrastructure.ui.adapter.album;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.databinding.ItemAlbumCardBinding;
import com.example.wanderfunmobile.domain.model.Album;

import java.util.List;

public class AlbumItemAdapter extends RecyclerView.Adapter<AlbumItemAdapter.AlbumItemViewHolder> {
    private final List<Album> albumsList;

    public AlbumItemAdapter(List<Album> albumsList) {
        this.albumsList = albumsList;
    }

    @NonNull
    @Override
    public AlbumItemAdapter.AlbumItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlbumCardBinding itemAlbumCardBinding = ItemAlbumCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlbumItemViewHolder(itemAlbumCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumItemAdapter.AlbumItemViewHolder holder, int position) {
        Album album = albumsList.get(position);
        holder.bind(album);
    }

    @Override
    public int getItemCount() {
        return albumsList.size();
    }

    public static class AlbumItemViewHolder extends RecyclerView.ViewHolder {
        final ItemAlbumCardBinding itemAlbumCardBinding;

        public AlbumItemViewHolder(@NonNull ItemAlbumCardBinding itemAlbumCardBinding) {
            super(itemAlbumCardBinding.getRoot());
            this.itemAlbumCardBinding = itemAlbumCardBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Album album) {
            itemAlbumCardBinding.albumName.setText(album.getName());
            itemAlbumCardBinding.albumUpdateDate.setText("Cập nhật lần cuối: " + (album.getLastModified() != null ? album.getLastModified().toString() : ""));

        }
    }
}


