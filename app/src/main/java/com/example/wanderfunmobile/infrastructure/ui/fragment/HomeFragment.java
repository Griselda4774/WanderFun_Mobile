package com.example.wanderfunmobile.infrastructure.ui.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.application.dto.place.PlaceMiniDto;
import com.example.wanderfunmobile.databinding.BottomSheetLocationPinBinding;
import com.example.wanderfunmobile.databinding.BottomSheetPlaceInfoBinding;
import com.example.wanderfunmobile.databinding.FragmentHomeBinding;
import com.example.wanderfunmobile.domain.model.CheckIn;
import com.example.wanderfunmobile.domain.model.Place;
import com.example.wanderfunmobile.infrastructure.ui.adapter.place.PlaceInfoTabAdapter;
import com.example.wanderfunmobile.infrastructure.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.infrastructure.ui.custom.dialog.SelectionDialog;
import com.example.wanderfunmobile.infrastructure.ui.custom.starrating.StarRatingView;
import com.example.wanderfunmobile.infrastructure.util.BitMapUtil;
import com.example.wanderfunmobile.infrastructure.util.ColorHexUtil;
import com.example.wanderfunmobile.infrastructure.util.DateTimeUtil;
import com.example.wanderfunmobile.infrastructure.util.SessionManager;
import com.example.wanderfunmobile.infrastructure.util.ViewPager2HeightAdjuster;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.viewmodel.PlaceViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.maplibre.android.MapLibre;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.location.LocationComponent;
import org.maplibre.android.location.LocationComponentActivationOptions;
import org.maplibre.android.location.LocationComponentOptions;
import org.maplibre.android.location.engine.LocationEngineCallback;
import org.maplibre.android.location.engine.LocationEngineRequest;
import org.maplibre.android.location.engine.LocationEngineResult;
import org.maplibre.android.location.modes.CameraMode;
import org.maplibre.android.location.permissions.PermissionsManager;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Projection;
import org.maplibre.android.maps.Style;
import org.maplibre.android.plugins.annotation.Symbol;
import org.maplibre.android.plugins.annotation.SymbolManager;
import org.maplibre.android.plugins.annotation.SymbolOptions;
import org.maplibre.android.style.layers.Property;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private PlaceViewModel placeViewModel;
    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private Style mapStyle;
    private SymbolManager symbolManager;
    private Symbol currentMarker;

    private PermissionsManager permissionsManager;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;
    private LocationComponent locationComponent;
    private long lastUpdateTime = 0;
    private FragmentHomeBinding viewBinding;
    private String mapStyleUrl;

    private LinearLayout gpsButton;
    private Location currentLocation;

    private BottomSheetPlaceInfoBinding placeInfoBottomSheetBinding;
    private BottomSheetLocationPinBinding locationPinBottomSheetBinding;

    private BottomSheetBehavior<ConstraintLayout> placeInfoBottomSheetBehavior;
    private BottomSheetBehavior<ConstraintLayout> locationPinBottomSheetBehavior;
    private List<Place> placeList = new ArrayList<>();
    private Place currentPlace = null;
    @Inject
    ObjectMapper objectMapper;
    private LoadingDialog loadingDialog;
    private SelectionDialog selectionDialog;
    private boolean canCheckIn;
    private Long currentCheckInPlaceId;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init maplibre
        MapLibre.getInstance(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false);

        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        mapView = viewBinding.mapView;
        gpsButton = viewBinding.gpsButton;

        placeInfoBottomSheetBinding = viewBinding.placeInfoBottomSheetContainer;
        placeInfoBottomSheetBehavior = BottomSheetBehavior.from(placeInfoBottomSheetBinding.getRoot());
        placeInfoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        locationPinBottomSheetBinding = viewBinding.locationPinBottomSheetContainer;
        locationPinBottomSheetBehavior = BottomSheetBehavior.from(locationPinBottomSheetBinding.getRoot());
        locationPinBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Khởi tạo launcher cho yêu cầu quyền
        requestPermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean isLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);

                    if (Boolean.FALSE.equals(isLocationGranted)) {
                        Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Place info bottom sheet
        placeInfoBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_EXPANDED) {
                    placeInfoBottomSheetBinding.placeInfoBottomSheetContent.smoothScrollTo(0, 0);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    currentPlace = null;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        // Location pin info bottom sheet
        locationPinBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_EXPANDED) {
                    locationPinBottomSheetBinding.bottomSheetContent.smoothScrollTo(0, 0);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // Selection dialog
        selectionDialog = viewBinding.selectionDialog;
        selectionDialog.setOnAcceptListener(() -> {
            if (currentCheckInPlaceId != null) {
                placeViewModel.getCheckInByPlaceIdAndUserId("Bearer " + SessionManager.getInstance(requireActivity().getApplicationContext()).getAccessToken(), currentCheckInPlaceId);
            }
            selectionDialog.hide();
            Log.d("SelectionDialog", "Accept");
        });

        selectionDialog.setOnRejectListener(() -> {
            selectionDialog.hide();
            Log.d("SelectionDialog", "Reject");
        });

        // Loading dialog
        loadingDialog = viewBinding.loadingDialog;
        placeViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                loadingDialog.show();
                loadingDialog.setVisibility(View.VISIBLE);
            } else {
                loadingDialog.hide();
                loadingDialog.setVisibility(View.GONE);
            }
        });
        placeViewModel.getIsCheckingIn().observe(getViewLifecycleOwner(), isCheckingIn -> {
            if (isCheckingIn) {
                loadingDialog.show();
                loadingDialog.setVisibility(View.VISIBLE);
            } else {
                loadingDialog.hide();
                loadingDialog.setVisibility(View.GONE);
            }
        });

        // Map
        mapView.onCreate(savedInstanceState);
        mapStyleUrl = String.format("%s/assets/goong_map_web.json?api_key=%s",
                getString(R.string.goong_map_url),
                getString(R.string.goong_map_key));

        placeViewModel.getAllPlaces();
        placeViewModel.getAllPlacesResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.isError()) {
                List<PlaceMiniDto> placeDtoList = data.getData();
                if (placeDtoList.isEmpty()) {
                    placeDtoList = new ArrayList<>();
                }
                placeList = objectMapper.mapList(placeDtoList, Place.class);
            }
            mapView.getMapAsync(this);
        });

        placeViewModel.getPlaceByIdResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.isError()) {
                currentPlace = objectMapper.map(data.getData(), Place.class);
                showPlaceInfoBottomSheet(currentPlace);
            }
        });

        placeViewModel.getCheckInByPlaceIdAndUserIdResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.isError()) {
                CheckIn checkIn = objectMapper.map(data.getData(), CheckIn.class);
                if (System.currentTimeMillis() - checkIn.getLastCheckInTime().getTime() > 50000) {
                    canCheckIn = true;
                    placeViewModel.checkInPlace("Bearer " + SessionManager.getInstance(requireActivity().getApplicationContext()).getAccessToken(), currentCheckInPlaceId);
                } else {
                    canCheckIn = false;
                    Toast.makeText(requireContext(), "Bạn đã check-in tại đây trước đó", Toast.LENGTH_SHORT).show();
                }
            } else {
                canCheckIn = true;
                placeViewModel.checkInPlace("Bearer " + SessionManager.getInstance(requireActivity().getApplicationContext()).getAccessToken(), currentCheckInPlaceId);
            }
        });

        placeViewModel.checkInPlaceResponseLiveData().observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.isError()) {
                Toast.makeText(requireContext(), "Check-in thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Check-in thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        gpsButton.setOnClickListener(v -> {
            if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
                if (currentMarker != null) {
                    symbolManager.delete(currentMarker);
                    currentMarker = null;
                }
                initializeLocationComponent(mapStyle, mapLibreMap);
                mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                        .zoom(15)
                        .build()), 1000);

            } else {
                checkPermissions();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapLibreMap map) {
        map.setStyle(mapStyleUrl, style -> {
            symbolManager = new SymbolManager(mapView, map, style);
            symbolManager.setIconAllowOverlap(true);
            symbolManager.setIconIgnorePlacement(true);

            Bitmap markerBitmap = BitMapUtil.convertVectorToBitmap(requireContext(),
                    R.drawable.ic_location_pin, 100, 100);
            style.addImage("marker-icon", markerBitmap);

            if (!placeList.isEmpty()) {
                addPlaceImageToMap(requireContext(), style, placeList);
                drawPlaceMarker(symbolManager, placeList);
            }

            if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
                initializeLocationComponent(style, map);
            } else {
                map.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(new LatLng(10.775658, 106.695094))
                                .zoom(8)
                                .build()), 1000
                );
            }

            symbolManager.addClickListener(symbol -> {
                if (currentMarker != null) {
                    symbolManager.delete(currentMarker);
                    currentMarker = null;
                }

                focusOnLocation(symbol.getLatLng(), map, 17, -200);

                Gson gson = new Gson();
                String title;
                Place place = null;
                if (symbol.getData() != null) {
                    title = symbol.getData().getAsJsonObject().get("title").getAsString();
                    if (title.equals("Place Marker") && symbol.getData().getAsJsonObject().get("place") != null) {
                        place = gson.fromJson(symbol.getData().getAsJsonObject().get("place"), Place.class);
                    }
                }

                if (place != null) {
                    placeViewModel.getPlaceById(place.getId());
                }
                return true;
            });

            map.addOnMapClickListener(latLng -> {
                addMarkerAtLocation(latLng, map, symbolManager);
                return true;
            });

            mapStyle = style;
        });

        mapLibreMap = map;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
        if (currentPlace != null) {
            placeViewModel.getPlaceById(currentPlace.getId());
        } else {
            placeViewModel.getAllPlaces();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    private void checkPermissions() {
        if (!PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            requestPermissionsLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationComponent(@NonNull Style style, MapLibreMap mapLibreMap) {
        LocationComponent locationComponent = mapLibreMap.getLocationComponent();

        LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(requireContext())
                .pulseEnabled(true)
                .pulseColor(ContextCompat.getColor(requireContext(), R.color.blue2))
                .foregroundTintColor(ContextCompat.getColor(requireContext(), R.color.black5))
                .build();

        LocationComponentActivationOptions locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), style)
                        .locationComponentOptions(locationComponentOptions)
                        .useDefaultLocationEngine(true)
                        .locationEngineRequest(new LocationEngineRequest.Builder(30000)
                                .setFastestInterval(30000)
                                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                                .build())
                        .build();

        locationComponent.activateLocationComponent(locationComponentActivationOptions);

        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setCameraMode(CameraMode.TRACKING);


        assert locationComponent.getLocationEngine() != null;
        locationComponent.getLocationEngine().requestLocationUpdates(
                Objects.requireNonNull(locationComponentActivationOptions.locationEngineRequest()),
                locationEngineCallback,
                Looper.getMainLooper()
        );

        locationComponent.getLocationEngine().getLastLocation(locationEngineCallback);

        mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .zoom(8)
                .build()), 1000);
    }

    private final LocationEngineCallback<LocationEngineResult> locationEngineCallback = new LocationEngineCallback<LocationEngineResult>() {
        @Override
        public void onSuccess(LocationEngineResult result) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime < 30000) {
                return;
            }
            lastUpdateTime = currentTime;
            currentLocation = result.getLastLocation();
            if (currentLocation != null && !placeList.isEmpty()) {
                for (Place place : placeList) {
                    float[] results = new float[1];
                    Location.distanceBetween(
                            currentLocation.getLatitude(), currentLocation.getLongitude(),
                            place.getLatitude(), place.getLongitude(),
                            results
                    );
                    float distanceInMeters = results[0];
                    if (distanceInMeters <= 20) {
                        Log.d("UserLocation", "Place can check-in: " + place.getName());
                        currentCheckInPlaceId = place.getId();
                        selectionDialog.show("Có thể check-in tại đây",
                                "Địa điểm được tìm thấy là: " + place.getName(),
                                "Bạn có muốn check-in tại đây?", "Có", "Không");
                    }
                }

                Log.d("UserLocation", "Lat: " + currentLocation.getLatitude() + ", Lng: " + currentLocation.getLongitude());
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.e("LocationError", "Failed to get location updates", exception);
        }
    };

    private void addMarkerAtLocation(LatLng latLng, MapLibreMap map, SymbolManager symbolManager) {
        if (currentMarker != null) {
            symbolManager.delete(currentMarker);
            currentMarker = null;
            locationPinBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            showLocationPinBottomSheet(latLng);

            JsonObject data = new JsonObject();
            data.addProperty("title", "Location Marker");
            currentMarker = symbolManager.create(new SymbolOptions()
                    .withLatLng(latLng)
                    .withIconImage("marker-icon")
                    .withData(data)
            );

            focusOnLocation(latLng, map, 15);
        }
    }

    public void focusOnLocation(LatLng latLng, MapLibreMap mapLibreMap, double zoom) {
        double targetZoom = Math.max(mapLibreMap.getCameraPosition().zoom, zoom);

        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(targetZoom)
                .build();
        mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
    }

    public void focusOnLocation(LatLng latLng, MapLibreMap mapLibreMap, double zoom, int offsetY) {
        double currentZoom = mapLibreMap.getCameraPosition().zoom;
        double targetZoom = Math.max(currentZoom, zoom);

        CameraPosition zoomPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(targetZoom)
                .build();

        mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(zoomPosition), 500, new MapLibreMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Projection projection = mapLibreMap.getProjection();
                PointF screenPoint = projection.toScreenLocation(latLng);

                screenPoint.y -= offsetY;

                LatLng adjustedLatLng = projection.fromScreenLocation(screenPoint);

                CameraPosition offsetPosition = new CameraPosition.Builder()
                        .target(adjustedLatLng)
                        .zoom(targetZoom)
                        .build();

                mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(offsetPosition), 500);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void addPlaceImageToMap(Context context, Style style, List<Place> placeList) {
        for (Place place : placeList) {
            String transformUrl = MediaManager.get().url()
                    .transformation(new Transformation<>()
                            .width(160)
                            .height(160)
                            .crop("thumb")
                            .radius("max")
                            .background("transparent")
                            .fetchFormat("png"))
                    .generate(place.getCoverImagePublicId());
            BitMapUtil.getBitMapFromUrl(context, transformUrl, bitmap -> {
                style.addImage(place.getCoverImagePublicId(), bitmap);
            });
        }
    }

    private void drawPlaceMarker(SymbolManager symbolManager, List<Place> placeList) {
        for (Place place : placeList) {
            addPlaceMarker(symbolManager, place);
        }
    }

    private void addPlaceMarker(SymbolManager symbolManager, Place place) {
        Gson gson = new Gson();
        JsonObject placeJson = gson.toJsonTree(place).getAsJsonObject();
        JsonObject data = new JsonObject();
        data.addProperty("title", "Place Marker");
        data.add("place", placeJson);

        symbolManager.create(new SymbolOptions()
                .withLatLng(new LatLng(place.getLatitude(), place.getLongitude()))
                .withIconImage(place.getCoverImagePublicId())
                .withData(data)
                .withTextField(place.getName())
                .withTextFont(new String[]{"Roboto Medium"})
                .withTextSize(16f)
                .withTextColor(ColorHexUtil.getColorHexString(ContextCompat.getColor(requireContext(), R.color.black4)))
                .withTextHaloColor(ColorHexUtil.getColorHexString(ContextCompat.getColor(requireContext(), R.color.white1)))
                .withTextHaloWidth(5.0f)
                .withTextHaloBlur(0.0f)
                .withTextAnchor(Property.TEXT_ANCHOR_TOP_LEFT)
                .withTextOffset(new Float[]{0f, 2f})
        );
    }

    @SuppressLint("SetTextI18n")
    private void showPlaceInfoBottomSheet(Place place) {
        placeInfoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        locationPinBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Place info tab
        // View pager
        PlaceInfoTabAdapter placeInfoTabAdapter = new PlaceInfoTabAdapter(this);
        ViewPager2 viewPager = placeInfoBottomSheetBinding.viewPager;
        viewPager.setAdapter(placeInfoTabAdapter);
        ViewPager2HeightAdjuster.autoAdjustHeight(viewPager, true);

        // Tab layout
        TabLayout tabLayout = placeInfoBottomSheetBinding.tabLayout;
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            @SuppressLint("InflateParams") View customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item, null);
            TextView tabText = customView.findViewById(R.id.tab_text);
            tabText.setTextAppearance(R.style.Text_TabLabel);

            switch (position) {
                case 0:
                    tabText.setText("Tổng quan");
                    tabText.setTextAppearance(R.style.Text_TabLabel_Active_Blue);
                    break;
                case 1:
                    tabText.setText("Đánh giá");
                    break;
                case 2:
                    tabText.setText("Ảnh");
                    break;
                case 3:
                    tabText.setText("Giới thiệu");
                    break;
            }

            tab.setCustomView(customView);
        }).attach();

        tabLayout.selectTab(tabLayout.getTabAt(0));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tabText = Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tab_text);
                tabText.setTextAppearance(R.style.Text_TabLabel);
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tabText = Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tab_text);
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.blue2));
                tabText.setTextAppearance(R.style.Text_TabLabel_Active_Blue);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        TextView placeName = placeInfoBottomSheetBinding.placeName;
        placeName.setText(place.getName());

        TextView placeAddress = placeInfoBottomSheetBinding.placeAddressContent;
        placeAddress.setText(place.getAddress());

        StarRatingView starRatingView = placeInfoBottomSheetBinding.placeRatingView;
        int roundedRating = (int) Math.floor(place.getAverageRating());
        starRatingView.setRating(roundedRating);

        TextView placeRating = placeInfoBottomSheetBinding.placeRatingScore;
        placeRating.setText(String.valueOf(place.getAverageRating()));

        TextView placeRatingCount = placeInfoBottomSheetBinding.placeRatingCount;
        placeRatingCount.setText("(" + place.getFeedbacks().size() + ")");

        ConstraintLayout placeTimeOpening = placeInfoBottomSheetBinding.placeTimeOpening;
        TextView placeTimeOpeningTimeClose = placeInfoBottomSheetBinding.placeTimeOpeningTimeClose;
        ConstraintLayout placeTimeClosing = placeInfoBottomSheetBinding.placeTimeClosing;
        TextView placeTimeClosingTimeOpen = placeInfoBottomSheetBinding.placeTimeClosingTimeOpen;
        if (place.isClosing()) {
            placeTimeOpening.setVisibility(View.GONE);
            placeTimeClosing.setVisibility(View.GONE);
            placeInfoBottomSheetBinding.openAllDay.setVisibility(View.GONE);
            placeInfoBottomSheetBinding.closing.setVisibility(View.VISIBLE);
        } else {
            if (place.isOpenAllDay()) {
                placeTimeOpening.setVisibility(View.GONE);
                placeTimeClosing.setVisibility(View.GONE);
                placeInfoBottomSheetBinding.openAllDay.setVisibility(View.VISIBLE);
                placeInfoBottomSheetBinding.closing.setVisibility(View.GONE);
            } else {
                if (place.getTimeOpen() != null && place.getTimeClose() != null) {
                    LocalTime currentTime = LocalTime.now();
                    if (place.getTimeClose().isBefore(currentTime) || place.getTimeOpen().isAfter(currentTime)) {
                        placeTimeClosing.setVisibility(View.VISIBLE);
                        placeTimeOpening.setVisibility(View.GONE);
                        placeInfoBottomSheetBinding.openAllDay.setVisibility(View.GONE);
                        placeInfoBottomSheetBinding.closing.setVisibility(View.GONE);
                        placeTimeClosingTimeOpen.setText(place.getTimeOpen().toString());
                    } else {
                        placeTimeOpening.setVisibility(View.VISIBLE);
                        placeTimeClosing.setVisibility(View.GONE);
                        placeInfoBottomSheetBinding.openAllDay.setVisibility(View.GONE);
                        placeInfoBottomSheetBinding.closing.setVisibility(View.GONE);
                        placeTimeOpeningTimeClose.setText(DateTimeUtil.localTimeToString(place.getTimeClose()));
                    }
                } else {
                    placeTimeOpening.setVisibility(View.GONE);
                    placeTimeClosing.setVisibility(View.GONE);
                    placeInfoBottomSheetBinding.openAllDay.setVisibility(View.GONE);
                    placeInfoBottomSheetBinding.closing.setVisibility(View.GONE);
                }
            }
        }

        ImageView placeCoverImage = placeInfoBottomSheetBinding.placeCoverImage;
        String transformUrl = MediaManager.get().url()
                .transformation(new Transformation<>()
                        .width(800)
                        .crop("scale"))
                .generate(place.getCoverImagePublicId());
        Glide.with(this)
                .load(transformUrl)
                .error(R.drawable.brown)
                .into(placeCoverImage);
    }

    @SuppressLint("SetTextI18n")
    private void showLocationPinBottomSheet(LatLng latLng) {
        locationPinBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        placeInfoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        TextView placeLong = locationPinBottomSheetBinding.locationPinLocationLong;
        TextView placeLat = locationPinBottomSheetBinding.locationPinLocationLat;
        placeLong.setText("Kinh độ: " + latLng.getLongitude());
        placeLat.setText("Vĩ độ: " + latLng.getLatitude());
    }

    public void switchTab(int position) {
        if (position >= 0 && position < placeInfoBottomSheetBinding.tabLayout.getTabCount()) {
            TabLayout.Tab tab = placeInfoBottomSheetBinding.tabLayout.getTabAt(position);
            if (tab != null) {
                tab.select();
            }
        }
    }
}