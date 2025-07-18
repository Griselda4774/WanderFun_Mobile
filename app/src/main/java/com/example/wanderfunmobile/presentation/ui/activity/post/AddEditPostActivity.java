package com.example.wanderfunmobile.presentation.ui.activity.post;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.wanderfunmobile.core.util.StringUtil;
import com.example.wanderfunmobile.data.dto.cloudinary.CloudinaryImageDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.databinding.ActivityAddEditPostBinding;
import com.example.wanderfunmobile.databinding.BottomSheetPostOptionHorizontalBinding;
import com.example.wanderfunmobile.databinding.BottomSheetPostOptionVerticalBinding;
import com.example.wanderfunmobile.domain.model.images.Image;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.domain.model.trips.Trip;
import com.example.wanderfunmobile.domain.model.users.User;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.PlaceSelectionDialog;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.TripSelectionDialog;
import com.example.wanderfunmobile.presentation.viewmodel.posts.PostViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.parceler.Parcels;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditPostActivity extends AppCompatActivity {

    private ActivityAddEditPostBinding viewBinding;

    private BottomSheetPostOptionHorizontalBinding postOptionHorizontalBottomSheet;

    private BottomSheetPostOptionVerticalBinding postOptionVerticalBottomSheet;

    private BottomSheetBehavior<ConstraintLayout> postOptionHorizontalBottomSheetBehavior;
    private BottomSheetBehavior<ConstraintLayout> postOptionVerticalBottomSheetBehavior;

    private Long postId;
    private Trip taggedTrip;
    private Place taggedPlace;
    private UserViewModel userViewModel;
    private PostViewModel postViewModel;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    private Uri imageUri;
    private LoadingDialog loadingDialog;

    @Inject
    ObjectMapper objectMapper;

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

        // Initialize loading dialog
        loadingDialog = new LoadingDialog(this);

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Observe view model
        userViewModel.getMiniSelfInfoResponseLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                User user = result.getData();
                bindUserData(user);
            } else {
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        postViewModel.getFindPostByIdLiveData().observe(this, result -> {
            if (!result.isError() && result.getData() != null) {
                Post post = result.getData();
                bindPostData(post);

            } else {
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        postViewModel.getCreatePostLiveData().observe(this, result -> {
            if (!result.isError()) {
                loadingDialog.hide();
                Toast.makeText(getApplicationContext(), "Đăng bài thành công", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("status", "post_created");
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                loadingDialog.hide();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        postViewModel.getUpdatePostLiveData().observe(this, result -> {
            if (!result.isError()) {
                loadingDialog.hide();
                Toast.makeText(getApplicationContext(), "Sửa bài viết thành công", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("status", "post_updated");
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                loadingDialog.hide();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        // Get postId from intent
        postId = getIntent().getLongExtra("postId", -1L);
        if (postId > 0) {
            postViewModel.findPostById(postId);
        } else {
            viewBinding.image.setVisibility(GONE);
            viewBinding.removeImageButton.getRoot().setVisibility(GONE);
        }

        // Fetch user info
        userViewModel.getMiniSelfInfo("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken());

        // Initialize pick media launcher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                imageUri = uri;
                Glide.with(this).load(uri).into(viewBinding.image);
                viewBinding.image.setVisibility(ImageView.VISIBLE);
                viewBinding.removeImageButton.getRoot().setVisibility(View.VISIBLE);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        // Get shared trip data from intent
        if (getIntent().hasExtra("shared_trip") && getIntent().getParcelableExtra("shared_trip") != null) {
            taggedTrip = objectMapper.map(Parcels.unwrap(getIntent().getParcelableExtra("shared_trip")), Trip.class);
            bindTripData(taggedTrip);
        } else {
            taggedTrip = null;
            viewBinding.tagTripContainer.setVisibility(GONE);
        }

        viewBinding.tagPlaceContainer.setVisibility(GONE);


        // Initialize bottom sheet
        initBottomSheet();
        initBottomSheetButtons();
        initActivityButtons();

        viewBinding.removeImageButton.getRoot().setVisibility(GONE);
        viewBinding.removeImageButton.getRoot().setOnClickListener(v -> {
            viewBinding.removeImageButton.getRoot().setVisibility(GONE);
            viewBinding.image.setVisibility(GONE);
            viewBinding.image.setImageDrawable(null);
            imageUri = null;
        });

        viewBinding.removeTripButton.getRoot().setVisibility(View.VISIBLE);
        viewBinding.removeTripButton.getRoot().setOnClickListener(v -> {
            taggedTrip = null;
            viewBinding.tagTripContainer.setVisibility(GONE);
        });

        viewBinding.removePlaceButton.getRoot().setVisibility(View.VISIBLE);
        viewBinding.removePlaceButton.getRoot().setOnClickListener(v -> {
            taggedPlace = null;
            viewBinding.tagPlaceContainer.setVisibility(GONE);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void initBottomSheet() {
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

    @SuppressLint("SetTextI18n")
    private void bindUserData(User user) {
        // User name
        viewBinding.userName.setText(user.getFirstName() + " " + user.getLastName());

        // User avatar
        if (user.getAvatarImage() != null) {
            Glide.with(viewBinding.getRoot())
                    .load(user.getAvatarImage().getImageUrl())
                    .error(R.drawable.ic_avatar)
                    .into(viewBinding.userAvatar);
        } else {
            viewBinding.userAvatar.setImageResource(R.drawable.ic_avatar);
        }
    }

    @SuppressLint("SetTextI18n")
    private void bindPostData(Post post) {
        viewBinding.textEdittext.setText(post.getContent());

        if (post.getImage() != null) {
            Glide.with(this)
                    .load(post.getImage().getImageUrl())
                    .error(R.drawable.chill_image)
                    .into(viewBinding.image);
            viewBinding.image.setVisibility(ImageView.VISIBLE);
            viewBinding.removeImageButton.getRoot().setVisibility(View.VISIBLE);
        } else {
            viewBinding.image.setVisibility(GONE);
            viewBinding.removeImageButton.getRoot().setVisibility(GONE);
        }

        if (post.getTrip() != null) {
            taggedTrip = post.getTrip();
            bindTripData(taggedTrip);
        } else {
            taggedTrip = null;
            viewBinding.tagTripContainer.setVisibility(GONE);
        }

        if (post.getPlace() != null) {
            taggedPlace = post.getPlace();
            bindPlaceData(taggedPlace);
        } else {
            taggedPlace = null;
            viewBinding.tagPlaceContainer.setVisibility(GONE);
        }
    }

    private void bindTripData(Trip trip) {
        viewBinding.tagTripContainer.setVisibility(View.VISIBLE);
        viewBinding.tagPlaceContainer.setVisibility(View.GONE);
        taggedPlace = null;

        viewBinding.trip.name.setText(trip.getName());
        viewBinding.trip.startTime.setText(DateTimeUtil.localDateToString(trip.getStartTime()));
        viewBinding.trip.endTime.setText(DateTimeUtil.localDateToString(trip.getEndTime()));

        taggedTrip = trip;
    }

    private void bindPlaceData(Place place) {
        viewBinding.tagPlaceContainer.setVisibility(View.VISIBLE);
        viewBinding.tagTripContainer.setVisibility(View.GONE);
        taggedTrip = null;

        viewBinding.place.placeName.setText(place.getName());
        viewBinding.place.placeRating.setText(String.valueOf(place.getRating()));
        viewBinding.place.address.setText(StringUtil.formatAddressToStringNoStreet(place.getAddress()));
        if (place.getCoverImage() != null) {
            Glide.with(this)
                    .load(place.getCoverImage().getImageUrl())
                    .error(R.drawable.chill_image)
                    .into(viewBinding.place.placeCoverImage);
        } else {
            viewBinding.place.placeCoverImage.setImageResource(R.drawable.chill_image);
        }
        taggedPlace = place;
    }

    private void initBottomSheetButtons() {
        viewBinding.postOptionVertical.addImageButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        viewBinding.postOptionHorizontal.addImageButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        viewBinding.postOptionHorizontal.addTripButton.setOnClickListener(v -> {
            TripSelectionDialog tripSelectionDialog = new TripSelectionDialog(
                    AddEditPostActivity.this,
                    trip -> {
//                        taggedTrip = trip;
                        bindTripData(trip);
                    },
                    objectMapper
            );
            tripSelectionDialog.show();
        });

        viewBinding.postOptionVertical.addTripButton.setOnClickListener(v -> {
            TripSelectionDialog tripSelectionDialog = new TripSelectionDialog(
                    AddEditPostActivity.this,
                    trip -> {
//                        taggedTrip = trip;
                        bindTripData(trip);
                    },
                    objectMapper
            );
            tripSelectionDialog.show();
        });

        viewBinding.postOptionHorizontal.addPlaceButton.setOnClickListener(v -> {
            PlaceSelectionDialog placeSelectionDialog = new PlaceSelectionDialog(
                    AddEditPostActivity.this,
                    place -> {
                        taggedPlace = place;
                        bindPlaceData(place);
                    },
                    objectMapper
            );
            placeSelectionDialog.show();
        });

        viewBinding.postOptionVertical.addPlaceButton.setOnClickListener(v -> {
            PlaceSelectionDialog placeSelectionDialog = new PlaceSelectionDialog(
                    AddEditPostActivity.this,
                    place -> {
                        place = objectMapper.map(place, Place.class);
                        bindPlaceData(place);
                    },
                    objectMapper
            );
            placeSelectionDialog.show();
        });
    }

    private void initActivityButtons() {
        viewBinding.backButton.button.setOnClickListener(v -> {
            finish();
        });

        viewBinding.postButton.setOnClickListener(v -> {
            loadingDialog.show();
            Post postCreate = new Post();
            postCreate.setTrip(taggedTrip);
            postCreate.setPlace(taggedPlace);
            if (viewBinding.textEdittext.getText() != null && !viewBinding.textEdittext.getText().toString().isEmpty()) {
                postCreate.setContent(viewBinding.textEdittext.getText().toString());
            } else {
                loadingDialog.hide();
                Toast.makeText(this, "Nội dung bài viết không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            if (viewBinding.image.getDrawable() != null && imageUri != null) {
                String folderName = "/wanderfun/posts/" + "user_" + SessionManager.getInstance(getApplicationContext()).getUserId().toString();
                String fileName = "post_user_" + SessionManager.getInstance(getApplicationContext()).getUserId().toString() + "_" + System.currentTimeMillis();
                CloudinaryUtil.uploadImageToCloudinary(getApplicationContext(), imageUri, fileName, folderName, new CloudinaryUtil.CloudinaryCallback() {
                    @Override
                    public void onSuccess(CloudinaryImageDto result) {
                        postCreate.setImage(new Image());
                        postCreate.getImage().setImageUrl(result.getUrl());
                        postCreate.getImage().setImagePublicId(result.getPublicId());
                        performCreateOrUpdatePost(postCreate);
                    }

                    @Override
                    public void onError(String error) {
                        postCreate.setImage(null);
                        performCreateOrUpdatePost(postCreate);                 }
                });
            } else {
                performCreateOrUpdatePost(postCreate);
            }
        });
    }

    private void performCreateOrUpdatePost(Post post) {
        if (postId <= 0) {
            postViewModel.createPost("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), post);
        } else {
            postViewModel.updatePost("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), postId, post);
        }
    }
}