package com.example.wanderfunmobile.presentation.ui.activity.auth;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityVerifyOtpBinding;

public class VerifyOtpActivity extends AppCompatActivity {
    private ActivityVerifyOtpBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}