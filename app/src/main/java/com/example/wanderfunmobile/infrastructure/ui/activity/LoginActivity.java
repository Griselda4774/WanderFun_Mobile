package com.example.wanderfunmobile.infrastructure.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.application.dto.auth.LoginDto;
import com.example.wanderfunmobile.application.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.databinding.ActivityLoginBinding;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    @Inject
    ObjectMapper objectMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Binding
        ActivityLoginBinding viewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);


        TextView registerButton = viewBinding.registerButton;
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        ConstraintLayout emailInput = viewBinding.emailInput;
        EditText emailEditText = emailInput.findViewById(R.id.text_edittext);

        ConstraintLayout passwordInput = viewBinding.passwordInput;
        EditText passwordEditText = passwordInput.findViewById(R.id.password_edittext);
        ImageView hidePassIcon = passwordInput.findViewById(R.id.hide_pass_icon);
        ImageView viewPassIcon = passwordInput.findViewById(R.id.view_pass_icon);
        setupPasswordToggle(passwordEditText, hidePassIcon, viewPassIcon);


        TextView loginButton = viewBinding.loginButton.findViewById(R.id.button);
        loginButton.setText("Đăng nhập");
        loginButton.setOnClickListener(v -> {
            String username = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail(username);
            loginDto.setPassword(password);
            authViewModel.login(loginDto);
        });

        authViewModel.getLoginResponseLiveData().observe(this, loginResponse -> {
            if (!loginResponse.isError()) {
                LoginResponseDto loginResponseDto = objectMapper.map(loginResponse.getData(), LoginResponseDto.class);
                SessionManager.getInstance(getApplicationContext()).login(
                        loginResponseDto.getId(),
                        loginResponseDto.getEmail(),
                        loginResponseDto.getRole().name(),
                        loginResponseDto.getTokenType(),
                        loginResponseDto.getAccessToken(),
                        loginResponseDto.getRefreshToken());
                Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Lỗi: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        TextView guestButton = viewBinding.guestButton;
        guestButton.setOnClickListener(v -> {
            SessionManager.getInstance(getApplicationContext()).logout();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
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