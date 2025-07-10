package com.example.wanderfunmobile.presentation.ui.custom.inputs;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class DebouncedSearchEditText extends AppCompatEditText {

    private static final int DELAY = 3000;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private DebouncedSearchListener searchListener;

    public DebouncedSearchEditText(Context context) {
        super(context);
        init();
    }

    public DebouncedSearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DebouncedSearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(searchRunnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String query = s.toString().trim();

                if (query.isEmpty() || !hasFocus()) {
                    return;
                }

                searchRunnable = () -> {
                    if (searchListener != null && hasFocus()) {
                        searchListener.onDebouncedSearch(query);
                    }
                };

                handler.postDelayed(searchRunnable, DELAY);
            }
        });
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (!focused) {
            handler.removeCallbacks(searchRunnable);
        }
    }

    public void setDebouncedSearchListener(DebouncedSearchListener listener) {
        this.searchListener = listener;
    }

    public interface DebouncedSearchListener {
        void onDebouncedSearch(String query);
    }
}