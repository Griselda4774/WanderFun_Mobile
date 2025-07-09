package com.example.wanderfunmobile.presentation.ui.activity.auth;

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
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.ActivityChangePasswordBinding;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding viewBinding;
    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpViewModel();

        setUpView();
    }

    private void setUpViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getChangePasswordResponseLiveData().observe(this, result -> {
            loadingDialog.hide();
            if (!result.isError()) {
                Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpView() {
        loadingDialog = new LoadingDialog(this);

        // Back button
        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });

        EditText passwordEditText = viewBinding.passwordInput.input.passwordEdittext;
        setupPasswordToggle(passwordEditText, viewBinding.passwordInput.hidePassIcon, viewBinding.passwordInput.viewPassIcon);

        EditText newPasswordEditText = viewBinding.newPasswordInput.input.passwordEdittext;
        setupPasswordToggle(newPasswordEditText, viewBinding.newPasswordInput.hidePassIcon, viewBinding.newPasswordInput.viewPassIcon);

        EditText reNewPasswordEditText = viewBinding.reNewPasswordInput.input.passwordEdittext;
        setupPasswordToggle(reNewPasswordEditText, viewBinding.reNewPasswordInput.hidePassIcon, viewBinding.reNewPasswordInput.viewPassIcon);

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

            String newPassword = newPasswordEditText.getText().toString();
            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 8 || !password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") || !password.matches(".*\\d.*")) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.equals(password)) {
                Toast.makeText(this, "Mật khẩu mới không được trùng với mật khẩu cũ", Toast.LENGTH_SHORT).show();
                return;
            }

            String reNewPassword = reNewPasswordEditText.getText().toString();
            if (reNewPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập lại mật khẩu mới", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(reNewPassword)) {
                Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            loadingDialog.setLoadingText("Đang đặt lại mật khẩu...");
            loadingDialog.show();
            authViewModel.changePassword("Bearer " + SessionManager.getInstance(this).getAccessToken(), password, newPassword);
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