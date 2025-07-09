package com.example.wanderfunmobile.data.dto.goong.trip;

import org.maplibre.android.geometry.LatLng;

import java.util.List;

public class GoongWayPointDto {
    private List<Double> location;
    public GoongWayPointDto() {}

    public List<Double> getLocation() {
        return location;
    }

    public LatLng toLatLng() {
        if (location != null && location.size() >= 2) {
            return new LatLng(location.get(1), location.get(0)); // [lng, lat] -> LatLng(lat, lng)
        }
        return null;
    }
}
