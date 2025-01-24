package com.example.wanderfunmobile.infrastructure.ui.activity.trip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.wanderfunmobile.domain.model.Place;
import com.example.wanderfunmobile.infrastructure.ui.activity.place.SearchPlaceActivity;
import com.example.wanderfunmobile.infrastructure.util.DateTimeUtil;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TripPlaceCreateActivity extends AppCompatActivity {

    private ActivityTripPlaceCreateBinding viewBinding;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Place selectedPlace;
    private PlaceViewModel placeViewModel;
    @Inject
    ObjectMapper objectMapper;
    private Date startTime = null;
    private Date endTime = null;
    private MaterialDatePicker.Builder<Long> startDatePickerBuilder;
    private MaterialDatePicker<Long> startDatePicker;
    private MaterialDatePicker.Builder<Long> endDatePickerBuilder;
    private MaterialDatePicker<Long> endDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityTripPlaceCreateBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        placeViewModel.getPlaceByIdResponseLiveData().observe(this, response -> {
            if (!response.isError()) {
                selectedPlace = objectMapper.map(response.getData(), Place.class);
                viewBinding.placeName.setText(selectedPlace.getName());
            }
        });

        // Back button
        viewBinding.backButton.findViewById(R.id.button).setOnClickListener(v -> finish());

        // Select place button
        TextView selectPlaceButton = viewBinding.selectPlaceButton.findViewById(R.id.button);
        selectPlaceButton.setText("Chọn");
        int paddingVertical = getResources().getDimensionPixelSize(R.dimen.button_vertical_padding_extra_small);
        selectPlaceButton.setPadding(
                selectPlaceButton.getPaddingLeft(),
                paddingVertical,
                selectPlaceButton.getPaddingRight(),
                paddingVertical
        );

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Long placeId = result.getData().getLongExtra("selected_place", 0);
                if (placeId != 0) {
                    placeViewModel.getPlaceById(placeId);
                }
            }
        });

        selectPlaceButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchPlaceActivity.class);
            activityResultLauncher.launch(intent);
        });

        // Start date picker
        startDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        startDatePickerBuilder.setTitleText("Chọn ngày");
        startDatePickerBuilder.setTheme(R.style.CustomMaterialDatePicker);

        // End date picker
        endDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        endDatePickerBuilder.setTitleText("Chọn ngày");
        endDatePickerBuilder.setTheme(R.style.CustomMaterialDatePicker);

        // Get start time
        String startTimeString = getIntent().getStringExtra("limit_start_time");
        if (startTimeString != null && !startTimeString.isEmpty()) {
            try {
                startTime = DateTimeUtil.stringToDate(startTimeString);
                viewBinding.startTime.setText(startTimeString);
                viewBinding.startTime.setClickable(false);
            } catch (ParseException e) {
                Toast.makeText(this, "Erorr parsing date", Toast.LENGTH_SHORT).show();
            }
        } else {
            viewBinding.startTime.setOnClickListener(v -> {
                if (endTime != null) {
                    startDatePicker = setMaxDate(startDatePickerBuilder, endTime);
                } else {
                    startDatePicker = startDatePickerBuilder.build();
                }
                startDatePicker.addOnPositiveButtonClickListener(selection -> {
                    startTime = new Date(selection);
                    String formattedDate = DateTimeUtil.dateToString(new Date(selection));
                    viewBinding.startTime.setText(formattedDate);
                });
                startDatePicker.show(getSupportFragmentManager(), "START_DATE_PICKER");
            });
        }

        viewBinding.endTime.setOnClickListener(v -> {
            if (startTime != null) {
                endDatePicker = setMinDate(endDatePickerBuilder, startTime);
            } else {
                endDatePicker = endDatePickerBuilder.build();
            }
            endDatePicker.addOnPositiveButtonClickListener(selection -> {
                endTime = new Date(selection);
                String formattedDate = DateTimeUtil.dateToString(new Date(selection));
                viewBinding.endTime.setText(formattedDate);
            });
            endDatePicker.show(getSupportFragmentManager(), "END_DATE_PICKER");
        });

        TextView cancelButton = viewBinding.cancelButton.findViewById(R.id.button);
        cancelButton.setText("Hủy");
        cancelButton.setOnClickListener(v -> finish());

        TextView saveButton = viewBinding.confirmButton.findViewById(R.id.button);
        saveButton.setText("Xong");
        saveButton.setOnClickListener(v -> {
            if (selectedPlace != null && startTime != null && endTime != null) {
                if (startTime.before(endTime)) {
                    Intent intent = new Intent();
                    intent.putExtra("selected_place", selectedPlace.getId());
                    intent.putExtra("start_time", DateTimeUtil.dateToString(startTime));
                    intent.putExtra("end_time", DateTimeUtil.dateToString(endTime));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, "Thời gian kết thúc phải sau thời gian bắt đầu", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn địa điểm và thời gian", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MaterialDatePicker<Long> setMaxDate(MaterialDatePicker.Builder<Long> builder, Date date) {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setEnd(date.getTime());
        builder.setCalendarConstraints(constraintsBuilder.build());
        return builder.build();
    }

    private MaterialDatePicker<Long> setMinDate(MaterialDatePicker.Builder<Long> builder, Date date) {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(date.getTime());
        builder.setCalendarConstraints(constraintsBuilder.build());
        return builder.build();
    }

    private MaterialDatePicker<Long> setMaxAndMinDate(MaterialDatePicker.Builder<Long> builder, Date startDate, Date endDate) {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(startDate.getTime());
        constraintsBuilder.setEnd(endDate.getTime());
        builder.setCalendarConstraints(constraintsBuilder.build());
        return builder.build();
    }
}