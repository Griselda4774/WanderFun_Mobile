package com.example.wanderfunmobile.presentation.ui.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityResetPasswordBinding;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding viewBinding;
    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;
    private String email;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getIntentData();

        setUpViewModel();

        setUpActivity();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
            otp = intent.getStringExtra("otp");
        }
    }

    private void setUpViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getForgotPasswordResponseLiveData().observe(this, result -> {
            loadingDialog.hide();
            if (!result.isError()) {
                Toast.makeText(getApplicationContext(), "Mật khẩu đã được đặt lại!", Toast.LENGTH_SHORT).show();
                toLoginActivity();
            } else {
                Toast.makeText(this, "Đặt lại mật khẩu không thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpActivity() {
        loadingDialog = new LoadingDialog(this);

        // Back button
        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });

        EditText passwordEditText = viewBinding.newPasswordInput.input.passwordEdittext;
        setupPasswordToggle(passwordEditText, viewBinding.newPasswordInput.hidePassIcon, viewBinding.newPasswordInput.viewPassIcon);

        EditText rePasswordEditText = viewBinding.reNewPasswordInput.input.passwordEdittext;
        setupPasswordToggle(rePasswordEditText, viewBinding.reNewPasswordInput.hidePassIcon, viewBinding.reNewPasswordInput.viewPassIcon);
        viewBinding.confirmButton.button.setText("Xong");
        viewBinding.confirmButton.button.setOnClickListener(v -> {
            String password = passwordEditText.getText().toString();
            if (password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 8 || !password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") || !password.matches(".*\\d.*")) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số", Toast.LENGTH_SHORT).show();
                return;
            }

            String rePassword = rePasswordEditText.getText().toString();
            if (rePassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(rePassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            loadingDialog.setLoadingText("Đang đặt lại mật khẩu...");
            loadingDialog.show();
            authViewModel.forgotPassword(email, otp, password);
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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