package com.example.wanderfunmobile.domain.model.trips;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.wanderfunmobile.domain.model.places.Place;

import java.time.LocalDate;

public class TripPlace implements Parcelable {
    private Long id;
    private Place place;
    private Long tripId;
    private LocalDate startTime;
    private LocalDate endTime;
    private String placeNotes;

    public TripPlace() {};

    public TripPlace(Parcel in){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public String getPlaceNotes() {
        return placeNotes;
    }

    public void setPlaceNotes(String placeNotes) {
        this.placeNotes = placeNotes;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        // write data
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TripPlace> CREATOR = new Creator<TripPlace>() {
        @Override
        public TripPlace createFromParcel(Parcel in) {
            return new TripPlace(in);
        }

        @Override
        public TripPlace[] newArray(int size) {
            return new TripPlace[size];
        }
    };
}
