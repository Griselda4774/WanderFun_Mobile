package com.example.wanderfunmobile.presentation.ui.custom.textviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.wanderfunmobile.databinding.TextViewCountdownBinding;

public class CountdownTextView extends LinearLayout {

    private TextViewCountdownBinding binding;
    private CountDownTimer timer;
    private long totalMillis;
    private long remainingMillis;
    private boolean isRunning = false;

    private OnStartListener onStartListener;
    private OnFinishListener onFinishListener;

    public interface OnStartListener {
        void onStart();
    }

    public interface OnFinishListener {
        void onFinish();
    }

    public CountdownTextView(Context context) {
        super(context);
        init(context);
    }

    public CountdownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        binding = TextViewCountdownBinding.inflate(LayoutInflater.from(context), this, true);
        updateText(0);
    }

    public void setSeconds(int seconds) {
        totalMillis = seconds * 1000L;
        remainingMillis = totalMillis;
        updateText(remainingMillis);
    }

    public void setOnStartListener(OnStartListener listener) {
        this.onStartListener = listener;
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.onFinishListener = listener;
    }

    public void start() {
        if (isRunning || remainingMillis <= 0) return;

        if (onStartListener != null) onStartListener.onStart();

        timer = new CountDownTimer(remainingMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                remainingMillis = millisUntilFinished;
                updateText(millisUntilFinished);
            }

            public void onFinish() {
                isRunning = false;
                if (onFinishListener != null) onFinishListener.onFinish();
            }
        };

        isRunning = true;
        timer.start();
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isRunning = false;
    }

    public void restart() {
        stop();
        remainingMillis = totalMillis;
        start();
    }

    @SuppressLint("DefaultLocale")
    private void updateText(long millis) {
        int seconds = Math.max(1, (int) (millis / 1000));
        binding.countdownText.setText(String.format("%02d", seconds));
    }
}
