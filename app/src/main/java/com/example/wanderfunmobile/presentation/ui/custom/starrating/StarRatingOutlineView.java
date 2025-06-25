package com.example.wanderfunmobile.presentation.ui.custom.starrating;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.presentation.ui.activity.place.FeedbackCreateActivity;

public class StarRatingOutlineView extends ConstraintLayout {
    private final ImageView[] stars = new ImageView[5];
    private int currentRating = 0;
    private Long placeId;
    private String placeName;

    public StarRatingOutlineView(Context context) {
        super(context);
        init(context);
    }

    public StarRatingOutlineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StarRatingOutlineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_star_rating_outline, this, true);

        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);

        setRating(0);
        enableIntent();
    }

    public int getRating() {
        return currentRating;
    }

    public void setRating(int rating) {
        rating = Math.max(0, Math.min(rating, 5));
        currentRating = rating;

        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star));
            } else {
                stars[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_outline));
            }
        }
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void enableIntent() {
        for (int i = 0; i < stars.length; i++) {
            final int finalI = i;
            stars[i].setOnClickListener(v -> {
                if (onStarClickListener != null) {
                    onStarClickListener.onStarClicked(finalI + 1, placeId, placeName);
                }
            });
        }
    }

    public void disableIntent() {
        for (int i = 0; i < stars.length; i++) {
            final int finalI = i;
            stars[i].setOnClickListener(v -> {
                setRating(finalI + 1);
            });
        }
    }

    public interface OnStarClickListener {
        void onStarClicked(int rating, Long placeId, String placeName);
    }

    private OnStarClickListener onStarClickListener;

    public void setOnStarClickListener(OnStarClickListener listener) {
        this.onStarClickListener = listener;
    }
}
