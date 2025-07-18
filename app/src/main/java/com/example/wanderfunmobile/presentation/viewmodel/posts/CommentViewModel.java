package com.example.wanderfunmobile.presentation.viewmodel.posts;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.posts.Comment;
import com.example.wanderfunmobile.domain.repository.PostRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CommentViewModel extends ViewModel {
    private final PostRepository postRepository;

    @Inject
    public CommentViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    private final MutableLiveData<Result<List<Comment>>> findAllCommentsByPostIdLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Comment>> createCommentLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Comment>> updateCommentLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Comment>> deleteCommentLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Result<List<Comment>>> getFindAllCommentsByPostIdLiveData() {
        return findAllCommentsByPostIdLiveData;
    }

    public MutableLiveData<Result<Comment>> getCreateCommentLiveData() {
        return createCommentLiveData;
    }

    public MutableLiveData<Result<Comment>> getUpdateCommentLiveData() {
        return updateCommentLiveData;
    }

    public MutableLiveData<Result<Comment>> getDeleteCommentLiveData() {
        return deleteCommentLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void findAllCommentsByPostId(String bearerToken, Long postId) {
        isLoading.setValue(true);
        postRepository.findAllCommentsByPostId(bearerToken, postId).observeForever(result -> {
            isLoading.setValue(false);
            findAllCommentsByPostIdLiveData.setValue(result);
        });
    }

    public void createComment(String bearerToken, Long postId, Comment comment, String localId) {
        isLoading.setValue(true);
        postRepository.createComment(bearerToken, postId, comment, localId).observeForever(result -> {
            isLoading.setValue(false);
            createCommentLiveData.setValue(result);
        });
    }

    public void updateComment(String bearerToken, Long commentId, Comment comment, String localId) {
        isLoading.setValue(true);
        postRepository.updateComment(bearerToken, commentId, comment, localId).observeForever(result -> {
            isLoading.setValue(false);
            updateCommentLiveData.setValue(result);
        });
    }

    public void deleteComment(String bearerToken, Long commentId, String localId) {
        isLoading.setValue(true);
        postRepository.deleteComment(bearerToken, commentId,localId).observeForever(result -> {
            isLoading.setValue(false);
            deleteCommentLiveData.setValue(result);
        });
    }
}
