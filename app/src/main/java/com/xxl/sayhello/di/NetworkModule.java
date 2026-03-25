package com.xxl.sayhello.di;

import com.xxl.sayhello.network.ApiService;
import com.xxl.sayhello.network.RetrofitClient;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public ApiService provideApiService() {
        return RetrofitClient.getApiService();
    }
}