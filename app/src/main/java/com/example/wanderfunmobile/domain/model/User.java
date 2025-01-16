package com.example.wanderfunmobile.domain.model;

import com.example.wanderfunmobile.domain.model.enums.UserRole;

import java.util.Date;
import java.util.List;

public class User {
    private Long id;
    private UserRole role;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isVerified;
    private String avatarUrl;
    private String avatarPublicId;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private int point;
    private int rank;
    private boolean isCreatedProfile;
    private List<FavouritePlace> favouritePlaces;
    private List<Trip> trips;
    private List<Album> albums;
    private List<CheckIn> checkIns;

    public User() {};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarPublicId() {
        return avatarPublicId;
    }

    public void setAvatarPublicId(String avatarPublicId) {
        this.avatarPublicId = avatarPublicId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isCreatedProfile() {
        return isCreatedProfile;
    }

    public void setCreatedProfile(boolean createdProfile) {
        isCreatedProfile = createdProfile;
    }

    public List<FavouritePlace> getFavouritePlaces() {
        return favouritePlaces;
    }

    public void setFavouritePlaces(List<FavouritePlace> favouritePlaces) {
        this.favouritePlaces = favouritePlaces;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<CheckIn> getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(List<CheckIn> checkIns) {
        this.checkIns = checkIns;
    }
}
