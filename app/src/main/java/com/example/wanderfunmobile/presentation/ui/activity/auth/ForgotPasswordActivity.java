package com.example.wanderfunmobile.presentation.ui.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.databinding.ActivityForgotPasswordBinding;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding viewBinding;
    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpViewModel();
        setUpActivity();
    }

    private void setUpViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getSendOtpResponseLiveData().observe(this, result -> {
            loadingDialog.hide();
            if (!result.isError()) {
                Toast.makeText(getApplicationContext(), "Đã gửi mã xác thực!", Toast.LENGTH_SHORT).show();
                toVerifyOtpActivity();
            } else {
                Toast.makeText(this, "Gửi mã xác thực không thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpActivity() {
        loadingDialog = new LoadingDialog(this);

        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });

        viewBinding.confirmButton.button.setText("Xác nhận");
        viewBinding.confirmButton.button.setOnClickListener(v-> {
            String email = viewBinding.emailInput.input.textEdittext.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.length() > 100) {
                Toast.makeText(this, "Email không được vượt quá 100 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            loadingDialog.setLoadingText("Đang gửi mã xác nhận...");
            loadingDialog.show();
            authViewModel.sendOtp(email);
        });
    }

    private void toVerifyOtpActivity() {
        Intent intent = new Intent(this, VerifyOtpActivity.class);
        intent.putExtra("action", "forgot_password");
        intent.putExtra("email", viewBinding.emailInput.input.textEdittext.getText().toString());
        startActivity(intent);
    }
}