package com.example.wanderfunmobile.infrastructure.ui.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityMyProfileBinding;

public class MyProfileActivity extends AppCompatActivity {

    ActivityMyProfileBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        TextView editProfileButton = viewBinding.editProfileButton.findViewById(R.id.button);
        editProfileButton.setText("Chỉnh sửa thông tin");
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

    }
}