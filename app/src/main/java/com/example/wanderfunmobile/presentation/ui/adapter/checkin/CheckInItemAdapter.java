package com.example.wanderfunmobile.presentation.ui.adapter.checkin;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.StringUtil;
import com.example.wanderfunmobile.databinding.ItemCheckInBinding;
import com.example.wanderfunmobile.domain.model.checkins.CheckIn;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnCheckInSelectedListener;

import java.util.List;

public class CheckInItemAdapter extends RecyclerView.Adapter<CheckInItemAdapter.CheckInItemViewHolder> {
    private final List<CheckIn> checkInList;
    private OnCheckInSelectedListener onCheckInSelectedListener;

    public CheckInItemAdapter(List<CheckIn> checkInList) {
        this.checkInList = checkInList;
    }

    @NonNull
    @Override
    public CheckInItemAdapter.CheckInItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCheckInBinding binding = ItemCheckInBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CheckInItemAdapter.CheckInItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckInItemAdapter.CheckInItemViewHolder holder, int position) {
        CheckIn checkIn = checkInList.get(position);
        holder.bind(checkIn);
    }

    @Override
    public int getItemCount() {
        return checkInList.size();
    }

    public void setOnCheckInSelectedListener(OnCheckInSelectedListener listener) {
        this.onCheckInSelectedListener = listener;
    }

    public class CheckInItemViewHolder extends RecyclerView.ViewHolder {
        final ItemCheckInBinding itemCheckInBinding;

        public CheckInItemViewHolder(@NonNull ItemCheckInBinding binding) {
            super(binding.getRoot());
            this.itemCheckInBinding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(CheckIn checkIn) {
            itemCheckInBinding.placeName.setText(checkIn.getPlace().getName());

            itemCheckInBinding.placeRating.setText(String.valueOf(checkIn.getPlace().getRating()));

            if (checkIn.getPlace().getCoverImage() != null) {
                Glide.with(itemCheckInBinding.placeCoverImage.getContext())
                        .load(checkIn.getPlace().getCoverImage().getImageUrl())
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(itemCheckInBinding.placeCoverImage);
            } else {
                itemCheckInBinding.placeCoverImage.setImageResource(R.drawable.img_placeholder);
            }

            if (checkIn.getPlace().getAddress() != null) {
                itemCheckInBinding.address.setText(StringUtil.formatAddressToStringNoStreet(checkIn.getPlace().getAddress()));
            } else {
                itemCheckInBinding.address.setVisibility(View.GONE);
            }

            itemCheckInBinding.getRoot().setOnClickListener(v -> {
                if (onCheckInSelectedListener != null) {
                    onCheckInSelectedListener.onCheckInSelected(checkIn);
                }
            });

            itemCheckInBinding.dateCreated.setText("Đã check-in lúc " + DateTimeUtil.localDateTimeToString(checkIn.getCreatedAt()));
        }
    }
}