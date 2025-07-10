package com.example.wanderfunmobile.presentation.ui.adapter.searchs;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.databinding.ItemSearchProvinceBinding;
import com.example.wanderfunmobile.domain.model.addresses.Province;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OnSearchProvinceItemClickListener;

import java.util.List;

public class SearchProvinceItemAdapter extends RecyclerView.Adapter<SearchProvinceItemAdapter.SearchProvinceItemViewHolder> {
    private final List<Province> provinceList;
    private OnSearchProvinceItemClickListener onSearchProvinceItemClickListener;

    public SearchProvinceItemAdapter(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    @NonNull
    @Override
    public SearchProvinceItemAdapter.SearchProvinceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchProvinceBinding binding = ItemSearchProvinceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchProvinceItemAdapter.SearchProvinceItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProvinceItemAdapter.SearchProvinceItemViewHolder holder, int position) {
        Province province = provinceList.get(position);
        holder.bind(province);
    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }

    public void setOnSearchProvinceItemClickListener(OnSearchProvinceItemClickListener listener) {
        this.onSearchProvinceItemClickListener = listener;
    }

    public class SearchProvinceItemViewHolder extends RecyclerView.ViewHolder {
        final ItemSearchProvinceBinding binding;

        public SearchProvinceItemViewHolder(@NonNull ItemSearchProvinceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Province province) {
            binding.itemSearchProvince.setText(province.getFullName());

            binding.itemSearchProvince.setOnClickListener(v -> {
                if (onSearchProvinceItemClickListener != null) {
                    onSearchProvinceItemClickListener.onSearchProvinceItemClick(province);
                }
            });
        }
    }
}