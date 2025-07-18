package com.example.wanderfunmobile.data.api.backend;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.posts.CommentCreateDto;
import com.example.wanderfunmobile.data.dto.posts.CommentDto;
import com.example.wanderfunmobile.data.dto.posts.PostCreateDto;
import com.example.wanderfunmobile.data.dto.posts.PostDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostApi {
    @GET("post/cursor")
    Call<ResponseDto<List<PostDto>>> findAllPostsByCursor(@Query("cursor") Long cursor,
                                                          @Query("size") Integer size);

//    @GET("post/cursor")
//    Call<ResponseDto<List<PostDto>>> findAllPostsWithSize(@Query("size") int size);
//
//    @GET("post/cursor")
//    Call<ResponseDto<List<PostDto>>> findAllPostsNoParam();

    @GET("post/user")
    Call<ResponseDto<List<PostDto>>> findAllPostsByUser(@Header("Authorization") String bearerToken);

    @GET("post/{postId}")
    Call<ResponseDto<PostDto>> findPostById(@Path("postId") Long postId);

    @POST("post")
    Call<ResponseDto<PostDto>> createPost(@Header("Authorization") String bearerToken,
                                          @Body PostCreateDto postCreateDto);

    @PUT("post/{postId}")
    Call<ResponseDto<PostDto>> updatePost(@Header("Authorization") String bearerToken,
                                          @Path("postId") Long postId,
                                          @Body PostCreateDto postCreateDto);

    @DELETE("post/{postId}")
    Call<ResponseDto<PostDto>> deletePost(@Header("Authorization") String bearerToken,
                                          @Path("postId") Long postId);

    @GET("post/comment")
    Call<ResponseDto<List<CommentDto>>> findAllCommentsByPostId(@Header("Authorization") String bearerToken,
                                                                @Query("postId") Long postId);

    @POST("post/comment")
    Call<ResponseDto<CommentDto>> createComment(@Header("Authorization") String bearerToken,
                                                @Query("postId") Long postId,
                                                @Body CommentCreateDto commentCreateDto);

    @PUT("post/comment/{commentId}")
    Call<ResponseDto<CommentDto>> updateComment(@Header("Authorization") String bearerToken,
                                                @Path("commentId") Long commentId,
                                                @Body CommentCreateDto commentCreateDto);

    @DELETE("post/comment/{commentId}")
    Call<ResponseDto<CommentDto>> deleteComment(@Header("Authorization") String bearerToken,
                                                @Path("commentId") Long commentId);
}
