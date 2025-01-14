package com.example.wanderfunmobile.infrastructure.util;

import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.lang.ref.WeakReference;

public class ViewPager2HeightAdjuster {

    @BindingAdapter("autoAdjustHeight")
    public static void autoAdjustHeight(ViewPager2 viewPager2, boolean autoAdjustHeight) {
        WeakReference<ViewPager2> weakRef = new WeakReference<>(viewPager2);

        setInitialHeight(weakRef);

        viewPager2.setOffscreenPageLimit(1);

        ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ViewPager2 vp2 = weakRef.get();
                if (vp2 != null) {
                    updateHeight(vp2, position);
                }
            }
        };

        RecyclerView.AdapterDataObserver adapterObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setInitialHeight(weakRef);
            }
        };

        if (autoAdjustHeight) {
            viewPager2.registerOnPageChangeCallback(pageChangeCallback);
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            if (recyclerView != null && recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().registerAdapterDataObserver(adapterObserver);
            }
        } else {
            viewPager2.unregisterOnPageChangeCallback(pageChangeCallback);
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            if (recyclerView != null && recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().unregisterAdapterDataObserver(adapterObserver);
            }
        }
    }

    private static void setInitialHeight(WeakReference<ViewPager2> viewPager2) {
        ViewPager2 vp2 = viewPager2.get();
        if (vp2 != null) {
            updateHeight(vp2, 0);
        }
    }

    private static void updateHeight(ViewPager2 viewPager2, int position) {
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        if (recyclerView == null) return;

        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) return;

        View view = viewHolder.itemView;
        view.post(() -> {
            int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(widthSpec, heightSpec);
            int height = view.getMeasuredHeight();

            if (viewPager2.getLayoutParams().height != height) {
                viewPager2.getLayoutParams().height = height;
                viewPager2.requestLayout();
            }
        });
    }
}


