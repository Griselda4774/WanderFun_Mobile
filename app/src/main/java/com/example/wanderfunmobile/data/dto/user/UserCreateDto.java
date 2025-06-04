package com.example.wanderfunmobile.data.dto.user;

import com.example.wanderfunmobile.data.dto.images.ImageDto;

import java.time.LocalDate;

public class UserCreateDto {
    private String firstName;
    private String lastName;
    private ImageDto avatarImage;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;

    public UserCreateDto() {}

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

    public ImageDto getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(ImageDto avatarImage) {
        this.avatarImage = avatarImage;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
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
}
