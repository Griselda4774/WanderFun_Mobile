package com.example.wanderfunmobile.presentation.ui.custom.inputs;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.InputOtpBinding;

public class OtpInput extends LinearLayout {

    private InputOtpBinding binding;
    private EditText[] otpFields;

    public OtpInput(Context context) {
        super(context);
        init(context);
    }

    public OtpInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        binding = InputOtpBinding.inflate(LayoutInflater.from(context), this, true);

        otpFields = new EditText[]{
                binding.otp1,
                binding.otp2,
                binding.otp3,
                binding.otp4,
                binding.otp5,
                binding.otp6
        };

        setupTextWatchers();
    }

    private void setupTextWatchers() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            EditText field = otpFields[index];

            field.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

            field.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    // Khi nhấn xóa
                    if (!otpFields[index].getText().toString().isEmpty()) {
                        otpFields[index].setText("");
                    } else if (index > 0) {
                        otpFields[index - 1].setText("");
                        otpFields[index - 1].requestFocus();
                    }
                    return true;
                }
                return false;
            });

            field.addTextChangedListener(new TextWatcher() {
                private boolean editing;

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (editing) return;
                    editing = true;

                    String value = s.toString();

                    field.setBackgroundResource(
                            value.isEmpty() ? R.drawable.bg_otp_border_gray : R.drawable.bg_otp_border_blue
                    );

                    if (value.length() > 1) {
                        handlePaste(value);
                    } else if (value.length() == 1) {
                        if (index == 0) {
                            otpFields[1].requestFocus();
                        } else if (index < otpFields.length - 1) {
                            otpFields[index + 1].requestFocus();
                        }
                    }

                    editing = false;
                }
            });
        }
    }



    public String getOtp() {
        StringBuilder sb = new StringBuilder();
        for (EditText e : otpFields) {
            sb.append(e.getText().toString());
        }
        return sb.toString();
    }

    public void clearOtp() {
        for (EditText e : otpFields) {
            e.setText("");
            e.setBackgroundResource(R.drawable.bg_otp_border_gray);
        }
        otpFields[0].requestFocus();
    }


    private void handlePaste(String pastedText) {
        char[] chars = pastedText.toCharArray();
        for (int i = 0; i < otpFields.length; i++) {
            if (i < chars.length) {
                otpFields[i].setText(String.valueOf(chars[i]));
            } else {
                otpFields[i].setText("");
            }
        }

        for (int i = 0; i < otpFields.length; i++) {
            if (otpFields[i].getText().toString().isEmpty()) {
                otpFields[i].requestFocus();
                return;
            }
        }

        otpFields[otpFields.length - 1].requestFocus();
    }

    public void setErrorState() {
        for (EditText edit : otpFields) {
            edit.setBackgroundResource(R.drawable.bg_otp_border_red);
        }
    }


}