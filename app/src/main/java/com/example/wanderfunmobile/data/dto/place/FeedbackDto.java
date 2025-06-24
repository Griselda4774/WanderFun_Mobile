package com.example.wanderfunmobile.data.dto.place;

import com.example.wanderfunmobile.data.dto.images.ImageDto;
import com.example.wanderfunmobile.data.dto.user.MiniUserDto;

import java.time.LocalDateTime;

public class FeedbackDto {
    private Long id;
    private MiniUserDto user;
    private int rating;
    private String content;
    private ImageDto image;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Long placeId;

    public FeedbackDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MiniUserDto getUser() {
        return user;
    }

    public void setUser(MiniUserDto user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }
}
