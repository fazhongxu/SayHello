package com.xxl.sayhello.network;

import com.xxl.sayhello.models.LoginRequest;
import com.xxl.sayhello.models.LoginResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/login")
    Single<LoginResponse> login(@Body LoginRequest request);
}