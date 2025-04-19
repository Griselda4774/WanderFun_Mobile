package com.example.wanderfunmobile.presentation.ui.activity;

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
import com.example.wanderfunmobile.databinding.ActivityRegisterBinding;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;
import com.example.wanderfunmobile.data.dto.auth.RegisterDto;

import org.modelmapper.ModelMapper;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private ModelMapper modelMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Binding
        ActivityRegisterBinding viewBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        TextView loginButton = viewBinding.loginButton;
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        ConstraintLayout firstnameInput = viewBinding.firstnameInput;
        EditText firstnameEditText = firstnameInput.findViewById(R.id.text_edittext);

        ConstraintLayout lastnameInput = viewBinding.lastnameInput;
        EditText lastnameEditText = lastnameInput.findViewById(R.id.text_edittext);

        ConstraintLayout emailInput = viewBinding.emailInput;
        EditText emailEditText = emailInput.findViewById(R.id.text_edittext);

        ConstraintLayout passwordInput = viewBinding.passwordInput;
        EditText passwordEditText = passwordInput.findViewById(R.id.password_edittext);
        ImageView hidePassIcon = passwordInput.findViewById(R.id.hide_pass_icon);
        ImageView viewPassIcon = passwordInput.findViewById(R.id.view_pass_icon);
        setupPasswordToggle(passwordEditText, hidePassIcon, viewPassIcon);

        ConstraintLayout rePasswordInput = viewBinding.repasswordInput;
        EditText rePasswordEditText = rePasswordInput.findViewById(R.id.password_edittext);
        ImageView hideRePassIcon = rePasswordInput.findViewById(R.id.hide_pass_icon);
        ImageView viewRePassIcon = rePasswordInput.findViewById(R.id.view_pass_icon);
        setupPasswordToggle(rePasswordEditText, hideRePassIcon, viewRePassIcon);

        TextView registerButton = viewBinding.registerButton.findViewById(R.id.button);
        registerButton.setText("Đăng ký");
        registerButton.setOnClickListener(v -> {
            String firstname = firstnameEditText.getText().toString();
            String lastname = lastnameEditText.getText().toString();
            String username = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            RegisterDto registerDto = new RegisterDto();
            registerDto.setFirstName(firstname);
            registerDto.setLastName(lastname);
            registerDto.setEmail(username);
            registerDto.setPassword(password);
            authViewModel.register(registerDto);
        });

        authViewModel.getRegisterResponseLiveData().observe(this, registerResponse -> {
            if (!registerResponse.isError()) {
                Toast.makeText(this, "Success: " + registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error: " + registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
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