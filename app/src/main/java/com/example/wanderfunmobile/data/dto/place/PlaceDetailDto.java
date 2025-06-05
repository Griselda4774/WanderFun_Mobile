package com.example.wanderfunmobile.data.dto.place;

import java.time.LocalTime;
import java.util.List;

public class PlaceDetailDto {
    private Long id;
    private String description;
    private int checkInPoint;
    private float checkInRangeMeter;
    private LocalTime timeOpen;
    private LocalTime timeClose;
    private boolean isClosed;
    private boolean isOpenAllDay;
    private String bestTimeToVisit;
    private int priceRangeTop;
    private int priceRangeBottom;
    private boolean isVerified;
    private String alternativeName;
    private String operator;
    private String url;
    private List<SectionDto> sectionList;

    public PlaceDetailDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCheckInPoint() {
        return checkInPoint;
    }

    public void setCheckInPoint(int checkInPoint) {
        this.checkInPoint = checkInPoint;
    }

    public float getCheckInRangeMeter() {
        return checkInRangeMeter;
    }

    public void setCheckInRangeMeter(float checkInRangeMeter) {
        this.checkInRangeMeter = checkInRangeMeter;
    }

    public LocalTime getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(LocalTime timeOpen) {
        this.timeOpen = timeOpen;
    }

    public LocalTime getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(LocalTime timeClose) {
        this.timeClose = timeClose;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public boolean isOpenAllDay() {
        return isOpenAllDay;
    }

    public void setOpenAllDay(boolean openAllDay) {
        isOpenAllDay = openAllDay;
    }

    public String getBestTimeToVisit() {
        return bestTimeToVisit;
    }

    public void setBestTimeToVisit(String bestTimeToVisit) {
        this.bestTimeToVisit = bestTimeToVisit;
    }

    public int getPriceRangeTop() {
        return priceRangeTop;
    }

    public void setPriceRangeTop(int priceRangeTop) {
        this.priceRangeTop = priceRangeTop;
    }

    public int getPriceRangeBottom() {
        return priceRangeBottom;
    }

    public void setPriceRangeBottom(int priceRangeBottom) {
        this.priceRangeBottom = priceRangeBottom;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<SectionDto> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<SectionDto> sectionList) {
        this.sectionList = sectionList;
    }
}
