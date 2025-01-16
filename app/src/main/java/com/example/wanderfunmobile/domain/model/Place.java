package com.example.wanderfunmobile.domain.model;

import com.example.wanderfunmobile.domain.model.enums.PlaceCategory;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class Place {
    private Long id;
    private double longitude;
    private double latitude;
    private String address;
    private String name;
    private String iconUrl;
    private String iconPublicId;
    private String coverImageUrl;
    private String coverImagePublicId;
    private List<Section> description;
    private int checkInPoint;
    private float checkInRange;
    private int checkInCount;
    private float averageRating;
    private PlaceCategory category;
    private LocalTime timeOpen;
    private LocalTime timeClose;
    private String alternativeName;
    private String operator;
    private String link;
    private List<PlaceImage> placeImages;
    private List<Feedback> feedbacks;

    public Place() {};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconPublicId() {
        return iconPublicId;
    }

    public void setIconPublicId(String iconPublicId) {
        this.iconPublicId = iconPublicId;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverImagePublicId() {
        return coverImagePublicId;
    }

    public void setCoverImagePublicId(String coverImagePublicId) {
        this.coverImagePublicId = coverImagePublicId;
    }

    public List<Section> getDescription() {
        return description;
    }

    public void setDescription(List<Section> description) {
        this.description = description;
    }

    public int getCheckInPoint() {
        return checkInPoint;
    }

    public void setCheckInPoint(int checkInPoint) {
        this.checkInPoint = checkInPoint;
    }

    public float getCheckInRange() {
        return checkInRange;
    }

    public void setCheckInRange(float checkInRange) {
        this.checkInRange = checkInRange;
    }

    public int getCheckInCount() {
        return checkInCount;
    }

    public void setCheckInCount(int checkInCount) {
        this.checkInCount = checkInCount;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public PlaceCategory getCategory() {
        return category;
    }

    public void setCategory(PlaceCategory category) {
        this.category = category;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<PlaceImage> getPlaceImages() {
        return placeImages;
    }

    public void setPlaceImages(List<PlaceImage> placeImages) {
        this.placeImages = placeImages;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
