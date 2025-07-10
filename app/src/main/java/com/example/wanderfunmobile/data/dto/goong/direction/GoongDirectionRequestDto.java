package com.example.wanderfunmobile.data.dto.goong.direction;

import java.util.ArrayList;
import java.util.List;

public class GoongDirectionRequestDto {
    private String origin;
    private List<String> destination = new ArrayList<>();
    private String vehicle = "car";

    public GoongDirectionRequestDto() {}

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<String> getDestination() {
        return destination;
    }

    public void setDestination(List<String> destination) {
        this.destination = destination;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}
