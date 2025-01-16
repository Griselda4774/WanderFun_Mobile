package com.example.wanderfunmobile.infrastructure.ui.activity.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityEditProfileBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding viewBinding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button
        ConstraintLayout backButton = viewBinding.backButton.findViewById(R.id.button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Date picker
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Chọn ngày");
        builder.setTheme(R.style.CustomMaterialDatePicker);
        MaterialDatePicker<Long> datePicker = builder.build();
        EditText dateEditText = viewBinding.birthdayInput.findViewById(R.id.date_edittext);
        dateEditText.setOnClickListener(v -> {
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });
        datePicker.addOnPositiveButtonClickListener(selection -> {
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(selection));

            dateEditText.setText(formattedDate);
        });

        // Gender selector
        AutoCompleteTextView autoCompleteTextView = viewBinding.genderSelector.findViewById(R.id.auto_complete_text);
        String[] options = {"Nam", "Nữ", "Khác", "Không muốn đề cập"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_drop_down, options) {
            @NonNull
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        results.values = options;
                        results.count = options.length;
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        notifyDataSetChanged();
                    }
                };
            }
        };
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setText(autoCompleteTextView.getAdapter().getItem(0).toString(), false);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = parent.getItemAtPosition(position).toString();
            autoCompleteTextView.setText(selectedItem);
            autoCompleteTextView.dismissDropDown();
        });
    }
}