package com.example.wanderfunmobile.infrastructure.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityMainBinding;
import com.example.wanderfunmobile.infrastructure.ui.fragment.HomeFragment;
import com.example.wanderfunmobile.infrastructure.ui.fragment.LeaderboardFragment;
import com.example.wanderfunmobile.infrastructure.ui.fragment.NotSignedInFragment;
import com.example.wanderfunmobile.infrastructure.ui.fragment.ProfileFragment;
import com.example.wanderfunmobile.infrastructure.ui.fragment.TripFragment;
import com.example.wanderfunmobile.infrastructure.util.CloudinaryUtil;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = viewBinding.bottomNavigation;
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home_nav) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.trip_nav) {
                if (SessionManager.getInstance(this).isLoggedIn()) {
                    selectedFragment = new TripFragment();
                } else {
                    selectedFragment = new NotSignedInFragment();
                }
            } else if (item.getItemId() == R.id.leaderboard_nav) {
                selectedFragment = new LeaderboardFragment();
            } else if (item.getItemId() == R.id.profile_nav) {
                if (SessionManager.getInstance(this).isLoggedIn()) {
                    selectedFragment = new ProfileFragment();
                } else {
                    selectedFragment = new NotSignedInFragment();
                }
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }

            return true;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(viewBinding.fragmentContainer.getId(), fragment)
                .commit();
    }
}