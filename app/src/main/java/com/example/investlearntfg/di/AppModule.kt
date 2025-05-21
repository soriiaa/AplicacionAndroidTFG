package com.example.investlearntfg.di

import com.example.investlearntfg.data.model.ApiKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("finnhub")
    fun getApiKeyFinnHub(): ApiKey {
        return ApiKey("d0e6uv1r01qv1dmld640d0e6uv1r01qv1dmld64g")
    }

    @Provides
    @Singleton
    @Named("polygon")
    fun getApiKeyPolygon(): ApiKey {
        return ApiKey("sWG_SGucG1vSuLM8pGvpUpQJOd953z45")
    }

    @Provides
    @Singleton
    @Named("exchange")
    fun getApiKeyExchangeRate(): ApiKey {
        return ApiKey("648b3137c9bc7a55b144e32ef3ec960d")
    }

}