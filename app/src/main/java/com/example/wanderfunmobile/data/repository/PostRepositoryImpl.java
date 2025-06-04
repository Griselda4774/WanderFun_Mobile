package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.api.backend.PostApi;
import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.posts.CommentCreateDto;
import com.example.wanderfunmobile.data.dto.posts.CommentDto;
import com.example.wanderfunmobile.data.dto.posts.PostCreateDto;
import com.example.wanderfunmobile.data.dto.posts.PostDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.posts.Comment;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.domain.repository.PostRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepositoryImpl implements PostRepository {

    private final PostApi postApi;

    private final ObjectMapper objectMapper;

    @Inject
    public PostRepositoryImpl(PostApi postApi, ObjectMapper objectMapper) {
        this.postApi = postApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public LiveData<Result<List<Post>>> findAllPostsByCursor(Long cursor, int size) {
        MutableLiveData<Result<List<Post>>> findAllPostsByCursorResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<PostDto>>> call = postApi.findAllPostsByCursor(cursor, size);
            call.enqueue(new Callback<ResponseDto<List<PostDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PostDto>>> call,
                                       @NonNull Response<ResponseDto<List<PostDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Post>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Post.class));
                        }
                        findAllPostsByCursorResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<PostDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PostRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PostRepositoryImpl", "Error during findAllPostsByCursor", e);
        }

        return findAllPostsByCursorResponseLiveData;
    }

    @Override
    public LiveData<Result<List<Post>>> findAllPostsWithSize(int size) {
        MutableLiveData<Result<List<Post>>> findAllPostsByCursorResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<PostDto>>> call = postApi.findAllPostsWithSize(size);
            call.enqueue(new Callback<ResponseDto<List<PostDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PostDto>>> call,
                                       @NonNull Response<ResponseDto<List<PostDto>>> response) {
                    Log.d("PostRepositoryImpl", "Response: " + response.body());
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Post>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Post.class));
                        }
                        findAllPostsByCursorResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<PostDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PostRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PostRepositoryImpl", "Error during findAllPostsByCursor", e);
        }

        return findAllPostsByCursorResponseLiveData;
    }

    @Override
    public LiveData<Result<List<Post>>> findAllPostsNoParam() {
        MutableLiveData<Result<List<Post>>> findAllPostsByCursorResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<PostDto>>> call = postApi.findAllPostsNoParam();
            call.enqueue(new Callback<ResponseDto<List<PostDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<PostDto>>> call,
                                       @NonNull Response<ResponseDto<List<PostDto>>> response) {
                    Log.d("PostRepositoryImpl", "Response: " + response.body());
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Post>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Post.class));
                        }
                        findAllPostsByCursorResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<PostDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PostRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PostRepositoryImpl", "Error during findAllPostsByCursor", e);
        }

        return findAllPostsByCursorResponseLiveData;
    }

    @Override
    public LiveData<Result<Post>> findPostById(Long postId) {
        MutableLiveData<Result<Post>> findPostByIdResponseLiveData = new MutableLiveData<>();

        try {
            Call<ResponseDto<PostDto>> call = postApi.findPostById(postId);
            call.enqueue(new Callback<ResponseDto<PostDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<PostDto>> call,
                                       @NonNull Response<ResponseDto<PostDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Post> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Post.class));
                        }
                        findPostByIdResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<PostDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PostRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PostRepositoryImpl", "Error during findPostById", e);
        }

        return findPostByIdResponseLiveData;
    }

    @Override
    public LiveData<Result<Post>> createPost(String bearerToken, Post post) {
        MutableLiveData<Result<Post>> createPostResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<PostDto>> call = postApi.createPost(bearerToken, objectMapper.map(post, PostCreateDto.class));
            PostCreateDto postCreateDto = objectMapper.map(post, PostCreateDto.class);
            call.enqueue(new Callback<ResponseDto<PostDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<PostDto>> call,
                                       @NonNull Response<ResponseDto<PostDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Post> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Post.class));
                        }
                        createPostResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<PostDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PostRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PostRepositoryImpl", "Error during createPost", e);
        }
        return createPostResponseLiveData;
    }

    @Override
    public LiveData<Result<Post>> updatePost(String bearerToken, Long postId, Post post) {
        MutableLiveData<Result<Post>> updatePostResponseLiveData = new MutableLiveData<>();

        try {
            Call<ResponseDto<PostDto>> call = postApi.updatePost(bearerToken, postId, objectMapper.map(post, PostCreateDto.class));
            call.enqueue(new Callback<ResponseDto<PostDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<PostDto>> call,
                                       @NonNull Response<ResponseDto<PostDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Post> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Post.class));
                        }
                        updatePostResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<PostDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PostRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PostRepositoryImpl", "Error during updatePost", e);
        }

        return updatePostResponseLiveData;
    }

    @Override
    public LiveData<Result<Post>> deletePost(String bearerToken, Long postId) {
        MutableLiveData<Result<Post>> deletePostResponseLiveData = new MutableLiveData<>();

        try {
            Call<ResponseDto<PostDto>> call = postApi.deletePost(bearerToken, postId);
            call.enqueue(new Callback<ResponseDto<PostDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<PostDto>> call,
                                       @NonNull Response<ResponseDto<PostDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Post> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Post.class));
                        }
                        deletePostResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<PostDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PostRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PostRepositoryImpl", "Error during deletePost", e);
        }

        return deletePostResponseLiveData;
    }

    @Override
    public LiveData<Result<List<Comment>>> findAllCommentsByPostId(String bearerToken, Long postId) {
        MutableLiveData<Result<List<Comment>>> findAllCommentsByPostIdResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<List<CommentDto>>> call = postApi.findAllCommentsByPostId(bearerToken, postId);
            call.enqueue(new Callback<ResponseDto<List<CommentDto>>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<List<CommentDto>>> call,
                                       @NonNull Response<ResponseDto<List<CommentDto>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<List<Comment>> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.mapList(response.body().getData(), Comment.class));
                        }
                        findAllCommentsByPostIdResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<List<CommentDto>>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PostRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PostRepositoryImpl", "Error during findAllPostsByCursor", e);
        }

        return findAllCommentsByPostIdResponseLiveData;
    }

    @Override
    public LiveData<Result<Comment>> createComment(String bearerToken, Long postId, Comment comment, String localId) {
        MutableLiveData<Result<Comment>> createCommentResponseLiveData = new MutableLiveData<>();
        try {
            Call<ResponseDto<CommentDto>> call = postApi.createComment(bearerToken, postId, objectMapper.map(comment, CommentCreateDto.class));
            call.enqueue(new Callback<ResponseDto<CommentDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<CommentDto>> call,
                                       @NonNull Response<ResponseDto<CommentDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Comment> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Comment.class));
                            result.getData().setLocalId(localId);
                        }
                        createCommentResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<CommentDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("PostRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PostRepositoryImpl", "Error during createPost", e);
        }
        return createCommentResponseLiveData;
    }

    @Override
    public LiveData<Result<Comment>> updateComment(String bearerToken, Long commentId, Comment comment, String localId) {
        MutableLiveData<Result<Comment>> updateCommentResponseLiveData = new MutableLiveData<>();

        try {
            Call<ResponseDto<CommentDto>> call = postApi.updateComment(bearerToken, commentId, objectMapper.map(comment, CommentCreateDto.class));
            call.enqueue(new Callback<ResponseDto<CommentDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<CommentDto>> call,
                                       @NonNull Response<ResponseDto<CommentDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Comment> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Comment.class));
                            result.getData().setLocalId(localId);
                        }
                        updateCommentResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<CommentDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("CommentRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("CommentRepositoryImpl", "Error during updateComment", e);
        }

        return updateCommentResponseLiveData;
    }

    @Override
    public LiveData<Result<Comment>> deleteComment(String bearerToken, Long commentId, String localId) {
        MutableLiveData<Result<Comment>> deleteCommentResponseLiveData = new MutableLiveData<>();

        try {
            Call<ResponseDto<CommentDto>> call = postApi.deleteComment(bearerToken, commentId);
            call.enqueue(new Callback<ResponseDto<CommentDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<CommentDto>> call,
                                       @NonNull Response<ResponseDto<CommentDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<Comment> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), Comment.class));
                        } else {
                            result.setData(new Comment());
                        }
                        result.getData().setLocalId(localId);
                        deleteCommentResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<CommentDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("CommentRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("CommentRepositoryImpl", "Error during deleteComment", e);
        }

        return deleteCommentResponseLiveData;
    }
}
