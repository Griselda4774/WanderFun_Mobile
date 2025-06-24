package com.example.wanderfunmobile.presentation.ui.activity.leaderboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityLeaderboardBinding;
import com.example.wanderfunmobile.presentation.ui.adapter.leaderboard.LeaderboardTabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LeaderboardActivity extends AppCompatActivity {

    private ActivityLeaderboardBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LeaderboardTabAdapter leaderboardTabAdapter = new LeaderboardTabAdapter(this);
        viewBinding.viewPager.setAdapter(leaderboardTabAdapter);

        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager, (tab, position) -> {
            @SuppressLint("InflateParams") View customView = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
            TextView tabText = customView.findViewById(R.id.tab_text);
            tabText.setTextAppearance(R.style.Text_TabLabel);

            if (position == 1) {
                tabText.setText("Địa điểm");
            } else {
                tabText.setText("Người dùng");
                tabText.setTextAppearance(R.style.Text_TabLabel_Active_Blue);
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
                if (position == 1) {
                    viewBinding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(LeaderboardActivity.this, R.color.green4));
                    tabText.setTextAppearance(R.style.Text_TabLabel_Active_Green);
                } else {
                    viewBinding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(LeaderboardActivity.this, R.color.blue2));
                    tabText.setTextAppearance(R.style.Text_TabLabel_Active_Blue);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // no-op
            }
        });

        ConstraintLayout backButton = viewBinding.backButton.button;
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewBinding = null;
    }
}
