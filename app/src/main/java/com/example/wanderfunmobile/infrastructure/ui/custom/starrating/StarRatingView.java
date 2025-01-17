package com.example.wanderfunmobile.infrastructure.ui.custom.starrating;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.wanderfunmobile.R;

public class StarRatingView extends LinearLayout {
    private final ImageView[] stars = new ImageView[5];

    public StarRatingView(Context context) {
        super(context);
        init(context);
    }

    public StarRatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StarRatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_star_rating, this, true);

        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);

        setRating(0);
        setIsClickable(true);
    }

    public void setRating(int rating) {
        rating = Math.max(0, Math.min(rating, 5));

        for (int i = 0; i < rating; i++) {
            stars[i].setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.star)));
        }
    }

    public void setIsClickable(boolean isClickable) {
        for (ImageView star : stars) {
            star.setClickable(isClickable);
        }
    }
}
