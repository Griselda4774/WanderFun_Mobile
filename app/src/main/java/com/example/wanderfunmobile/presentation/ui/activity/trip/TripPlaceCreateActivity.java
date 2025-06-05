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
import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;
import com.example.wanderfunmobile.data.dto.place.PlaceDto;
import com.example.wanderfunmobile.data.dto.tripplace.TripPlaceDto;
import com.example.wanderfunmobile.databinding.ActivityTripPlaceCreateBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;
import com.example.wanderfunmobile.presentation.ui.activity.place.SearchPlaceActivity;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.parceler.Parcels;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
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

    private boolean isEdit = false;
    private int updatePosition = -1;
    private Date startTime, endTime;
    private EditText noteEditText;
    private TextView cancelBtn;
    private TextView confirmBtn;

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

        noteEditText = binding.inputNote.findViewById(R.id.text_edittext);
        cancelBtn = binding.cancelButton.findViewById(R.id.button);
        confirmBtn = binding.confirmButton.findViewById(R.id.button);

        initViewModel();
        initPlacePicker();
        receiveIntentData();
        setupPickers();
        setupButtons();
    }

    private void initViewModel() {
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        placeViewModel.getFindPlaceByIdResponseLiveData().observe(this, result -> {
            if (!result.isError()) {
                selectedPlace = result.getData();
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
                        if (placeId != 0) placeViewModel.findPlaceById(placeId);
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
            TripPlaceDto tripPlace = Parcels.unwrap(getIntent().getParcelableExtra("selected_place"));

            isEdit = getIntent().getBooleanExtra("is_edit", false);
            updatePosition = getIntent().getIntExtra("position", -1);

            if(tripPlace != null) {
                selectedPlace = objectMapper.map(tripPlace.getPlace(), Place.class);
                binding.placeName.setText(tripPlace.getPlace().getName());

                noteEditText.setText(tripPlace.getPlaceNotes());

                startTime = DateTimeUtil.stringToDate(DateTimeUtil.localDateToString(tripPlace.getStartTime()));
                binding.startTime.setText(DateTimeUtil.localDateToString(tripPlace.getStartTime()));

                endTime = DateTimeUtil.stringToDate(DateTimeUtil.localDateToString(tripPlace.getEndTime()));
                binding.endTime.setText(DateTimeUtil.localDateToString(tripPlace.getEndTime()));
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

        binding.startTime.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Chọn ngày bắt đầu");

            Date minDate = isEdit && startTime != null ? startTime : new Date();  // kiểm tra chỉnh sửa hay tạo mới

            MaterialDatePicker<Long> picker = buildDatePickerFrom(builder, minDate);
            picker.addOnPositiveButtonClickListener(selection -> {
                startTime = new Date(selection);
                binding.startTime.setText(DateTimeUtil.dateToString(startTime));
            });
            picker.show(getSupportFragmentManager(), "START_DATE");
        });

        binding.endTime.setOnClickListener(v -> {
            if (startTime == null) {
                Toast.makeText(this, "Vui lòng chọn ngày bắt đầu trước", Toast.LENGTH_SHORT).show();
                return;
            }

            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Chọn ngày kết thúc");

            MaterialDatePicker<Long> picker = buildEndDatePickerFromStart(builder, startTime);
            picker.addOnPositiveButtonClickListener(selection -> {
                endTime = new Date(selection);
                binding.endTime.setText(DateTimeUtil.dateToString(endTime));
            });

            picker.show(getSupportFragmentManager(), "END_DATE");
        });
    }

    private void setupButtons() {
        binding.backButton.findViewById(R.id.button).setOnClickListener(v -> finish());

        cancelBtn.setText("Hủy");
        cancelBtn.setOnClickListener(v -> finish());

        confirmBtn.setText("Xong");
        confirmBtn.setOnClickListener(v -> {
            if (selectedPlace != null && startTime != null && endTime != null) {
                if (startTime.before(endTime)) {
                    Intent resultIntent = new Intent();
                    TripPlaceDto resultTripPlaceDto = new TripPlaceDto();
                    resultTripPlaceDto.setPlace(objectMapper.map(selectedPlace, MiniPlaceDto.class));
                    resultTripPlaceDto.setStartTime(DateTimeUtil.dateToLocalDate(startTime));
                    resultTripPlaceDto.setEndTime(DateTimeUtil.dateToLocalDate(endTime));
                    resultTripPlaceDto.setPlaceNotes(noteEditText.getText().toString());

                    if(!isEdit) {
                        resultIntent.putExtra("created_place", Parcels.wrap(resultTripPlaceDto));
                    } else {
                        resultIntent.putExtra("updated_place", Parcels.wrap(resultTripPlaceDto));
                        resultIntent.putExtra("position", updatePosition);
                    }

                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(this, "Thời gian kết thúc phải sau bắt đầu", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn địa điểm và thời gian", Toast.LENGTH_SHORT).show();
            }
            isEdit = false;
        });
    }

    private MaterialDatePicker<Long> buildDatePickerFrom(MaterialDatePicker.Builder<Long> builder, Date minDate) {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        long min;
        if (isEdit && minDate != null) {
            min = Math.min(minDate.getTime(), System.currentTimeMillis());
        } else {
            min = System.currentTimeMillis();
        }

        CalendarConstraints.DateValidator validator = DateValidatorPointForward.from(min);

        constraintsBuilder.setValidator(validator);
        builder.setCalendarConstraints(constraintsBuilder.build());
        return builder.build();
    }

    private MaterialDatePicker<Long> buildEndDatePickerFromStart(MaterialDatePicker.Builder<Long> builder, Date startTime) {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        long min = Math.min(startTime.getTime(), System.currentTimeMillis());

        CalendarConstraints.DateValidator validator = DateValidatorPointForward.from(min);

        constraintsBuilder.setValidator(validator);
        builder.setCalendarConstraints(constraintsBuilder.build());

        return builder.build();
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
