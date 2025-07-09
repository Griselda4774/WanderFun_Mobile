package com.example.wanderfunmobile.presentation.ui.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.wanderfunmobile.databinding.DialogLoadingBinding;

public class LoadingDialog extends Dialog {

    private DialogLoadingBinding binding;

    public LoadingDialog(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DialogLoadingBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());

        setUpDialog();
    }

    private void setUpDialog() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void setLoadingText(String message) {
        binding.loadingText.setVisibility(View.VISIBLE);
        binding.loadingText.setText(message);
    }

    public void hideLoadingText() {
        binding.loadingText.setVisibility(View.GONE);
    }
}
