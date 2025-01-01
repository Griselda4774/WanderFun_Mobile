package com.example.wanderfunmobile.infrastructure.ui.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.databinding.FragmentHomeBinding;
import com.example.wanderfunmobile.infrastructure.util.BitMapUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.maplibre.android.maps.Style;
import org.maplibre.android.plugins.annotation.Symbol;
import org.maplibre.android.plugins.annotation.SymbolManager;
import org.maplibre.android.plugins.annotation.SymbolOptions;
import org.maplibre.android.style.expressions.Expression;
import org.maplibre.android.style.layers.Property;
import org.maplibre.android.style.layers.PropertyFactory;
import org.maplibre.android.style.layers.SymbolLayer;
import org.maplibre.android.style.sources.GeoJsonSource;

import java.util.Objects;

public class HomeFragment extends Fragment implements OnMapReadyCallback{
    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private Style mapStyle;
    private SymbolManager symbolManager;
    private Symbol currentMarker;

    private PermissionsManager permissionsManager;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;
    private LocationComponent locationComponent;
    private FragmentHomeBinding viewBinding;
    private String mapStyleUrl;

    private LinearLayout gpsButton;
    private Location currentLocation;

    private ConstraintLayout bottomSheet;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;

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

        mapView = viewBinding.mapView;
        gpsButton = viewBinding.gpsButton;

        bottomSheet = viewBinding.bottomSheetContainer.bottomSheet;
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

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
        mapView.onCreate(savedInstanceState);

        mapStyleUrl = String.format("%s/assets/goong_map_web.json?api_key=%s",
                getString(R.string.goong_map_url),
                getString(R.string.goong_map_key));
        mapView.getMapAsync(this);

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

            Bitmap icon1BitMap = BitMapUtil.convertVectorToBitmap(requireContext(),
                    R.drawable.ic_home, 100, 100);
            Bitmap icon2BitMap = BitMapUtil.convertVectorToBitmap(requireContext(),
                    R.drawable.ic_suitcase, 100, 100);
            Bitmap icon3BitMap = BitMapUtil.convertVectorToBitmap(requireContext(),
                    R.drawable.ic_trophy, 100, 100);
            style.addImage("icon1", icon1BitMap);
            style.addImage("icon2", icon2BitMap);
            style.addImage("icon3", icon3BitMap);

            GeoJsonSource geoJsonSource = null;
            try {
                geoJsonSource = new GeoJsonSource("marker-source", String.valueOf(createGeoJson()));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            style.addSource(geoJsonSource);

            SymbolLayer markerLayer = new SymbolLayer("marker-layer", "marker-source");
            markerLayer.setProperties(
                    PropertyFactory.iconImage(Expression.get("icon"))
//                        PropertyFactory.iconSize(Expression.switchCase(
//                                Expression.eq(Expression.get("selected"), true),
//                                Expression.literal(1.5f),
//                                Expression.literal(1.0f))
//                        PropertyFactory.textField(Expression.get("title")),
//                        PropertyFactory.textSize(12f),
//                        PropertyFactory.textAnchor(Property.TEXT_ANCHOR_TOP),
//                        PropertyFactory.textOffset(new Float[]{0f, 1.5f}))
            );
            markerLayer.setFilter(Expression.gte(Expression.zoom(), 12));
            style.addLayer(markerLayer);

            symbolManager.addClickListener(symbol -> {
                String title = "maker-non";
                if (symbol.getData() != null)
                    title = symbol.getData().getAsJsonObject().get("title").getAsString();
                Toast.makeText(requireContext(), title, Toast.LENGTH_SHORT).show();
                symbol.setIconImage("icon3");
                symbolManager.update(symbol);
                return true;
            });

            map.addOnMapClickListener(latLng -> {
                addMarkerAtLocation(latLng, map, symbolManager);
                return true;
            });

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
                        .locationEngineRequest(new LocationEngineRequest.Builder(750)
                                .setFastestInterval(750)
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
            currentLocation = result.getLastLocation();
            if (currentLocation != null) {
                Log.d("UserLocation", "Lat: " + currentLocation.getLatitude() + ", Lng: " + currentLocation.getLongitude());
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.e("LocationError", "Failed to get location updates", exception);
        }
    };

    private JSONObject createGeoJsonFeature(double lat, double lng, String title, String iconName) throws JSONException {
        JSONObject feature = new JSONObject();
        feature.put("type", "Feature");
        feature.put("properties", new JSONObject()
                .put("title", title)
                .put("icon", iconName));
        feature.put("geometry", new JSONObject()
                .put("type", "Point")
                .put("coordinates", new JSONArray().put(lng).put(lat)));
        return feature;
    }

    private JSONObject createGeoJson() throws JSONException {
        JSONArray features = new JSONArray();
        features.put(createGeoJsonFeature(10.7769, 106.7009, "Marker 1", "icon1"));
        features.put(createGeoJsonFeature(10.7809, 106.7039, "Marker 2", "icon2"));
        features.put(createGeoJsonFeature(10.7709, 106.6959, "Marker 3", "icon3"));

        JSONObject geoJson = new JSONObject();
        geoJson.put("type", "FeatureCollection");
        geoJson.put("features", features);
        return geoJson;
    }

    private void addMarkerAtLocation(LatLng latLng, MapLibreMap mapLibreMap, SymbolManager symbolManager) {
        if (currentMarker != null) {
            symbolManager.delete(currentMarker);
            currentMarker = null;
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            TextView placeLong = bottomSheet.findViewById(R.id.place_long);
            TextView placeLat = bottomSheet.findViewById(R.id.place_lat);
            placeLong.setText(String.valueOf(latLng.getLongitude()));
            placeLat.setText(String.valueOf(latLng.getLatitude()));

            JsonObject data = new JsonObject();
            data.addProperty("title", "My Marker");
            data.addProperty("description", "This is a sample marker description");
            currentMarker = symbolManager.create(new SymbolOptions()
                    .withLatLng(latLng)
                    .withIconImage("marker-icon")
                    .withData(data)
                    .withTextField("Mark")
                    .withTextFont(new String[] {"Roboto Medium"})
                    .withTextSize(16f)
                    .withTextColor(String.valueOf(R.color.black4))
                    .withTextAnchor(Property.TEXT_ANCHOR_LEFT)
                    .withTextOffset(new Float[]{0f, 2.5f})
            );

            double targetZoom = mapLibreMap.getCameraPosition().zoom > 12 ? mapLibreMap.getCameraPosition().zoom : 12;

            CameraPosition position = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(targetZoom)
                    .build();
            mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
        }
    }

    private void addMarker(LatLng latLng, MapLibreMap mapLibreMap, SymbolManager symbolManager) {
        symbolManager.create(new SymbolOptions()
                .withLatLng(latLng)
                .withIconImage("marker-icon")
        );
    }
}