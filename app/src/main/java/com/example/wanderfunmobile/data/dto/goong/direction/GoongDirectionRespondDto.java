package com.example.wanderfunmobile.data.dto.goong.direction;

import com.example.wanderfunmobile.data.dto.goong.trip.GoongTripDto;
import com.example.wanderfunmobile.data.dto.goong.trip.GoongWayPointDto;

import java.util.List;

public class GoongDirectionRespondDto {
    private List<GoongDirectionRouteDto> routes;

    public GoongDirectionRespondDto() {}

    public List<GoongDirectionRouteDto> getRoutes() {
        return routes;
    }

    public void setRoutes(List<GoongDirectionRouteDto> routes) {
        this.routes = routes;
    }
}
