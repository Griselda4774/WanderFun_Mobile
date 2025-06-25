package com.example.wanderfunmobile.presentation.ui.adapter.album;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ItemAlbumCardBinding;
import com.example.wanderfunmobile.domain.model.albums.Album;
import com.example.wanderfunmobile.presentation.ui.activity.album.AlbumDetailsActivity;
import com.example.wanderfunmobile.core.util.DateTimeUtil;

import java.util.List;

public class AlbumItemAdapter extends RecyclerView.Adapter<AlbumItemAdapter.AlbumItemViewHolder> {
    private List<Album> albumsList;

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

    public void updateAlbumList(List<Album> newAlbumList) {
        this.albumsList = newAlbumList;
        notifyDataSetChanged();
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
            itemAlbumCardBinding.albumUpdateDate.setText("Cập nhật lần cuối: " + (album.getUpdatedAt() != null ? DateTimeUtil.localDateTimeToString(album.getUpdatedAt()) : ""));
            itemAlbumCardBinding.itemAlbumCard.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), AlbumDetailsActivity.class);
                intent.putExtra("albumId", album.getId());
                v.getContext().startActivity(intent);
            });

            if (album.getCoverImage() != null) {
                Glide.with(itemAlbumCardBinding.getRoot().getContext())
                        .load(album.getCoverImage().getImageUrl())
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(itemAlbumCardBinding.placeCoverImage);
            } else {
                itemAlbumCardBinding.placeCoverImage.setImageResource(R.drawable.img_placeholder);
            }
        }
    }
}


