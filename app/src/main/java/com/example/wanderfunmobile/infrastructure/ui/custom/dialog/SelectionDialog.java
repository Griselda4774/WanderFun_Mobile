package com.example.wanderfunmobile.infrastructure.ui.custom.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.DialogSelectionBinding;

public class SelectionDialog extends LinearLayout {

    private DialogSelectionBinding binding;

    private OnAcceptListener onAcceptListener;
    private OnRejectListener onRejectListener;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoHideRunnable;

    private TextView acceptButton;
    private TextView rejectButton;

    public SelectionDialog(Context context) {
        super(context);
        init(context);
    }

    public SelectionDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectionDialog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        binding = DialogSelectionBinding.inflate(LayoutInflater.from(context), this, true);

        hide();

        acceptButton = binding.acceptButton.findViewById(R.id.button);
        acceptButton.setOnClickListener(v -> {
            if (onAcceptListener != null) {
                onAcceptListener.onAccept();
            }
            cancelAutoHide();
        });

        rejectButton = binding.rejectButton.findViewById(R.id.button);
        rejectButton.setOnClickListener(v -> {
            if (onRejectListener != null) {
                onRejectListener.onReject();
            }
            cancelAutoHide();
        });
    }

    public void setInfo(String title, String content1, String content2, String acceptText, String rejectText) {
        binding.title.setText(title);
        binding.content1.setText(content1);
        binding.content2.setText(content2);
        acceptButton.setText(acceptText);
        rejectButton.setText(rejectText);
    }

    public void show(String title, String content1, String content2, String acceptText, String rejectText) {
        setInfo(title, content1, content2, acceptText, rejectText);
        binding.getRoot().setVisibility(VISIBLE);

        autoHideRunnable = this::hide;
        handler.postDelayed(autoHideRunnable, 15000);
    }

    public void hide() {
        binding.getRoot().setVisibility(GONE);
        cancelAutoHide();
    }

    public void setOnAcceptListener(OnAcceptListener listener) {
        this.onAcceptListener = listener;
    }

    public void setOnRejectListener(OnRejectListener listener) {
        this.onRejectListener = listener;
    }

    public interface OnAcceptListener {
        void onAccept();
    }

    public interface OnRejectListener {
        void onReject();
    }

    private void cancelAutoHide() {
        if (autoHideRunnable != null) {
            handler.removeCallbacks(autoHideRunnable);
            autoHideRunnable = null;
        }
    }
}