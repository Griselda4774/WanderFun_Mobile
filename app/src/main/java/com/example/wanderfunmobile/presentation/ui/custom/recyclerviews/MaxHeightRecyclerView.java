package com.example.wanderfunmobile.presentation.ui.custom.recyclerviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfunmobile.R;

public class MaxHeightRecyclerView extends RecyclerView {
    private int maxHeight = Integer.MAX_VALUE;

    public MaxHeightRecyclerView(Context context) {
        super(context);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView)) {
            maxHeight = a.getDimensionPixelSize(R.styleable.MaxHeightRecyclerView_maxHeight, Integer.MAX_VALUE);
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}