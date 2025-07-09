package com.example.wanderfunmobile.presentation.ui.adapter.place;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ItemSectionBinding;
import com.example.wanderfunmobile.domain.model.places.Section;

import java.util.List;

public class SectionItemAdapter extends RecyclerView.Adapter<SectionItemAdapter.SectionItemViewHolder> {
    private final List<Section> descriptionList;

    public SectionItemAdapter(List<Section> descriptionList) {
        this.descriptionList = descriptionList;
    }

    @NonNull
    @Override
    public SectionItemAdapter.SectionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSectionBinding binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SectionItemAdapter.SectionItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionItemAdapter.SectionItemViewHolder holder, int position) {
        Section description = descriptionList.get(position);
        holder.bind(description);
    }

    @Override
    public int getItemCount() {
        return descriptionList.size();
    }

    public static class SectionItemViewHolder extends RecyclerView.ViewHolder {
        final ItemSectionBinding binding;

        public SectionItemViewHolder(@NonNull ItemSectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Section section) {
            TextView title = binding.title;
            if (section.getTitle() != null) {
                title.setText(section.getTitle());
            }

            TextView content = binding.content;
            if (section.getContent() != null) {
                content.setText(section.getContent());
            }

            ImageView image = binding.image;
            if (section.getImage() != null && section.getImage().getImageUrl() != null) {
                Glide.with(binding.getRoot())
                        .load(section.getImage().getImageUrl())
                        .error(R.drawable.brown)
                        .into(image);
                image.setVisibility(ImageView.VISIBLE);
            }
        }
    }
}
