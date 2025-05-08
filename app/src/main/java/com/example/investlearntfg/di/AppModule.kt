package com.example.investlearntfg.di

import com.example.investlearntfg.data.model.ApiKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getApiKey(): ApiKey {
        return ApiKey("d0e6uv1r01qv1dmld640d0e6uv1r01qv1dmld64g")
    }
}