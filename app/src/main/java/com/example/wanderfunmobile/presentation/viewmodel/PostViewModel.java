package com.example.wanderfunmobile.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.domain.repository.PostRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PostViewModel extends ViewModel {
    private final PostRepository postRepository;

    @Inject
    public PostViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    private final MutableLiveData<Result<List<Post>>> findAllPostsByCursorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Post>> findPostByIdLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Post>> createPostLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Post>> updatePostLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Post>> deletePostLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Result<List<Post>>> getFindAllPostsByCursorLiveData() {
        return findAllPostsByCursorLiveData;
    }

    public MutableLiveData<Result<Post>> getFindPostByIdLiveData() {
        return findPostByIdLiveData;
    }

    public MutableLiveData<Result<Post>> getCreatePostLiveData() {
        return createPostLiveData;
    }

    public MutableLiveData<Result<Post>> getUpdatePostLiveData() {
        return updatePostLiveData;
    }

    public MutableLiveData<Result<Post>> getDeletePostLiveData() {
        return deletePostLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void findAllPostsByCursor(String bearerToken, Long cursor, int size) {
        isLoading.setValue(true);
        postRepository.findAllPostsByCursor(bearerToken, cursor, size).observeForever(result -> {
            isLoading.setValue(false);
            findAllPostsByCursorLiveData.setValue(result);
        });
    }

    public void findPostById(String bearerToken, Long postId) {
        isLoading.setValue(true);
        postRepository.findPostById(bearerToken, postId).observeForever(result -> {
            isLoading.setValue(false);
            findPostByIdLiveData.setValue(result);
        });
    }

    public void createPost(String bearerToken, Post post) {
        isLoading.setValue(true);
        postRepository.createPost(bearerToken, post).observeForever(result -> {
            isLoading.setValue(false);
            createPostLiveData.setValue(result);
        });
    }

    public void updatePost(String bearerToken, Long postId, Post post) {
        isLoading.setValue(true);
        postRepository.updatePost(bearerToken, postId, post).observeForever(result -> {
            isLoading.setValue(false);
            updatePostLiveData.setValue(result);
        });
    }

    public void deletePost(String bearerToken, Long postId) {
        isLoading.setValue(true);
        postRepository.deletePost(bearerToken, postId).observeForever(result -> {
            isLoading.setValue(false);
            deletePostLiveData.setValue(result);
        });
    }
}
