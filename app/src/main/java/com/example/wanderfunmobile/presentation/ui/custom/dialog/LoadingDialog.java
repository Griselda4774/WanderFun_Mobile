package com.example.wanderfunmobile.presentation.ui.custom.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.wanderfunmobile.databinding.DialogLoadingBinding;

public class LoadingDialog extends LinearLayout {

    private DialogLoadingBinding binding;

    public LoadingDialog(Context context) {
        super(context);
        init(context);
    }

    public LoadingDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingDialog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        binding = DialogLoadingBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void show() {
        binding.getRoot().setVisibility(VISIBLE);
    }

    public void hide() {
        binding.getRoot().setVisibility(GONE);
    }
}
