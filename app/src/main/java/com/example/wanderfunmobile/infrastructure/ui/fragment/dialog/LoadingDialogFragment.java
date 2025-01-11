package com.example.wanderfunmobile.infrastructure.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LoadingDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ProgressBar progressBar = new ProgressBar(requireContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(progressBar);
        builder.setCancelable(false);
        return builder.create();
    }
}
