package com.example.wanderfunmobile.domain.model.places;

import com.example.wanderfunmobile.domain.model.addresses.Address;
import com.example.wanderfunmobile.domain.model.images.Image;

public class Place {
    private Long id;
    private double longitude;
    private double latitude;
    private Address address;
    private String name;
    private PlaceCategory category;
    private Image coverImage;
    private int checkInPoint;
    private float checkInRangeMeter;
    private boolean verified;
    private boolean pending;
    private float rating;
    private int feedbackCount;
    private int checkInCount;
    private PlaceDetail placeDetail;

    public Place() {
    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlaceCategory getCategory() {
        return category;
    }

    public void setCategory(PlaceCategory category) {
        this.category = category;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getFeedbackCount() {
        return feedbackCount;
    }

    public void setFeedbackCount(int feedbackCount) {
        this.feedbackCount = feedbackCount;
    }

    public int getCheckInCount() {
        return checkInCount;
    }

    public void setCheckInCount(int checkInCount) {
        this.checkInCount = checkInCount;
    }

    public PlaceDetail getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(PlaceDetail placeDetail) {
        this.placeDetail = placeDetail;
    }
}
