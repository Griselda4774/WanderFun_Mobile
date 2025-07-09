package com.example.wanderfunmobile.presentation.ui.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
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
import com.example.wanderfunmobile.databinding.ActivityRegisterBinding;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.viewmodel.AuthViewModel;
import com.example.wanderfunmobile.data.dto.auth.RegisterDto;

import org.modelmapper.ModelMapper;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private ModelMapper modelMapper;
    private ActivityRegisterBinding viewBinding;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Binding
        viewBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpViewModel();

        loadingDialog = new LoadingDialog(this);

        TextView loginButton = viewBinding.loginButton;
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        EditText firstnameEditText = viewBinding.firstnameInput.input.textEdittext;

        EditText lastnameEditText = viewBinding.lastnameInput.input.textEdittext;

        EditText emailEditText = viewBinding.emailInput.input.textEdittext;

        EditText passwordEditText = viewBinding.passwordInput.input.passwordEdittext;
        setupPasswordToggle(passwordEditText, viewBinding.passwordInput.hidePassIcon, viewBinding.passwordInput.viewPassIcon);

        EditText rePasswordEditText = viewBinding.repasswordInput.input.passwordEdittext;
        setupPasswordToggle(rePasswordEditText, viewBinding.repasswordInput.hidePassIcon, viewBinding.repasswordInput.viewPassIcon);

        TextView registerButton = viewBinding.registerButton.button;
        registerButton.setText("Đăng ký");
        registerButton.setOnClickListener(v -> {
            String firstname = firstnameEditText.getText().toString().trim();
            if (firstname.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                return;
            }

            if (firstname.length() < 2) {
                Toast.makeText(this, "Tên phải có ít nhất 2 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            if (firstname.length() > 50) {
                Toast.makeText(this, "Tên không được vượt quá 50 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!firstname.matches("\\p{L}[\\p{L}\\s]*")) {
                Toast.makeText(this, "Tên chỉ được chứa chữ cái và khoảng trắng", Toast.LENGTH_SHORT).show();
                return;
            }
            String lastname = lastnameEditText.getText().toString().trim();
            if (lastname.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập họ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (lastname.length() < 2) {
                Toast.makeText(this, "Họ phải có ít nhất 2 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            if (lastname.length() > 50) {
                Toast.makeText(this, "Họ không được vượt quá 50 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!lastname.matches("\\p{L}[\\p{L}\\s]*")) {
                Toast.makeText(this, "Họ chỉ được chứa chữ cái và khoảng trắng", Toast.LENGTH_SHORT).show();
                return;
            }

            String email = emailEditText.getText().toString().trim();
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

            loadingDialog.setLoadingText("Đang đăng ký...");
            loadingDialog.show();
            RegisterDto registerDto = new RegisterDto();
            registerDto.setFirstName(firstname);
            registerDto.setLastName(lastname);
            registerDto.setEmail(email);
            registerDto.setPassword(password);
            authViewModel.register(registerDto);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void setUpViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getRegisterResponseLiveData().observe(this, registerResponse -> {
            loadingDialog.hide();
            if (!registerResponse.isError()) {
                Toast.makeText(this, "Vui lòng xác thực tài khoản của bạn!", Toast.LENGTH_SHORT).show();
                authViewModel.sendOtp(viewBinding.emailInput.input.textEdittext.getText().toString());
            } else {
                Toast.makeText(this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
            }
        });

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

    private void toVerifyOtpActivity() {
        Intent intent = new Intent(this, VerifyOtpActivity.class);
        intent.putExtra("action", "register");
        intent.putExtra("email", viewBinding.emailInput.input.textEdittext.getText().toString());
        intent.putExtra("password", viewBinding.passwordInput.input.passwordEdittext.getText().toString());
        startActivity(intent);
    }
}