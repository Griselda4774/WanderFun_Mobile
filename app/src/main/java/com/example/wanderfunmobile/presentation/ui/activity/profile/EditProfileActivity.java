package com.example.wanderfunmobile.presentation.ui.activity.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.CloudinaryUtil;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.dto.cloudinary.CloudinaryImageDto;
import com.example.wanderfunmobile.databinding.ActivityEditProfileBinding;
import com.example.wanderfunmobile.domain.model.images.Image;
import com.example.wanderfunmobile.domain.model.users.User;
import com.example.wanderfunmobile.presentation.viewmodel.UserViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding viewBinding;
    private UserViewModel userViewModel;

    private User user;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    private Uri imageUri;
    
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

        setUpMediaLauncher();

        setUpActivity();

        setUpViewModel();

        getUserData();
    }

    private void setUpMediaLauncher() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                imageUri = uri;
                Glide.with(this).load(uri).into(viewBinding.avatarSelector.avatar);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
    }

    private void setUpActivity() {
        // Back button
        ConstraintLayout backButton = viewBinding.backButton.button;
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Date picker
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Chọn ngày");
        builder.setTheme(R.style.CustomMaterialDatePicker);
        MaterialDatePicker<Long> datePicker = builder.build();
        EditText dateEditText = viewBinding.birthdayInput.input.dateEdittext;
        dateEditText.setOnClickListener(v -> {
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });
        datePicker.addOnPositiveButtonClickListener(selection -> {
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(selection));

            dateEditText.setText(formattedDate);
        });

        // Gender selector
        AutoCompleteTextView autoCompleteTextView = viewBinding.genderSelector.input.autoCompleteText;
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

        // Confirm button
        viewBinding.confirmButton.button.setText("Xong");
        viewBinding.confirmButton.button.setOnClickListener(v -> {
            updateUserData();
        });

        // Cancel button
        viewBinding.cancelButton.button.setText("Hủy");
        viewBinding.cancelButton.button.setOnClickListener(v -> {
            finish();
        });

        // Avatar selector
        viewBinding.avatarSelector.pickImageButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));
    }

    private void setUpViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUpdateSelfInfoResponseLiveData().observe(this, result -> {
            if (!result.isError()) {
                viewBinding.loadingDialog.hide();
                Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("status", "profile_updated");
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                viewBinding.loadingDialog.hide();
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getUserData() {
        Intent intent = getIntent();
        String userJson = intent.getStringExtra("user");
        if (userJson != null && !userJson.isEmpty()) {
            Gson gson = new Gson();
            user = gson.fromJson(userJson, User.class);
            bindUserData(user);
        }
    }

    private void bindUserData(User user) {
        if (user != null) {
            if (user.getAvatarImage() != null) {
                Glide.with(this)
                        .load(user.getAvatarImage().getImageUrl())
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(viewBinding.avatarSelector.avatar);
            } else {
                viewBinding.avatarSelector.avatar.setImageResource(R.drawable.ic_avatar);
            }

            if (user.getFirstName() != null) {
                viewBinding.firstnameInput.input.textEdittext.setText(user.getFirstName());
            }

            if (user.getLastName() != null) {
                viewBinding.lastnameInput.input.textEdittext.setText(user.getLastName());
            }

            if (user.getDateOfBirth() != null) {
                viewBinding.birthdayInput.input.dateEdittext
                        .setText(DateTimeUtil.localDateToString(user.getDateOfBirth()));
            }

            if (user.getGender() != null && !user.getGender().isEmpty()) {
                viewBinding.genderSelector.input.autoCompleteText.setText(user.getGender(), false);
            } else {
                viewBinding.genderSelector.input.autoCompleteText.setText("Không muốn đề cập", false);
            }

            if (user.getPhoneNumber() != null) {
                viewBinding.phoneInput.input.phoneEdittext.setText(user.getPhoneNumber());
            } else {
                viewBinding.phoneInput.input.phoneEdittext.setText("");
            }
        }
    }

    private void updateUserData() {
        viewBinding.loadingDialog.show();
        User updatedUser = new User();
        String firstName = viewBinding.firstnameInput.input.textEdittext.getText().toString();
        String lastName = viewBinding.lastnameInput.input.textEdittext.getText().toString();
        String dateOfBirth = viewBinding.birthdayInput.input.dateEdittext.getText().toString();
        String gender = viewBinding.genderSelector.input.autoCompleteText.getText().toString();
        String phoneNumber = viewBinding.phoneInput.input.phoneEdittext.getText().toString();
        if (!firstName.isEmpty()) {
            updatedUser.setFirstName(firstName);
        } else {
            Toast.makeText(this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!lastName.isEmpty()) {
            updatedUser.setLastName(lastName);
        } else {
            Toast.makeText(this, "Vui lòng nhập họ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dateOfBirth.isEmpty()) {
            updatedUser.setDateOfBirth(DateTimeUtil.stringToLocalDate(dateOfBirth));
        } else {
            updatedUser.setDateOfBirth(null);
        }

        if (!gender.isEmpty()) {
            updatedUser.setGender(gender);
        } else {
            updatedUser.setGender(null);
        }

        if (!phoneNumber.isEmpty()) {
            updatedUser.setPhoneNumber(phoneNumber);
        } else {
            updatedUser.setPhoneNumber(null);
        }

        if (viewBinding.avatarSelector.avatar.getDrawable() != null && imageUri != null) {
            String folderName = "/wanderfun/users/" + "user_" + SessionManager.getInstance(getApplicationContext()).getUserId().toString();
            String fileName = "avatar_user_" + SessionManager.getInstance(getApplicationContext()).getUserId().toString() + "_" + System.currentTimeMillis();
            CloudinaryUtil.uploadImageToCloudinary(getApplicationContext(), imageUri, fileName, folderName, new CloudinaryUtil.CloudinaryCallback() {
                @Override
                public void onSuccess(CloudinaryImageDto result) {
                    updatedUser.setAvatarImage(new Image());
                    updatedUser.getAvatarImage().setImageUrl(result.getUrl());
                    updatedUser.getAvatarImage().setImagePublicId(result.getPublicId());
                    userViewModel.updateSelfInfo("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), updatedUser);
                }

                @Override
                public void onError(String error) {
                    updatedUser.setAvatarImage(null);
                    userViewModel.updateSelfInfo("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), updatedUser);
                }
            });
        } else {
            updatedUser.setAvatarImage(null);
            userViewModel.updateSelfInfo("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), updatedUser);
        }
    }
}