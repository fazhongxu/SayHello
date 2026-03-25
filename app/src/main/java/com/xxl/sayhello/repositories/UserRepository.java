package com.xxl.sayhello.repositories;

import com.xxl.sayhello.models.LoginRequest;
import com.xxl.sayhello.models.LoginResponse;
import com.xxl.sayhello.network.ApiService;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Single;

@Singleton
public class UserRepository {
    private final ApiService apiService;

    @Inject
    public UserRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Single<LoginResponse> login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
//        return apiService.login(request);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(true);
        loginResponse.setUser(new LoginResponse.User());
        return Single.just(loginResponse);
    }
}