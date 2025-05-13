package com.example.wanderfunmobile.domain.model.posts;

import com.example.wanderfunmobile.domain.model.images.Image;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.model.trips.Trip;
import com.example.wanderfunmobile.domain.model.users.User;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private User user;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Place place;
    private boolean isTripShare;
    private Trip trip;
    private Image image;
    private Long likeCount;

    public Post() {
    }
}
