package com.example.wanderfunmobile.data.dto.goong.trip;

import java.util.List;

public class GoongTripRespondDto {
    private String code;
    private List<GoongTripDto> trips;
    private List<GoongWayPointDto> waypoints;

    public GoongTripRespondDto() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<GoongTripDto> getTrips() {
        return trips;
    }

    public void setTrips(List<GoongTripDto> trips) {
        this.trips = trips;
    }

    public List<GoongWayPointDto> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<GoongWayPointDto> waypoints) {
        this.waypoints = waypoints;
    }
}
