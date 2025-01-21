package com.example.wanderfunmobile.infrastructure.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.FragmentTripBinding;
import com.example.wanderfunmobile.infrastructure.ui.adapter.trip.TripTabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class TripFragment extends Fragment {

    private FragmentTripBinding viewBinding;

    public TripFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentTripBinding.inflate(inflater, container, false);

        TripTabAdapter tripTabAdapter = new TripTabAdapter(this);
        viewBinding.viewPager.setAdapter(tripTabAdapter);

        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager, (tab, position) -> {
            @SuppressLint("InflateParams") View customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item, null);
            TextView tabText = customView.findViewById(R.id.tab_text);
            tabText.setTextAppearance(R.style.Text_TabLabel);

            switch (position) {
                case 0:
                    tabText.setText("Tất cả");
                    tabText.setTextAppearance(R.style.Text_TabLabel_Active_Blue);
                    break;
                case 1:
                    tabText.setText("Đã qua");
//                    tabText.setTextAppearance(R.style.Text_TabLabel_Active_Orange);
                    break;
                case 2:
                    tabText.setText("Đã xóa");
//                    tabText.setTextAppearance(R.style.Text_TabLabel_Active_Red);
                    break;
            }

            tab.setCustomView(customView);
        }).attach();

        viewBinding.tabLayout.selectTab(viewBinding.tabLayout.getTabAt(0));
        viewBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tabText = Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tab_text);
                tabText.setTextAppearance(R.style.Text_TabLabel);
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tabText = Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tab_text);
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        viewBinding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.blue2));
                        tabText.setTextAppearance(R.style.Text_TabLabel_Active_Blue);
                        break;
                    case 1:
                        viewBinding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.orange5));
                        tabText.setTextAppearance(R.style.Text_TabLabel_Active_Orange);
                        break;
                    case 2:
                        viewBinding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.red4));
                        tabText.setTextAppearance(R.style.Text_TabLabel_Active_Red);
                        break;
                    default:
                        viewBinding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.blue2));
                        tabText.setTextAppearance(R.style.Text_TabLabel_Active_Blue);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}