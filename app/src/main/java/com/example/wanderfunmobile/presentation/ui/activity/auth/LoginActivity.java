package com.example.wanderfunmobile.presentation.ui.activity.auth;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.FavoritePlaceManager;
import com.example.wanderfunmobile.core.util.PostViewManager;
import com.example.wanderfunmobile.data.dto.auth.LoginDto;
import com.example.wanderfunmobile.data.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.databinding.ActivityLoginBinding;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.ui.activity.MainActivity;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.FavoritePlaceViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding viewBinding;
    private AuthViewModel authViewModel;
    private FavoritePlaceViewModel favoritePlaceViewModel;
    @Inject
    ObjectMapper objectMapper;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Binding
        viewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpViewModel();

        loadingDialog = new LoadingDialog(this);

        TextView registerButton = viewBinding.registerButton;
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        EditText emailEditText = viewBinding.emailInput.input.textEdittext;

        EditText passwordEditText = viewBinding.passwordInput.input.passwordEdittext;
        setupPasswordToggle(passwordEditText, viewBinding.passwordInput.hidePassIcon, viewBinding.passwordInput.viewPassIcon);


        TextView loginButton = viewBinding.loginButton.button;
        loginButton.setText("Đăng nhập");
        loginButton.setOnClickListener(v -> {
            loadingDialog.setLoadingText("Đang đăng nhập...");
            loadingDialog.show();
            String username = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail(username);
            loginDto.setPassword(password);
            authViewModel.login(loginDto);
        });

        TextView guestButton = viewBinding.guestButton;
        guestButton.setOnClickListener(v -> {
            SessionManager.getInstance(getApplicationContext()).logout();
            PostViewManager.getInstance(getApplicationContext()).reset();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Forgot Password
        viewBinding.forgotPassword.setOnClickListener(v-> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setUpViewModel() {
        favoritePlaceViewModel = new ViewModelProvider(this).get(FavoritePlaceViewModel.class);
        favoritePlaceViewModel.getFindAllByUserLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                FavoritePlaceManager.getInstance(getApplicationContext()).init(result.getData());
            }
            toMainActivity();
        });

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getLoginResponseLiveData().observe(this, loginResponse -> {
            loadingDialog.hide();
            if (!loginResponse.isError()) {
                LoginResponseDto loginResponseDto = objectMapper.map(loginResponse.getData(), LoginResponseDto.class);
                if (!loginResponseDto.isVerified()) {
                    Toast.makeText(this, "Vui lòng xác thực tài khoản của bạn!", Toast.LENGTH_SHORT).show();
                    authViewModel.sendOtp(viewBinding.emailInput.input.textEdittext.getText().toString());
                } else {
                    SessionManager.getInstance(getApplicationContext()).login(
                            loginResponseDto.getId(),
                            loginResponseDto.getUserId(),
                            loginResponseDto.getRole().name(),
                            loginResponseDto.getTokenType(),
                            loginResponseDto.getAccessToken(),
                            loginResponseDto.getRefreshToken());
                    PostViewManager.getInstance(getApplicationContext()).reset();
                    FavoritePlaceManager.getInstance(getApplicationContext()).clear();
                    favoritePlaceViewModel.findAllByUser("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());
                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getSendOtpResponseLiveData().observe(this, result -> {
            if (!result.isError()) {
                Toast.makeText(getApplicationContext(), "Đã gửi mã xác thực!", Toast.LENGTH_SHORT).show();
                toVerifyOtpActivity();
            } else {
                Toast.makeText(this, "Gửi mã xác thực không thành công!", Toast.LENGTH_SHORT).show();
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

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void toVerifyOtpActivity() {
        Intent intent = new Intent(this, VerifyOtpActivity.class);
        intent.putExtra("action", "login");
        intent.putExtra("email", viewBinding.emailInput.input.textEdittext.getText().toString());
        intent.putExtra("password", viewBinding.passwordInput.input.passwordEdittext.getText().toString());
        startActivity(intent);
    }
}