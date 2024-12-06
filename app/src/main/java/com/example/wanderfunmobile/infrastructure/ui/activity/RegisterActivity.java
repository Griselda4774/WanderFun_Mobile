package com.example.wanderfunmobile.infrastructure.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    // Binding
    private ActivityRegisterBinding viewBinding;

    private TextView registerButton;
    private TextView loginButton;
    private ConstraintLayout passwordInput;
    private ConstraintLayout rePasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton = viewBinding.loginButton;
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        passwordInput = viewBinding.passwordInput;
        EditText passwordEditText = passwordInput.findViewById(R.id.password_edittext);
        ImageView hidePassIcon = passwordInput.findViewById(R.id.hide_pass_icon);
        ImageView viewPassIcon = passwordInput.findViewById(R.id.view_pass_icon);
        setupPasswordToggle(passwordEditText, hidePassIcon, viewPassIcon);

        rePasswordInput = viewBinding.repasswordInput;
        EditText rePasswordEditText = rePasswordInput.findViewById(R.id.password_edittext);
        ImageView hideRePassIcon = rePasswordInput.findViewById(R.id.hide_pass_icon);
        ImageView viewRePassIcon = rePasswordInput.findViewById(R.id.view_pass_icon);
        setupPasswordToggle(rePasswordEditText, hideRePassIcon, viewRePassIcon);

    }

    private void setupPasswordToggle(EditText passwordEditText, ImageView showIcon, ImageView hideIcon) {
        showIcon.setOnClickListener(v -> {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setSelection(passwordEditText.getText().length());
            passwordEditText.setTextAppearance(R.style.Text_Content);
            showIcon.setVisibility(View.INVISIBLE);
            showIcon.setClickable(false);
            hideIcon.setVisibility(View.VISIBLE);
            hideIcon.setClickable(true);
        });

        hideIcon.setOnClickListener(v -> {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setSelection(passwordEditText.getText().length());
            passwordEditText.setTextAppearance(R.style.Text_Content);
            showIcon.setVisibility(View.VISIBLE);
            showIcon.setClickable(true);
            hideIcon.setVisibility(View.INVISIBLE);
            hideIcon.setClickable(false);
        });
    }
}