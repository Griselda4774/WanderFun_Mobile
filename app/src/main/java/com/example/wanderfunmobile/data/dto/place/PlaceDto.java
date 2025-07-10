package com.example.wanderfunmobile.data.dto.place;

import com.example.wanderfunmobile.data.dto.addresses.AddressDto;
import com.example.wanderfunmobile.data.dto.images.ImageDto;

import org.parceler.Parcel;

@Parcel
public class PlaceDto {
    private Long id;
    private double longitude;
    private double latitude;
    private AddressDto address;
    private String name;
    private PlaceCategoryDto category;
    private ImageDto coverImage;
    private int checkInPoint;
    private float checkInRangeMeter;
    private boolean verified;
    private boolean pending;
    private float rating;
    private int feedbackCount;
    private int checkInCount;
    private PlaceDetailDto placeDetail;

    public PlaceDto() {
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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlaceCategoryDto getCategory() {
        return category;
    }

    public void setCategory(PlaceCategoryDto category) {
        this.category = category;
    }

    public ImageDto getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(ImageDto coverImage) {
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

    public PlaceDetailDto getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(PlaceDetailDto placeDetail) {
        this.placeDetail = placeDetail;
    }
}

