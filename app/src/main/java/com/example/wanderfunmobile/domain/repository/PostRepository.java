package com.example.wanderfunmobile.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.posts.Post;

import java.util.List;

public interface PostRepository {
    LiveData<Result<List<Post>>> findAllPostsByCursor(String bearerToken, Long cursor, int size);
    LiveData<Result<Post>> findPostById(String bearerToken, Long postId);
    LiveData<Result<Post>> createPost(String bearerToken, Post post);
    LiveData<Result<Post>> updatePost(String bearerToken, Long postId, Post post);
    LiveData<Result<Post>> deletePost(String bearerToken, Long postId);
}
