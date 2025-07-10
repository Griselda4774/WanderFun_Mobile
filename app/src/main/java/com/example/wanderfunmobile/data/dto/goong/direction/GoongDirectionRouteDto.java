package com.example.wanderfunmobile.data.dto.goong.direction;

import java.util.List;

public class GoongDirectionRouteDto {
    private List<GoongDirectionLegDto> legs;
    private GoongDirectionPolyline overview_polyline;

    public GoongDirectionRouteDto() {}

    public List<GoongDirectionLegDto> getLegs() {
        return legs;
    }

    public void setLegs(List<GoongDirectionLegDto> legs) {
        this.legs = legs;
    }

    public GoongDirectionPolyline getOverviewPolyline() {
        return overview_polyline;
    }

    public void setOverviewPolyline(GoongDirectionPolyline overviewPolyline) {
        this.overview_polyline = overviewPolyline;
    }
}
