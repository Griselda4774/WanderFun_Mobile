package com.example.wanderfunmobile.presentation.ui.activity.trip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.ActivityTripPlaceCreateBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.activity.place.SearchPlaceActivity;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TripPlaceCreateActivity extends AppCompatActivity {

    private ActivityTripPlaceCreateBinding binding;
    private ActivityResultLauncher<Intent> placePickerLauncher;
    private Place selectedPlace;
    private PlaceViewModel placeViewModel;
    @Inject
    ObjectMapper objectMapper;

    private Date startTime, endTime;
    private MaterialDatePicker.Builder<Long> startPickerBuilder, endPickerBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTripPlaceCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
            return insets;
        });

        initViewModel();
        initPlacePicker();
        receiveIntentData();
        setupPickers();
        setupButtons();
    }

    private void initViewModel() {
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        placeViewModel.getPlaceByIdResponseLiveData().observe(this, response -> {
            if (!response.isError()) {
                selectedPlace = objectMapper.map(response.getData(), Place.class);
                binding.placeName.setText(selectedPlace.getName());
            }
        });
    }

    private void initPlacePicker() {
        placePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        long placeId = result.getData().getLongExtra("selected_place", 0);
                        if (placeId != 0) placeViewModel.getPlaceById(placeId);
                    }
                });

        TextView selectPlaceBtn = binding.selectPlaceButton.findViewById(R.id.button);
        selectPlaceBtn.setText("Chọn");
        int padding = getResources().getDimensionPixelSize(R.dimen.button_vertical_padding_extra_small);
        selectPlaceBtn.setPadding(selectPlaceBtn.getPaddingLeft(), padding, selectPlaceBtn.getPaddingRight(), padding);
        selectPlaceBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchPlaceActivity.class);
            placePickerLauncher.launch(intent);
        });
    }

    private void receiveIntentData() {
        try {
            String name = getIntent().getStringExtra("selected_place_name");
            String startStr = getIntent().getStringExtra("selected_place_start_time");
            String endStr = getIntent().getStringExtra("selected_place_end_time");
            String note = getIntent().getStringExtra("selected_place_notes");

            EditText noteEditText = binding.inputNote.findViewById(R.id.text_edittext);

            if (!isNullOrEmpty(name)) binding.placeName.setText(name);
            if (!isNullOrEmpty(note)) noteEditText.setText(note);
            if (!isNullOrEmpty(startStr)) {
                startTime = DateTimeUtil.stringToDate(startStr);
                binding.startTime.setText(startStr);
            }
            if (!isNullOrEmpty(endStr)) {
                endTime = DateTimeUtil.stringToDate(endStr);
                binding.endTime.setText(endStr);
            }

            String limitStart = getIntent().getStringExtra("limit_start_time");
            if (!isNullOrEmpty(limitStart)) {
                startTime = DateTimeUtil.stringToDate(limitStart);
                binding.startTime.setText(limitStart);
                binding.startTime.setClickable(false);
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Lỗi xử lý thời gian", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupPickers() {
        startPickerBuilder = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày")
                .setTheme(R.style.CustomMaterialDatePicker);

        endPickerBuilder = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày")
                .setTheme(R.style.CustomMaterialDatePicker);

        binding.startTime.setOnClickListener(v -> {
            MaterialDatePicker<Long> picker = (endTime != null)
                    ? setMaxDate(startPickerBuilder, endTime)
                    : startPickerBuilder.build();

            picker.addOnPositiveButtonClickListener(selection -> {
                startTime = new Date(selection);
                binding.startTime.setText(DateTimeUtil.dateToString(startTime));
            });
            picker.show(getSupportFragmentManager(), "START_DATE");
        });

        binding.endTime.setOnClickListener(v -> {
            MaterialDatePicker<Long> picker = (startTime != null)
                    ? setMinDate(endPickerBuilder, startTime)
                    : endPickerBuilder.build();

            picker.addOnPositiveButtonClickListener(selection -> {
                endTime = new Date(selection);
                binding.endTime.setText(DateTimeUtil.dateToString(endTime));
            });
            picker.show(getSupportFragmentManager(), "END_DATE");
        });
    }

    private void setupButtons() {
        binding.backButton.findViewById(R.id.button).setOnClickListener(v -> finish());

        TextView cancelBtn = binding.cancelButton.findViewById(R.id.button);
        cancelBtn.setText("Hủy");
        cancelBtn.setOnClickListener(v -> finish());

        TextView confirmBtn = binding.confirmButton.findViewById(R.id.button);
        confirmBtn.setText("Xong");
        confirmBtn.setOnClickListener(v -> {
            if (selectedPlace != null && startTime != null && endTime != null) {
                if (startTime.before(endTime)) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("place_id", selectedPlace.getId());
                    resultIntent.putExtra("place_name", selectedPlace.getName());
                    resultIntent.putExtra("place_cover_image", selectedPlace.getCoverImage().getImageUrl());
                    resultIntent.putExtra("start_time", DateTimeUtil.dateToString(startTime));
                    resultIntent.putExtra("end_time", DateTimeUtil.dateToString(endTime));
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(this, "Thời gian kết thúc phải sau bắt đầu", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn địa điểm và thời gian", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MaterialDatePicker<Long> setMaxDate(MaterialDatePicker.Builder<Long> builder, Date date) {
        return builder.setCalendarConstraints(
                new CalendarConstraints.Builder().setEnd(date.getTime()).build()
        ).build();
    }

    private MaterialDatePicker<Long> setMinDate(MaterialDatePicker.Builder<Long> builder, Date date) {
        return builder.setCalendarConstraints(
                new CalendarConstraints.Builder().setStart(date.getTime()).build()
        ).build();
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
