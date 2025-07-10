package com.example.wanderfunmobile.data.dto.goong.trip;

import java.util.ArrayList;
import java.util.List;

public class GoongTripRequestDto {
    private String origin;
    private List<String> waypoints = new ArrayList<>();
    private String destination;
    private String vehicle = "car";

    public GoongTripRequestDto(){}

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<String> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<String> waypoints) {
        this.waypoints = waypoints;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}
