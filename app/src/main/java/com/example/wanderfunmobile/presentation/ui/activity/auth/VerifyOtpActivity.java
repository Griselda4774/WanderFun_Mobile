package com.example.wanderfunmobile.presentation.ui.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.dto.auth.LoginDto;
import com.example.wanderfunmobile.data.dto.auth.LoginResponseDto;
import com.example.wanderfunmobile.data.dto.auth.MailOtpDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.databinding.ActivityVerifyOtpBinding;
import com.example.wanderfunmobile.presentation.ui.activity.MainActivity;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.ui.custom.listeners.OtpInputListener;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.FavoritePlaceViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VerifyOtpActivity extends AppCompatActivity {
    private ActivityVerifyOtpBinding viewBinding;
    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;
    private String email;
    private String password;
    private String action;
    @Inject
    ObjectMapper objectMapper;

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

        getIntentData();

        setUpViewModel();

        setUpView();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            action = getIntent().getStringExtra("action");
        }
    }

    private void setUpViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getSendOtpResponseLiveData().observe(this, result -> {
            loadingDialog.hide();
            if (!result.isError()) {
                Toast.makeText(getApplicationContext(), "Đã gửi mã xác thực!", Toast.LENGTH_SHORT).show();
                viewBinding.countdownText.start();
            } else {
                Toast.makeText(this, "Gửi mã xác thực không thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getVerifyOtpResponseLiveData().observe(this, result -> {
            loadingDialog.hide();
            if (!result.isError()) {
                Toast.makeText(getApplicationContext(), "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                if ("register".equals(action) || "login".equals(action)) {
                    login();
                } else if ("reset_password".equals(action)) {
                    // Navigate to reset password activity
                    // Intent intent = new Intent(this, ResetPasswordActivity.class);
                    // startActivity(intent);
                    // finish();
                }
            } else {
                Toast.makeText(this, "Xác thực không thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getLoginResponseLiveData().observe(this, loginResponse -> {
            if (!loginResponse.isError()) {
                LoginResponseDto loginResponseDto = objectMapper.map(loginResponse.getData(), LoginResponseDto.class);
                SessionManager.getInstance(getApplicationContext()).login(
                        loginResponseDto.getId(),
                        loginResponseDto.getUserId(),
                        loginResponseDto.getRole().name(),
                        loginResponseDto.getTokenType(),
                        loginResponseDto.getAccessToken(),
                        loginResponseDto.getRefreshToken());
                PostViewManager.getInstance(getApplicationContext()).reset();
                FavoritePlaceManager.getInstance(getApplicationContext()).clear();
                loadingDialog.hide();
                Toast.makeText(getApplicationContext(), "Chào mừng bạn đến với WanderFun!", Toast.LENGTH_SHORT).show();
                toMainActivity();
            } else {
                loadingDialog.hide();
                Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpView() {
        // Loading dialog
        loadingDialog = new LoadingDialog(this);

        // Timer
        viewBinding.countdownText.setOnStartListener(() -> {
            viewBinding.resendCodeButton.setVisibility(View.GONE);
            viewBinding.countdownTextContainer.setVisibility(View.VISIBLE);
        });
        viewBinding.countdownText.setOnFinishListener(() -> {
            viewBinding.resendCodeButton.setVisibility(View.VISIBLE);
            viewBinding.countdownTextContainer.setVisibility(View.GONE);
        });

        viewBinding.countdownText.setSeconds(20);
        viewBinding.countdownText.start();

        // Otp input
        viewBinding.otpInput.setOtpInputListener(new OtpInputListener() {
            @Override
            public void onComplete(String otp) {
                viewBinding.confirmButton.button.setEnabled(true);
                viewBinding.confirmButton.button.setBackgroundResource(R.drawable.bg_button_highlight);
            }

            @Override
            public void onIncomplete() {
                viewBinding.confirmButton.button.setEnabled(false);
                viewBinding.confirmButton.button.setBackgroundResource(R.drawable.bg_button_disabled);
            }
        });

        // Back button
        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });

        // Confirm button
        viewBinding.confirmButton.button.setText("Xác nhận");
        viewBinding.confirmButton.button.setEnabled(false);
        viewBinding.confirmButton.button.setBackgroundResource(R.drawable.bg_button_disabled);
        viewBinding.confirmButton.button.setOnClickListener(v -> {
            loadingDialog.setLoadingText("Đang xác thực mã...");
            loadingDialog.show();
            MailOtpDto mailOtpDto = new MailOtpDto();
            mailOtpDto.setEmail(email);
            mailOtpDto.setOtp(viewBinding.otpInput.getOtp());
            authViewModel.verifyOtp(mailOtpDto);
        });

        // Resend code button
        viewBinding.resendCodeButton.setOnClickListener(v -> {
            loadingDialog.setLoadingText("Đang gửi mã xác thực...");
            loadingDialog.show();
            authViewModel.sendOtp(email);
        });
    }

    private void login() {
        loadingDialog.setLoadingText("Đang đăng nhập...");
        loadingDialog.show();
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);
        authViewModel.login(loginDto);
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}