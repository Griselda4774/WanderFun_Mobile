package com.example.wanderfunmobile.data.dto.goong;

import java.util.List;

public class GoongTripRespondDto {
    private String code;
    private GoongTripDto trip;
    private List<GoongWayPointDto> waypoints;

    public GoongTripRespondDto() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public GoongTripDto getTrip() {
        return trip;
    }

    public void setTrip(GoongTripDto trip) {
        this.trip = trip;
    }

    public List<GoongWayPointDto> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<GoongWayPointDto> waypoints) {
        this.waypoints = waypoints;
    }
}
