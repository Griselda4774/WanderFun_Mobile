package com.example.wanderfunmobile.presentation.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.android.MediaManager;
import com.example.wanderfunmobile.core.util.CloudinaryUtil;
import com.example.wanderfunmobile.core.util.FavoritePlaceManager;
import com.example.wanderfunmobile.core.util.MediaManagerStateUtil;
import com.example.wanderfunmobile.core.util.PostViewManager;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.FavoritePlaceViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private FavoritePlaceViewModel favoritePlaceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!MediaManagerStateUtil.isInitialized()) {
            CloudinaryUtil.init(getApplicationContext());
        }

        setUpViewModel();

        if (SessionManager.getInstance(getApplicationContext()).isLoggedIn()) {
            authViewModel.refreshToken("Bearer " + SessionManager.getInstance(getApplicationContext()).getRefreshToken());
        } else {
            toLoginActivity();
        }
    }

    private void setUpViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getRefreshTokenResponseLiveData().observe(this, response -> {
            if (response != null && !response.isError()) {
                SessionManager.getInstance(getApplicationContext()).refresh(response.getData().getAccessToken(),
                        response.getData().getRefreshToken());
                FavoritePlaceManager.getInstance(getApplicationContext()).clear();
                favoritePlaceViewModel.findAllByUser("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());
            } else {
                SessionManager.getInstance(getApplicationContext()).logout();
                PostViewManager.getInstance(getApplicationContext()).reset();
                Toast.makeText(getApplicationContext(), "Phiên đăng nhập hết hạn!\nVui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                toLoginActivity();
            }
        });

        favoritePlaceViewModel = new ViewModelProvider(this).get(FavoritePlaceViewModel.class);
        favoritePlaceViewModel.getFindAllByUserLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                FavoritePlaceManager.getInstance(getApplicationContext()).init(result.getData());
            }
            toMainActivity();
        });
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}