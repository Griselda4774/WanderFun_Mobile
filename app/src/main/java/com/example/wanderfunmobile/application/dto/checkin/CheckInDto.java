package com.example.wanderfunmobile.application.dto.checkin;

import java.util.Date;

public class CheckInDto {
    private Long id;
    private Long placeId;
    private int totalPoint;
    private int count;
    private Date lastCheckInTime;

    public CheckInDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastCheckInTime() {
        return lastCheckInTime;
    }

    public void setLastCheckInTime(Date lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
    }
}
