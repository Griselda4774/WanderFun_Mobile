package com.example.wanderfunmobile.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wanderfunmobile.data.dto.ResponseDto;
import com.example.wanderfunmobile.data.dto.user.MiniUserDto;
import com.example.wanderfunmobile.data.api.backend.UserApi;
import com.example.wanderfunmobile.data.dto.user.UserCreateDto;
import com.example.wanderfunmobile.data.dto.user.UserDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.domain.model.Result;
import com.example.wanderfunmobile.domain.model.users.User;
import com.example.wanderfunmobile.domain.repository.UserRepository;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepositoryImpl implements UserRepository {
    private final UserApi userApi;
    private final ObjectMapper objectMapper;

    @Inject
    public UserRepositoryImpl(UserApi userApi, ObjectMapper objectMapper) {
        this.userApi = userApi;
        this.objectMapper = objectMapper;
    }

//    @Override
//    public LiveData<ResponseDto<SelfInfoDto>> getSelfInfo(String bearerToken) {
//        MutableLiveData<ResponseDto<SelfInfoDto>> getSelfInfoResponseLiveData = new MutableLiveData<>();
//        String errorType = "UserRepositoryImpl GetSelfInfo Error";
//
//        try {
//            Call<ResponseDto<SelfInfoDto>> call = userApi.getSelfInfo(bearerToken);
//            call.enqueue(new Callback<ResponseDto<SelfInfoDto>>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseDto<SelfInfoDto>> call,
//                                       @NonNull Response<ResponseDto<SelfInfoDto>> response) {
//                    getSelfInfoResponseLiveData.postValue(response.body());
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseDto<SelfInfoDto>> call,
//                                      @NonNull Throwable throwable) {
//                    Log.e(errorType, "Error during onFailure");
//                }
//            });
//        } catch (Exception e) {
//            Log.e(errorType, "Error during catch");
//        }
//
//        return getSelfInfoResponseLiveData;
//    }

    @Override
    public LiveData<Result<User>> getSelfInfo(String bearerToken) {
        MutableLiveData<Result<User>> selfInfoResponseLiveData = new MutableLiveData<>();

        try {
            Call<ResponseDto<UserDto>> call = userApi.getSelfInfo(bearerToken);
            call.enqueue(new Callback<ResponseDto<UserDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<UserDto>> call,
                                       @NonNull Response<ResponseDto<UserDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<User> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), User.class));
                        }
                        selfInfoResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<UserDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("UserRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("UserRepositoryImpl", "Error during getMiniSelfInfo", e);
        }

        return selfInfoResponseLiveData;
    }

    @Override
    public LiveData<Result<User>> getMiniSelfInfo(String bearerToken) {
        MutableLiveData<Result<User>> miniSelfInfoResponseLiveData = new MutableLiveData<>();

        try {
            Call<ResponseDto<MiniUserDto>> call = userApi.getMiniSelfInfo(bearerToken);
            call.enqueue(new Callback<ResponseDto<MiniUserDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<MiniUserDto>> call,
                                       @NonNull Response<ResponseDto<MiniUserDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<User> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), User.class));
                        }
                        miniSelfInfoResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<MiniUserDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("UserRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("UserRepositoryImpl", "Error during getMiniSelfInfo", e);
        }

        return miniSelfInfoResponseLiveData;
    }

    @Override
    public LiveData<Result<User>> updateSelfInfo(String bearerToken, User user) {
        MutableLiveData<Result<User>> updateSelfInfoResponseLiveData = new MutableLiveData<>();

        try {
            Call<ResponseDto<UserDto>> call = userApi.updateSelfInfo(bearerToken, objectMapper.map(user, UserCreateDto.class));
            call.enqueue(new Callback<ResponseDto<UserDto>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<UserDto>> call,
                                       @NonNull Response<ResponseDto<UserDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<User> result = new Result<>();
                        result.setError(response.body().isError());
                        result.setMessage(response.body().getMessage());
                        if (response.body().getData() != null) {
                            result.setData(objectMapper.map(response.body().getData(), User.class));
                        }
                        updateSelfInfoResponseLiveData.postValue(result);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDto<UserDto>> call,
                                      @NonNull Throwable throwable) {
                    Log.e("UserRepositoryImpl", "Error during onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("UserRepositoryImpl", "Error during getMiniSelfInfo", e);
        }

        return updateSelfInfoResponseLiveData;
    }

//    @Override
//    public LiveData<ResponseDto<SelfInfoDto>> updateSelfInfo(String bearerToken, ChangeInfoDto changeInfoDto) {
//        MutableLiveData<ResponseDto<SelfInfoDto>> updateSelfInfoResponseLiveData = new MutableLiveData<>();
//        String errorType = "UserRepositoryImpl UpdateSelfInfo Error";
//
//        try {
//            Call<ResponseDto<SelfInfoDto>> call = userApi.updateSelfInfo(bearerToken, changeInfoDto);
//            call.enqueue(new Callback<ResponseDto<SelfInfoDto>>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseDto<SelfInfoDto>> call,
//                                       @NonNull Response<ResponseDto<SelfInfoDto>> response) {
//                    updateSelfInfoResponseLiveData.postValue(response.body());
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseDto<SelfInfoDto>> call,
//                                      @NonNull Throwable throwable) {
//                    Log.e(errorType, "Error during onFailure");
//                }
//            });
//        } catch (Exception e) {
//            Log.e(errorType, "Error during catch");
//        }
//
//        return updateSelfInfoResponseLiveData;
//    }

//    @Override
//    public LiveData<ResponseDto<SelfInfoDto>> deleteSelf(String bearerToken) {
//        MutableLiveData<ResponseDto<SelfInfoDto>> deleteSelfResponseLiveData = new MutableLiveData<>();
//        String errorType = "UserRepositoryImpl DeleteSelf Error";
//
//        try {
//            Call<ResponseDto<SelfInfoDto>> call = userApi.deleteSelf(bearerToken);
//            call.enqueue(new Callback<ResponseDto<SelfInfoDto>>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseDto<SelfInfoDto>> call,
//                                       @NonNull Response<ResponseDto<SelfInfoDto>> response) {
//                    deleteSelfResponseLiveData.postValue(response.body());
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseDto<SelfInfoDto>> call,
//                                      @NonNull Throwable throwable) {
//                    Log.e(errorType, "Error during onFailure");
//                }
//            });
//        } catch (Exception e) {
//            Log.e(errorType, "Error during catch");
//        }
//
//        return deleteSelfResponseLiveData;
//    }
}
