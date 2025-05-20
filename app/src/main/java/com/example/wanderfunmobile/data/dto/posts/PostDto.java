package com.example.wanderfunmobile.data.dto.posts;

import com.example.wanderfunmobile.data.dto.images.ImageDto;
import com.example.wanderfunmobile.data.dto.place.MiniPlaceDto;
import com.example.wanderfunmobile.data.dto.trip.TripDto;
import com.example.wanderfunmobile.data.dto.user.MiniUserDto;

import java.time.LocalDateTime;

public class PostDto {
    private Long id;
    private MiniUserDto user;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private MiniPlaceDto place;
    private boolean isTripShare;
    private TripDto trip;
    private ImageDto image;
    private Long likeCount;
    private Long commentCount;
    private boolean isLiked;

    public PostDto() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public MiniPlaceDto getPlace() {
        return place;
    }

    public void setPlace(MiniPlaceDto place) {
        this.place = place;
    }

    public boolean isTripShare() {
        return isTripShare;
    }

    public void setTripShare(boolean tripShare) {
        isTripShare = tripShare;
    }

    public TripDto getTrip() {
        return trip;
    }

    public void setTrip(TripDto trip) {
        this.trip = trip;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
