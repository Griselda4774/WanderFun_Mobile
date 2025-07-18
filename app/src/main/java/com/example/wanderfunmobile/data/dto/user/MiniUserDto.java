package com.example.wanderfunmobile.data.dto.user;

import com.example.wanderfunmobile.data.dto.images.ImageDto;

public class MiniUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private ImageDto avatarImage;

    public MiniUserDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ImageDto getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(ImageDto avatarImage) {
        this.avatarImage = avatarImage;
    }
}
