package com.example.wanderfunmobile.infrastructure.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.infrastructure.util.CloudinaryUtil;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloudinaryUtil.init(getApplicationContext());

        if (SessionManager.getInstance(getApplicationContext()).isLoggedIn()) {
            authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
            authViewModel.refreshToken("Bearer " + SessionManager.getInstance(getApplicationContext()).getRefreshToken());
            authViewModel.getRefreshTokenResponseLiveData().observe(this, response -> {
                if (response != null && !response.isError()) {
                    SessionManager.getInstance(getApplicationContext()).refresh(response.getData().getAccessToken(),
                            response.getData().getRefreshToken());
                    toMainActivity();
                } else {
                    SessionManager.getInstance(getApplicationContext()).logout();
                    Toast.makeText(getApplicationContext(), "Phiên đăng nhập hết hạn!\nVui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    toLoginActivity();
                }
            });
        } else {
            toLoginActivity();
        }
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