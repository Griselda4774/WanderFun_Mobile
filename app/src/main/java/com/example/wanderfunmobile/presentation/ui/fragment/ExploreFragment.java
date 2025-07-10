package com.example.wanderfunmobile.presentation.ui.fragment;


import static org.maplibre.android.style.expressions.Expression.eq;
import static org.maplibre.android.style.expressions.Expression.get;
import static org.maplibre.android.style.expressions.Expression.literal;
import static org.maplibre.android.style.layers.Property.LINE_CAP_ROUND;
import static org.maplibre.android.style.layers.Property.LINE_JOIN_ROUND;
import static org.maplibre.android.style.layers.PropertyFactory.fillColor;
import static org.maplibre.android.style.layers.PropertyFactory.fillOpacity;
import static org.maplibre.android.style.layers.PropertyFactory.lineCap;
import static org.maplibre.android.style.layers.PropertyFactory.lineColor;
import static org.maplibre.android.style.layers.PropertyFactory.lineJoin;
import static org.maplibre.android.style.layers.PropertyFactory.lineWidth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.DateTimeUtil;
import com.example.wanderfunmobile.core.util.FavoritePlaceManager;
import com.example.wanderfunmobile.core.util.GeoJsonUtil;
import com.example.wanderfunmobile.core.util.NumberUtil;
import com.example.wanderfunmobile.core.util.StringUtil;
import com.example.wanderfunmobile.core.util.ViewPager2HeightAdjuster;
import com.example.wanderfunmobile.databinding.BottomSheetLocationPinBinding;
import com.example.wanderfunmobile.databinding.BottomSheetPlaceInfoBinding;
import com.example.wanderfunmobile.databinding.FragmentExploreBinding;
import com.example.wanderfunmobile.domain.model.addresses.Province;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.presentation.ui.adapter.place.PlaceInfoTabAdapter;
import com.example.wanderfunmobile.presentation.ui.adapter.searchs.SearchPlaceItemAdapter;
import com.example.wanderfunmobile.presentation.ui.adapter.searchs.SearchProvinceItemAdapter;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.CheckInDialog;
import com.example.wanderfunmobile.presentation.ui.custom.dialog.LoadingDialog;
import com.example.wanderfunmobile.core.util.BitMapUtil;
import com.example.wanderfunmobile.core.util.ColorHexUtil;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.presentation.ui.custom.starrating.StarRatingView;
import com.example.wanderfunmobile.presentation.viewmodel.AutoCompleteViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.CheckInViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.FavoritePlaceViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.places.PlaceViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Projection;
import org.maplibre.android.maps.Style;
import org.maplibre.android.plugins.annotation.Symbol;
import org.maplibre.android.plugins.annotation.SymbolManager;
import org.maplibre.android.plugins.annotation.SymbolOptions;
import org.maplibre.android.style.expressions.Expression;
import org.maplibre.android.style.layers.FillLayer;
import org.maplibre.android.style.layers.LineLayer;
import org.maplibre.android.style.layers.Property;
import org.maplibre.android.style.sources.GeoJsonSource;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.Geometry;
import org.maplibre.geojson.MultiPolygon;
import org.maplibre.geojson.Point;
import org.maplibre.geojson.Polygon;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExploreFragment extends Fragment implements OnMapReadyCallback {
    private String selectedProvinceName = null;
    private PlaceViewModel placeViewModel;
    private CheckInViewModel checkInViewModel;
    private MapLibreMap mapLibreMap;
    private Style mapStyle;
    private SymbolManager symbolManager;
    private Symbol currentMarker;

    private PermissionsManager permissionsManager;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;
    private LocationComponent locationComponent;
    private long lastUpdateTime = 0;
    private FragmentExploreBinding viewBinding;
    private String mapStyleUrl;
    private Location currentLocation;

    private BottomSheetPlaceInfoBinding placeInfoBottomSheetBinding;
    private BottomSheetLocationPinBinding locationPinBottomSheetBinding;

    private BottomSheetBehavior<ConstraintLayout> placeInfoBottomSheetBehavior;
    private BottomSheetBehavior<ConstraintLayout> locationPinBottomSheetBehavior;
    private List<Place> placeList = new ArrayList<>();
    private Place currentPlace = null;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    Gson gson;
    private LoadingDialog loadingDialog;
    private boolean canCheckIn;
    private Long currentCheckInPlaceId;
    private FavoritePlaceViewModel favoritePlaceViewModel;
    private final List<Province> searchProvinceList = new ArrayList<>();
    private SearchProvinceItemAdapter searchProvinceItemAdapter;
    private final List<Place> searchPlaceList = new ArrayList<>();
    private SearchPlaceItemAdapter searchPlaceItemAdapter;
    private AutoCompleteViewModel autoCompleteViewModel;

    public ExploreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init maplibre
        MapLibre.getInstance(requireContext());

        setUpRequestPermissionsLauncher();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentExploreBinding.inflate(inflater, container, false);

        initViewModel();

        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpViewModel();

        setUpAdapter();

        setUpFragment(savedInstanceState);
    }

    @Override
    public void onMapReady(@NonNull MapLibreMap map) {
        map.setStyle(mapStyleUrl, style -> {
            // Load GeoJSON from file
            String geoJson = GeoJsonUtil.loadGeoJsonFromAsset(requireContext(), "Provinces.geojson");

            // Create Source form GeoJSON
            GeoJsonSource source = new GeoJsonSource("province-source", geoJson);
            style.addSource(source);

            // Create Layer to show province boundaries
            FillLayer layer = new FillLayer("province-layer", "province-source");
            layer.setProperties(
                    fillColor(Color.parseColor("#3bb2d0")),
                    fillOpacity(0.0f)
            );

            // Add the layer to the map
            style.addLayer(layer);

            LineLayer borderLayer = new LineLayer("province-borders", "province-source");
            borderLayer.setProperties(
                    lineColor(Color.BLACK),
                    lineWidth(2.0f),
                    lineJoin(LINE_JOIN_ROUND),
                    lineCap(LINE_CAP_ROUND)
            );

            // Thêm layer sau khi đã thêm layer tô nền
            style.addLayerAbove(borderLayer, "province-layer");

            FillLayer provinceSelectedLayer = new FillLayer("province-selected", "province-source");
            provinceSelectedLayer.setProperties(
                    fillColor(Color.parseColor("#ff9800")),
                    fillOpacity(0.2f)
            );
            provinceSelectedLayer.setFilter(eq(get("Name"), ""));

            style.addLayerAbove(provinceSelectedLayer, "province-layer");



            symbolManager = new SymbolManager(viewBinding.mapView, map, style);
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

                focusOnLocation(symbol.getLatLng(), map, 12, -200);

                String title;
                Place place = null;
                if (symbol.getData() != null) {
                    title = symbol.getData().getAsJsonObject().get("title").getAsString();
                    if (title.equals("Place Marker") && symbol.getData().getAsJsonObject().get("place") != null) {
                        place = gson.fromJson(symbol.getData().getAsJsonObject().get("place"), Place.class);
                    }
                }

                if (place != null) {
                    placeViewModel.findPlaceById(place.getId());
                }
                return true;
            });

            map.addOnMapClickListener(latLng -> {
//                addMarkerAtLocation(latLng, map, symbolManager);

                PointF screenPoint = map.getProjection().toScreenLocation(latLng);

                List<Feature> features = map.queryRenderedFeatures(screenPoint, "province-layer");

                if (!features.isEmpty()) {
                    Feature feature = features.get(0);

                    Geometry geometry = feature.geometry();
                    if (geometry instanceof Polygon) {
                        Point centroid = getCentroidOfPolygon((Polygon) geometry);
                        LatLng centerLatLng = new LatLng(centroid.latitude(), centroid.longitude());
                        focusOnLocation(centerLatLng, map, 8.0f);
                    } else if (geometry instanceof MultiPolygon) {
                        List<Polygon> polygons = ((MultiPolygon) geometry).polygons();
                        if (!polygons.isEmpty()) {
                            Point centroid = getCentroidOfPolygon(polygons.get(0));
                            LatLng centerLatLng = new LatLng(centroid.latitude(), centroid.longitude());
                            focusOnLocation(centerLatLng, map, 8.0f);
                        }
                    }

                    String clickedProvinceName = feature.getStringProperty("Name");

                    if (clickedProvinceName.equals(selectedProvinceName)) {
                        selectedProvinceName = null;
                    } else {
                        selectedProvinceName = clickedProvinceName;
                    }

                    FillLayer selectedLayer = style.getLayerAs("province-selected");
                    if (selectedLayer != null) {
                        if (selectedProvinceName != null) {
                            selectedLayer.setFilter(eq(get("Name"), literal(selectedProvinceName)));
                            placeViewModel.findAllPlacesByProvinceName(selectedProvinceName);
                        } else {
                            removePlaceImagesFromMap(style, placeList);
                            removePlaceMarkers(symbolManager);
                            selectedLayer.setFilter(eq(get("Name"), literal("")));
                        }
                    }
                }

                return true;
            });

            mapStyle = style;
        });

        mapLibreMap = map;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewBinding.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewBinding.mapView.onResume();
        //        if (currentPlace != null) {
