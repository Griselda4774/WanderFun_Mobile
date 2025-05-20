package com.example.wanderfunmobile.presentation.ui.activity.post;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wanderfunmobile.databinding.ActivityAddEditPostBinding;
import com.example.wanderfunmobile.databinding.BottomSheetPostOptionHorizontalBinding;
import com.example.wanderfunmobile.databinding.BottomSheetPostOptionVerticalBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class AddEditPostActivity extends AppCompatActivity {

    private ActivityAddEditPostBinding viewBinding;

    private BottomSheetPostOptionHorizontalBinding postOptionHorizontalBottomSheet;

    private BottomSheetPostOptionVerticalBinding postOptionVerticalBottomSheet;

    private BottomSheetBehavior<ConstraintLayout> postOptionHorizontalBottomSheetBehavior;
    private BottomSheetBehavior<ConstraintLayout> postOptionVerticalBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityAddEditPostBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        postOptionHorizontalBottomSheet = viewBinding.postOptionHorizontal;
        postOptionHorizontalBottomSheetBehavior = BottomSheetBehavior.from(postOptionHorizontalBottomSheet.getRoot());
        postOptionHorizontalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        
        postOptionVerticalBottomSheet = viewBinding.postOptionVertical;
        postOptionVerticalBottomSheetBehavior = BottomSheetBehavior.from(postOptionVerticalBottomSheet.getRoot());
        postOptionVerticalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        postOptionHorizontalBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    postOptionVerticalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    postOptionVerticalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        postOptionVerticalBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    postOptionHorizontalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    postOptionHorizontalBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }
}