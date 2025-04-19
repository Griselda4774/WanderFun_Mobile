package com.example.wanderfunmobile.presentation.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityMainBinding;
import com.example.wanderfunmobile.presentation.ui.fragment.HomeFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.LeaderboardFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.NotSignedInFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.ProfileFragment;
import com.example.wanderfunmobile.presentation.ui.fragment.TripFragment;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import dagger.hilt.android.AndroidEntryPoint;

//@AndroidEntryPoint
//public class MainActivity extends AppCompatActivity {
//
//    ActivityMainBinding viewBinding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(viewBinding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        BottomNavigationView bottomNavigationView = viewBinding.bottomNavigation;
//        if (savedInstanceState == null) {
//            loadFragment(new HomeFragment());
//        }
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//
//            if (item.getItemId() == R.id.home_nav) {
//                selectedFragment = new HomeFragment();
//            } else if (item.getItemId() == R.id.trip_nav) {
//                if (SessionManager.getInstance(getApplicationContext()).isLoggedIn()) {
//                    selectedFragment = new TripFragment();
//                } else {
//                    selectedFragment = new NotSignedInFragment();
//                }
//            } else if (item.getItemId() == R.id.leaderboard_nav) {
//                selectedFragment = new LeaderboardFragment();
//            } else if (item.getItemId() == R.id.profile_nav) {
//                if (SessionManager.getInstance(getApplicationContext()).isLoggedIn()) {
//                    selectedFragment = new ProfileFragment();
//                } else {
//                    selectedFragment = new NotSignedInFragment();
//                }
//            }
//
//            if (selectedFragment != null) {
//                loadFragment(selectedFragment);
//            }
//
//            return true;
//        });
//
//    }
//
//    private void loadFragment(Fragment fragment) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(viewBinding.fragmentContainer.getId(), fragment)
//                .commit();
//    }
//}

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding viewBinding;
    private final Map<String, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = viewBinding.bottomNavigation;

        if (savedInstanceState == null) {
            loadFragment("home", HomeFragment::new);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home_nav) {
                loadFragment("home", HomeFragment::new);
            } else if (id == R.id.trip_nav) {
                if (SessionManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    loadFragment("trip", TripFragment::new);
                } else {
                    loadFragment("not_signed_in", NotSignedInFragment::new);
                }
            } else if (id == R.id.leaderboard_nav) {
                loadFragment("leaderboard", LeaderboardFragment::new);
            } else if (id == R.id.profile_nav) {
                if (SessionManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    loadFragment("profile", ProfileFragment::new);
                } else {
                    loadFragment("not_signed_in", NotSignedInFragment::new);
                }
            }
            return true;
        });
    }

    private void loadFragment(String tag, Supplier<Fragment> fragmentSupplier) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(viewBinding.fragmentContainer.getId());
        Fragment targetFragment = fragmentMap.get(tag);

        if (targetFragment == null) {
            targetFragment = fragmentSupplier.get();
            fragmentMap.put(tag, targetFragment);
        }

        if (currentFragment != null && currentFragment.getClass().equals(targetFragment.getClass())) {
            return; // avoid unnecessary replace
        }

        fm.beginTransaction()
                .replace(viewBinding.fragmentContainer.getId(), targetFragment, tag)
                .commit();
    }
}