//            placeViewModel.getPlaceById(currentPlace.getId());
//        } else {
//            placeViewModel.getAllPlaces();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        viewBinding.mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewBinding.mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        viewBinding.mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding.mapView.onDestroy();
        viewBinding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewBinding.mapView.onSaveInstanceState(outState);
    }

    private void setUpRequestPermissionsLauncher() {
        requestPermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean isLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);

                    if (Boolean.FALSE.equals(isLocationGranted)) {
                        Toast.makeText(requireContext(), "Quyền truy cập bị từ chối!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void initViewModel() {
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        checkInViewModel = new ViewModelProvider(this).get(CheckInViewModel.class);
        favoritePlaceViewModel = new ViewModelProvider(this).get(FavoritePlaceViewModel.class);
        autoCompleteViewModel = new ViewModelProvider(this).get(AutoCompleteViewModel.class);
    }

    private void setUpFragment(Bundle savedInstanceState) {
        setUpBottomSheet();
        setUpDialog();
        setUpMapView(savedInstanceState);
        setUpButton();
        setUpSearchBar();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpViewModel() {
        placeViewModel.getFindAllPlacesResponseLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result != null && !result.isError()) {
                placeList = result.getData();
            }
        });

        placeViewModel.getFindAllPlacesByProvinceNameResponseLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result != null && !result.isError()) {
                removePlaceImagesFromMap(mapStyle, placeList);
                placeList = objectMapper.mapList(result.getData(), Place.class);
            }
            removePlaceMarkers(symbolManager);
            addPlaceImageToMap(requireContext(), mapStyle, placeList);
            drawPlaceMarker(symbolManager, placeList);
        });

        placeViewModel.getFindPlaceByIdResponseLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!result.isError() && result.getData() != null) {
                showPlaceInfoBottomSheet(result.getData());
            }
        });

        checkInViewModel.getCreateCheckInLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (!result.isError()) {
                    Toast.makeText(requireContext(), "Check-in thành công!", Toast.LENGTH_SHORT).show();
                    currentCheckInPlaceId = null;
                } else {
                    Toast.makeText(requireContext(), "Check-in thất bại: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Check-in thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        favoritePlaceViewModel.getCreateFavoritePlaceLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!result.isError() && result.getData() != null) {
                Place place = result.getData();
                FavoritePlaceManager.getInstance(requireActivity().getApplicationContext()).add(place);
                boolean isFavorite = FavoritePlaceManager.getInstance(requireActivity().getApplicationContext()).isFavorite(place);
                updatePlaceMarkerIcon(symbolManager, place, isFavorite);
                placeInfoBottomSheetBinding.favoriteEnabled.setVisibility(View.VISIBLE);
                placeInfoBottomSheetBinding.favoriteDisabled.setVisibility(View.GONE);
                Toast.makeText(requireActivity(), "Đã thêm vào yêu thích!", Toast.LENGTH_SHORT).show();
            } else {
                placeInfoBottomSheetBinding.favoriteEnabled.setVisibility(View.GONE);
                placeInfoBottomSheetBinding.favoriteDisabled.setVisibility(View.VISIBLE);
                Toast.makeText(requireActivity(), "Thêm yêu thích thất bại!" , Toast.LENGTH_SHORT).show();
            }
        });

        favoritePlaceViewModel.getDeleteByUserAndPlaceIdLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!result.isError() && result.getData() != null) {
                Place place = result.getData();
                FavoritePlaceManager.getInstance(requireActivity().getApplicationContext()).remove(place);
                boolean isFavorite = FavoritePlaceManager.getInstance(requireActivity().getApplicationContext()).isFavorite(place);
                updatePlaceMarkerIcon(symbolManager, place, isFavorite);
                placeInfoBottomSheetBinding.favoriteEnabled.setVisibility(View.GONE);
                placeInfoBottomSheetBinding.favoriteDisabled.setVisibility(View.VISIBLE);
                Toast.makeText(requireActivity(), "Đã xóa khỏi yêu thích!", Toast.LENGTH_SHORT).show();
            } else {
                placeInfoBottomSheetBinding.favoriteEnabled.setVisibility(View.VISIBLE);
                placeInfoBottomSheetBinding.favoriteDisabled.setVisibility(View.GONE);
                Toast.makeText(requireActivity(), "Xóa yêu thích thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteViewModel.getAutoCompleteSearchPlaceLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result != null && !result.isError()) {
                if (!result.getData().isEmpty()) {
                    searchPlaceList.clear();
                    searchPlaceList.addAll(result.getData());
                    searchPlaceItemAdapter.notifyDataSetChanged();
                    viewBinding.searchPlaceTitle.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.searchPlaceTitle.setVisibility(View.GONE);
                    searchPlaceList.clear();
                    searchPlaceItemAdapter.notifyDataSetChanged();
                }
            }
        });

        autoCompleteViewModel.getAutoCompleteSearchProvinceLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result != null && !result.isError()) {
                if (!result.getData().isEmpty()) {
                    searchProvinceList.clear();
                    searchProvinceList.addAll(result.getData());
                    searchProvinceItemAdapter.notifyDataSetChanged();
                    viewBinding.searchProvinceTitle.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.searchProvinceTitle.setVisibility(View.GONE);
                    searchProvinceList.clear();
                    searchProvinceItemAdapter.notifyDataSetChanged();
                }
                viewBinding.searchResultContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpAdapter() {
        viewBinding.searchProvinceList.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchProvinceItemAdapter = new SearchProvinceItemAdapter(searchProvinceList);
        viewBinding.searchProvinceList.setAdapter(searchProvinceItemAdapter);
        searchProvinceItemAdapter.setOnSearchProvinceItemClickListener(province -> {
            if (province != null) {
                selectedProvinceName = province.getName();
                viewBinding.searchInput.setText(selectedProvinceName);
                viewBinding.searchInput.clearFocus();
                viewBinding.searchResultContainer.setVisibility(View.GONE);
                placeViewModel.findAllPlacesByProvinceName(selectedProvinceName);
            }
        });

        viewBinding.searchPlaceList.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchPlaceItemAdapter = new SearchPlaceItemAdapter(searchPlaceList);
        viewBinding.searchPlaceList.setAdapter(searchPlaceItemAdapter);
        searchPlaceItemAdapter.setOnSearchPlaceItemClickListener(place -> {
            if (place != null) {
                viewBinding.searchInput.setText(place.getName());
                viewBinding.searchInput.clearFocus();
                viewBinding.searchResultContainer.setVisibility(View.GONE);
                focusOnLocation(new LatLng(place.getLatitude(), place.getLongitude()), mapLibreMap, 12, -200);
                placeViewModel.findPlaceById(place.getId());
            }
        });
    }

    private void setUpBottomSheet() {
        // Place info bottom sheet
        placeInfoBottomSheetBinding = viewBinding.placeInfoBottomSheetContainer;
        placeInfoBottomSheetBehavior = BottomSheetBehavior.from(placeInfoBottomSheetBinding.getRoot());
        placeInfoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
        locationPinBottomSheetBinding = viewBinding.locationPinBottomSheetContainer;
        locationPinBottomSheetBehavior = BottomSheetBehavior.from(locationPinBottomSheetBinding.getRoot());
        locationPinBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
    }

    private void setUpDialog() {
        // Selection dialog
        viewBinding.selectionDialog.setOnAcceptListener(() -> {
            if (currentCheckInPlaceId != null) {
//                placeViewModel.findCheckInByPlaceId("Bearer " + SessionManager.getInstance(requireActivity().getApplicationContext()).getAccessToken(), currentCheckInPlaceId);
            }
            viewBinding.selectionDialog.hide();
            Log.d("SelectionDialog", "Accept");
        });

        viewBinding.selectionDialog.setOnRejectListener(() -> {
            viewBinding.selectionDialog.hide();
            Log.d("SelectionDialog", "Reject");
        });
    }

    private void setUpMapView(Bundle savedInstanceState) {
        viewBinding.mapView.onCreate(savedInstanceState);
        mapStyleUrl = String.format("%s/assets/goong_map_web.json?api_key=%s",
                getString(R.string.goong_map_url),
                getString(R.string.goong_map_key));

        viewBinding.mapView.getMapAsync(this);
    }

    private void setUpButton() {
        viewBinding.gpsButton.setOnClickListener(v -> {
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

        viewBinding.checkInButton.setOnClickListener(v -> {
            if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
                if (currentMarker != null) {
                    symbolManager.delete(currentMarker);
                    currentMarker = null;
                }

                initializeLocationComponent(mapStyle, mapLibreMap);

                CheckInDialog checkInDialog = new CheckInDialog(requireContext(), getViewLifecycleOwner(),
                        checkInViewModel, currentLocation.getLongitude(), currentLocation.getLatitude());
                checkInDialog.setOnPlaceSelectedListener(place -> {
                    currentCheckInPlaceId = place.getId();
                    checkInViewModel.createCheckIn("Bearer " + SessionManager.getInstance(requireActivity().getApplicationContext()).getAccessToken(), currentCheckInPlaceId);
                });
                checkInDialog.show();

            } else {
                checkPermissions();
            }
        });
    }

    private void setUpSearchBar() {
        viewBinding.searchResultContainer.setVisibility(View.GONE);

        viewBinding.searchInput.setDebouncedSearchListener(query -> {
            autoCompleteViewModel.autoCompleteSearchProvince(query);
            autoCompleteViewModel.autoCompleteSearchPlace(query);
        });
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
            Log.d("UserLocation", "Lat: " + currentLocation.getLatitude() + ", Lng: " + currentLocation.getLongitude());
//            if (currentLocation != null && !placeList.isEmpty()) {
//                for (Place place : placeList) {
//                    float[] results = new float[1];
//                    Location.distanceBetween(
//                            currentLocation.getLatitude(), currentLocation.getLongitude(),
//                            place.getLatitude(), place.getLongitude(),
//                            results
//                    );
//                    float distanceInMeters = results[0];
//                    if (distanceInMeters <= 20) {
//                        Log.d("UserLocation", "Place can check-in: " + place.getName());
//                        currentCheckInPlaceId = place.getId();
//                        viewBinding.selectionDialog.show("Có thể check-in tại đây",
//                                "Địa điểm được tìm thấy là: " + place.getName(),
//                                "Bạn có muốn check-in tại đây?", "Có", "Không");
//                    }
//                }
//
//            }
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
                    .generate(place.getCoverImage().getImagePublicId());
            BitMapUtil.getBitMapFromUrl(context, transformUrl, bitmap -> {
                int borderColor = ContextCompat.getColor(context, R.color.red3);
                Bitmap bitmapWithBorder = BitMapUtil.addCircularBorder(bitmap, 20, borderColor);
                style.addImage(place.getCoverImage().getImagePublicId() + "fav_disabled", bitmap);
                style.addImage(place.getCoverImage().getImagePublicId() + "fav_enabled", bitmapWithBorder);
            });
        }
    }

    private void drawPlaceMarker(SymbolManager symbolManager, List<Place> placeList) {
        for (Place place : placeList) {
            addPlaceMarker(symbolManager, place);
        }
    }

    private void addPlaceMarker(SymbolManager symbolManager, Place place) {
        JsonObject placeJson = gson.toJsonTree(place).getAsJsonObject();
        JsonObject data = new JsonObject();
        data.addProperty("title", "Place Marker");
        data.add("place", placeJson);

        boolean isFavorite = FavoritePlaceManager.getInstance(requireActivity().getApplicationContext()).isFavorite(place);

        symbolManager.create(new SymbolOptions()
                .withLatLng(new LatLng(place.getLatitude(), place.getLongitude()))
                .withIconImage(isFavorite ? place.getCoverImage().getImagePublicId() + "fav_enabled" : place.getCoverImage().getImagePublicId() + "fav_disabled")
                .withData(data)
                .withTextField(place.getName())
                .withTextFont(new String[]{"Roboto Medium"})
                .withTextSize(16f)
                .withTextColor(ColorHexUtil.getColorHexString(ContextCompat.getColor(requireContext(), R.color.black4)))
                .withTextHaloColor(ColorHexUtil.getColorHexString(ContextCompat.getColor(requireContext(), R.color.white1)))
                .withTextHaloWidth(5.0f)
                .withTextHaloBlur(1.0f)
                .withTextAnchor(Property.TEXT_ANCHOR_TOP_LEFT)
                .withTextOffset(new Float[]{0f, 2.4f})
        );
    }

    private void removePlaceImagesFromMap(Style style, List<Place> placeList) {
        for (Place place : placeList) {
            String imageId1 = place.getCoverImage().getImagePublicId() + "fav_disabled";
            String imageId2 = place.getCoverImage().getImagePublicId() + "fav_enabled";
            if (style.getImage(imageId1) != null) {
                style.removeImage(imageId1);
            }
            if (style.getImage(imageId2) != null) {
                style.removeImage(imageId2);
            }
        }
    }

    private void updatePlaceMarkerIcon(SymbolManager symbolManager, Place place, boolean isFavorite) {
        LongSparseArray<Symbol> symbolArray = symbolManager.getAnnotations();

        String imageId = place.getCoverImage().getImagePublicId() +
                (isFavorite ? "fav_enabled" : "fav_disabled");

        for (int i = 0; i < symbolArray.size(); i++) {
            Symbol symbol = symbolArray.valueAt(i);
            JsonElement dataElement = symbol.getData();
            if (dataElement != null && dataElement.isJsonObject()) {
                JsonObject data = dataElement.getAsJsonObject();
                if (data.has("place") && data.get("place").isJsonObject()) {
                    JsonObject placeJson = data.get("place").getAsJsonObject();
                    if (placeJson.has("id") && placeJson.get("id").getAsString().equals(place.getId())) {
                        symbol.setIconImage(imageId);
                        symbolManager.delete(symbol);
                        addPlaceMarker(symbolManager, place);
                        break;
                    }
                }
            }
        }
    }

    private void removePlaceMarkers(SymbolManager symbolManager) {
        List<Symbol> symbolsToRemove = new ArrayList<>();
        LongSparseArray<Symbol> allSymbols = symbolManager.getAnnotations();

        for (int i = 0; i < allSymbols.size(); i++) {
            Symbol symbol = allSymbols.valueAt(i);
            if (symbol.getData() != null &&
                    symbol.getData().isJsonObject() &&
                    symbol.getData().getAsJsonObject().has("title") &&
                    "Place Marker".equals(symbol.getData().getAsJsonObject().get("title").getAsString())) {
                symbolsToRemove.add(symbol);
            }
        }

        symbolManager.delete(symbolsToRemove);
    }

    @SuppressLint("SetTextI18n")
    private void showPlaceInfoBottomSheet(Place place) {
        placeInfoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        locationPinBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Place info tab
        // View pager
        ViewPager2 viewPager = placeInfoBottomSheetBinding.viewPager;
        PlaceInfoTabAdapter placeInfoTabAdapter = new PlaceInfoTabAdapter(requireActivity(), gson.toJson(place),viewPager);
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
                    tabText.setText("Giới thiệu");
                    tabText.setTextAppearance(R.style.Text_TabLabel_Active_Blue);
                    break;
                case 1:
                    tabText.setText("Đánh giá");
                    break;
                case 2:
                    tabText.setText("Ảnh");
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

        boolean isFavorite = FavoritePlaceManager.getInstance(requireContext()).isFavorite(place);

        placeInfoBottomSheetBinding.favoriteEnabled.setVisibility(isFavorite ? View.VISIBLE : View.GONE);
        placeInfoBottomSheetBinding.favoriteDisabled.setVisibility(isFavorite ? View.GONE : View.VISIBLE);

        placeInfoBottomSheetBinding.favoriteDisabled.setOnClickListener(v -> {
            favoritePlaceViewModel.createFavoritePlace("Bearer " + SessionManager.getInstance(requireContext()).getAccessToken(), place.getId());
        });

        placeInfoBottomSheetBinding.favoriteEnabled.setOnClickListener(v -> {
            favoritePlaceViewModel.deleteByUserAndPlaceId("Bearer " + SessionManager.getInstance(requireContext()).getAccessToken(), place.getId());
        });

        TextView placeName = placeInfoBottomSheetBinding.placeName;
        placeName.setText(place.getName());

        // Address
        if (place.getAddress() != null) {
            placeInfoBottomSheetBinding.placeAddressContent.setText(StringUtil.formatAddressToString(place.getAddress()));
        } else {
            placeInfoBottomSheetBinding.placeAddressContent.setText("Chưa có địa chỉ");
        }

        StarRatingView starRatingView = placeInfoBottomSheetBinding.placeRatingView;
        int roundedRating = (int) Math.floor(place.getRating());
        starRatingView.setRating(roundedRating);

        TextView placeRating = placeInfoBottomSheetBinding.placeRatingScore;
        placeRating.setText(String.valueOf(place.getRating()));

        TextView placeRatingCount = placeInfoBottomSheetBinding.placeRatingCount;
        placeRatingCount.setText("(" + NumberUtil.formatNumberWithCommas(place.getFeedbackCount()) + ")");

        ConstraintLayout placeTimeOpening = placeInfoBottomSheetBinding.placeTimeOpening;
        TextView placeTimeOpeningTimeClose = placeInfoBottomSheetBinding.placeTimeOpeningTimeClose;
        ConstraintLayout placeTimeClosing = placeInfoBottomSheetBinding.placeTimeClosing;
        TextView placeTimeClosingTimeOpen = placeInfoBottomSheetBinding.placeTimeClosingTimeOpen;
        if (place.getPlaceDetail().isClosed()) {
            placeTimeOpening.setVisibility(View.GONE);
            placeTimeClosing.setVisibility(View.GONE);
            placeInfoBottomSheetBinding.openAllDay.setVisibility(View.GONE);
            placeInfoBottomSheetBinding.closing.setVisibility(View.VISIBLE);
        } else {
            if (place.getPlaceDetail().isOpenAllDay()) {
                placeTimeOpening.setVisibility(View.GONE);
                placeTimeClosing.setVisibility(View.GONE);
                placeInfoBottomSheetBinding.openAllDay.setVisibility(View.VISIBLE);
                placeInfoBottomSheetBinding.closing.setVisibility(View.GONE);
            } else {
                if (place.getPlaceDetail().getTimeOpen() != null && place.getPlaceDetail().getTimeClose() != null) {
                    LocalTime currentTime = LocalTime.now();
                    if (place.getPlaceDetail().getTimeClose().isBefore(currentTime) || place.getPlaceDetail().getTimeOpen().isAfter(currentTime)) {
                        placeTimeClosing.setVisibility(View.VISIBLE);
                        placeTimeOpening.setVisibility(View.GONE);
                        placeInfoBottomSheetBinding.openAllDay.setVisibility(View.GONE);
                        placeInfoBottomSheetBinding.closing.setVisibility(View.GONE);
                        placeTimeClosingTimeOpen.setText(place.getPlaceDetail().getTimeOpen().toString());
                    } else {
                        placeTimeOpening.setVisibility(View.VISIBLE);
                        placeTimeClosing.setVisibility(View.GONE);
                        placeInfoBottomSheetBinding.openAllDay.setVisibility(View.GONE);
                        placeInfoBottomSheetBinding.closing.setVisibility(View.GONE);
                        placeTimeOpeningTimeClose.setText(DateTimeUtil.localTimeToString(place.getPlaceDetail().getTimeClose()));
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
                .generate(place.getCoverImage().getImagePublicId());
        Glide.with(this)
                .load(transformUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
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

    private Point getCentroidOfPolygon(Polygon polygon) {
        List<Point> coordinates = polygon.coordinates().get(0);
        double latitude = 0;
        double longitude = 0;

        for (Point point : coordinates) {
            latitude += point.latitude();
            longitude += point.longitude();
        }

        int total = coordinates.size();
        return Point.fromLngLat(longitude / total, latitude / total);
    }

    private void clickToProvince(String provinceName, Feature feature) {
        if (provinceName == null || feature == null) return;

        Geometry geometry = feature.geometry();
        if (geometry == null) return;

        Point centroid = null;
        if (geometry instanceof Polygon) {
            centroid = getCentroidOfPolygon((Polygon) geometry);
        } else if (geometry instanceof MultiPolygon) {
            List<Polygon> polygons = ((MultiPolygon) geometry).polygons();
            if (!polygons.isEmpty()) {
                centroid = getCentroidOfPolygon(polygons.get(0));
            }
        }

        if (centroid != null) {
            LatLng centerLatLng = new LatLng(centroid.latitude(), centroid.longitude());
            focusOnLocation(centerLatLng, mapLibreMap, 8.0f);
        }

        FillLayer selectedLayer = mapStyle.getLayerAs("province-selected");
        if (selectedLayer == null) return;

        if (provinceName.equals(selectedProvinceName)) {
            selectedProvinceName = null;
            selectedLayer.setFilter(eq(get("Name"), literal("")));
            removePlaceImagesFromMap(mapStyle, placeList);
            removePlaceMarkers(symbolManager);
        } else {
            selectedProvinceName = provinceName;
            selectedLayer.setFilter(eq(get("Name"), literal(provinceName)));
            placeViewModel.findAllPlacesByProvinceName(provinceName);
        }
    }

    private void simulateMapClickByProvinceName(String provinceName) {
        if (mapStyle == null || provinceName == null) return;

        GeoJsonSource source = mapStyle.getSourceAs("province-source");
        if (source == null) return;

        List<Feature> features = source.querySourceFeatures(
                eq(get("Name"), literal(provinceName))
        );

        if (features.isEmpty()) return;

        Feature feature = features.get(0);
        clickToProvince(provinceName, feature);
    }

}