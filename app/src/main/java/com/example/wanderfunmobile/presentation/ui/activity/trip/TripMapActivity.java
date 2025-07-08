package com.example.wanderfunmobile.presentation.ui.activity.trip;


import static org.maplibre.android.style.expressions.Expression.eq;
import static org.maplibre.android.style.expressions.Expression.get;
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
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.example.wanderfunmobile.R;
import com.example.wanderfunmobile.core.util.BitMapUtil;
import com.example.wanderfunmobile.core.util.ColorHexUtil;
import com.example.wanderfunmobile.core.util.GeoJsonUtil;
import com.example.wanderfunmobile.core.util.PolylineUtils;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.data.dto.goong.GoongTripDto;
import com.example.wanderfunmobile.data.dto.goong.GoongTripRequestDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.databinding.ActivityTripMapBinding;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;
import com.example.wanderfunmobile.presentation.viewmodel.GoongViewModel;
import com.example.wanderfunmobile.presentation.viewmodel.TripViewModel;
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
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Style;
import org.maplibre.android.plugins.annotation.SymbolManager;
import org.maplibre.android.plugins.annotation.SymbolOptions;
import org.maplibre.android.style.layers.FillLayer;
import org.maplibre.android.style.layers.LineLayer;
import org.maplibre.android.style.layers.Property;
import org.maplibre.android.style.layers.PropertyFactory;
import org.maplibre.android.style.sources.GeoJsonSource;
import org.maplibre.geojson.LineString;
import org.maplibre.geojson.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TripMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityTripMapBinding binding;
    private TripViewModel tripViewModel;
    private GoongViewModel goongViewModel;
    private MapLibreMap mapLibreMap;
    private Style mapStyle;
    private String mapStyleUrl;
    private long lastUpdateTime = 0;
    private Location currentLocation;
    private SymbolManager symbolManager;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;
    private List<TripPlace> tripPlaceList = new ArrayList<>();
    @Inject
    Gson gson;
    @Inject
    ObjectMapper objectMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapLibre.getInstance(this);

        EdgeToEdge.enable(this);
        binding = ActivityTripMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpRequestPermissionsLauncher();
        initViewModels();


        Long tripId = getIntent().getLongExtra("tripId", -1);
        if (tripId != -1) {
            tripViewModel.getTripById("Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(), tripId);
        } else {
            Toast.makeText(this, "Không tìm thấy chuyến đi!", Toast.LENGTH_SHORT).show();
            finish();
        }

        setUpMapView(savedInstanceState);
    }

    private void setUpRequestPermissionsLauncher() {
        requestPermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean isLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);

                    if (Boolean.FALSE.equals(isLocationGranted)) {
                        Toast.makeText(this, "Quyền truy cập bị từ chối!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void initViewModels() {
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        goongViewModel = new ViewModelProvider(this).get(GoongViewModel.class);

        tripViewModel.getTripByIdResponseLiveData().observe(this, response -> {
            if (response != null && !response.isError() && response.getData() != null) {
                tripPlaceList = objectMapper.mapList(response.getData().getTripPlaceList(), TripPlace.class);

                if (tripPlaceList.size() > 1) {
                    GoongTripRequestDto goongTripRequestDto = new GoongTripRequestDto();
                    goongTripRequestDto.setOrigin(tripPlaceList.get(0).getPlace().getLatitude() + "," + tripPlaceList.get(0).getPlace().getLongitude());
                    goongTripRequestDto.setDestination(tripPlaceList.get(tripPlaceList.size() - 1).getPlace().getLatitude() + "," + tripPlaceList.get(tripPlaceList.size() - 1).getPlace().getLongitude());

                    goongViewModel.getGoongTrip(
                            "Bearer " + SessionManager.getInstance(getApplicationContext()).getAccessToken(),
                            goongTripRequestDto,
                            getString(R.string.goong_api_key)
                    );
                }
            }
        });

        goongViewModel.getGetGoongTripResponseLiveData().observe(this, response -> {
            if (response != null && !response.isError() && response.getData() != null) {
                List<LatLng> latLngs;
                latLngs = PolylineUtils.decode(response.getData().getTrips().get(0).getGeometry());

                drawRouteOnMap(latLngs);
            } else {
                Toast.makeText(this, "Không thể lấy dữ liệu tuyến đường!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapLibreMap map) {
        map.setStyle(mapStyleUrl, style -> {
            // Load GeoJSON from file
            String geoJson = GeoJsonUtil.loadGeoJsonFromAsset(this, "Provinces.geojson");

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


            symbolManager = new SymbolManager(binding.mapView, map, style);
            symbolManager.setIconAllowOverlap(true);
            symbolManager.setIconIgnorePlacement(true);

            if (!tripPlaceList.isEmpty()) {
                addTripPlaceImageToMap(this, style, tripPlaceList);
                drawPlaceMarker(symbolManager, tripPlaceList);
            }

            if (PermissionsManager.areLocationPermissionsGranted(this)) {
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
                String title;
                Place place = null;
                if (symbol.getData() != null) {
                    title = symbol.getData().getAsJsonObject().get("title").getAsString();
                    if (title.equals("Place Marker") && symbol.getData().getAsJsonObject().get("place") != null) {
                        place = gson.fromJson(symbol.getData().getAsJsonObject().get("place"), Place.class);
                    }
                }
                return true;
            });

            mapStyle = style;
        });

        mapLibreMap = map;
    }

    private void setUpMapView(Bundle savedInstanceState) {
        binding.mapView.onCreate(savedInstanceState);
        mapStyleUrl = String.format("%s/assets/goong_map_web.json?api_key=%s",
                getString(R.string.goong_map_url),
                getString(R.string.goong_map_key));

        binding.mapView.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationComponent(@NonNull Style style, MapLibreMap mapLibreMap) {
        LocationComponent locationComponent = mapLibreMap.getLocationComponent();

        LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(this)
                .pulseEnabled(true)
                .pulseColor(ContextCompat.getColor(this, R.color.blue2))
                .foregroundTintColor(ContextCompat.getColor(this, R.color.black5))
                .build();

        LocationComponentActivationOptions locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, style)
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
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.e("LocationError", "Failed to get location updates", exception);
        }
    };

    private void addTripPlaceImageToMap(Context context, Style style, List<TripPlace> tripPlaceList) {
        for (TripPlace tripPlace : tripPlaceList) {
            String transformUrl = MediaManager.get().url()
                    .transformation(new Transformation<>()
                            .width(160)
                            .height(160)
                            .crop("thumb")
                            .radius("max")
                            .background("transparent")
                            .fetchFormat("png"))
                    .generate(tripPlace.getPlace().getCoverImage().getImagePublicId());
            BitMapUtil.getBitMapFromUrl(context, transformUrl, bitmap -> {
                style.addImage(tripPlace.getPlace().getCoverImage().getImagePublicId(), bitmap);
            });
        }
    }

    private void drawPlaceMarker(SymbolManager symbolManager, List<TripPlace> tripPlaceList) {
        for (TripPlace tripPlace : tripPlaceList) {
            addTripPlaceMarker(symbolManager, tripPlace);
        }
    }

    private void addTripPlaceMarker(SymbolManager symbolManager, TripPlace tripPlace) {
        JsonObject tripPlaceJson = gson.toJsonTree(tripPlace).getAsJsonObject();
        JsonObject data = new JsonObject();
        data.addProperty("title", "Trip Place Marker");
        data.add("tripPlace", tripPlaceJson);

        symbolManager.create(new SymbolOptions()
                .withLatLng(new LatLng(tripPlace.getPlace().getLatitude(), tripPlace.getPlace().getLongitude()))
                .withIconImage(tripPlace.getPlace().getCoverImage().getImagePublicId())
                .withData(data)
                .withTextField(tripPlace.getPlace().getName())
                .withTextFont(new String[]{"Roboto Medium"})
                .withTextSize(16f)
                .withTextColor(ColorHexUtil.getColorHexString(ContextCompat.getColor(this, R.color.black4)))
                .withTextHaloColor(ColorHexUtil.getColorHexString(ContextCompat.getColor(this, R.color.white1)))
                .withTextHaloWidth(5.0f)
                .withTextHaloBlur(0.0f)
                .withTextAnchor(Property.TEXT_ANCHOR_TOP_LEFT)
                .withTextOffset(new Float[]{0f, 2f})
        );
    }

    private void drawRouteOnMap(List<LatLng> latLngs) {
        List<Point> points = new ArrayList<>();
        for (LatLng latLng : latLngs) {
            points.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
        }

        LineString lineString = LineString.fromLngLats(points);
        GeoJsonSource routeSource = new GeoJsonSource("route-source", lineString);

        binding.mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                if (style.getSource("route-source") == null) {
                    style.addSource(routeSource);
                } else {
                    ((GeoJsonSource) Objects.requireNonNull(style.getSource("route-source"))).setGeoJson(lineString);
                }

                if (style.getLayer("route-layer") == null) {
                    LineLayer routeLayer = new LineLayer("route-layer", "route-source");
                    routeLayer.setProperties(
                            PropertyFactory.lineColor(Color.parseColor("#1DB954")),
                            PropertyFactory.lineWidth(6f),
                            PropertyFactory.lineCap(LINE_CAP_ROUND),
                            PropertyFactory.lineJoin(LINE_JOIN_ROUND)
                    );
                    style.addLayer(routeLayer);
                }
            });
        });
    }
}
