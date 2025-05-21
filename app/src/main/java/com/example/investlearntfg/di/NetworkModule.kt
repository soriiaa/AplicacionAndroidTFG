package com.example.investlearntfg.di

import com.example.investlearntfg.data.remote.ExchangeRateApiService
import com.example.investlearntfg.data.remote.FinnHubApiService
import com.example.investlearntfg.data.remote.PolygonApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Interceptor de logs HTTP
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Muestra TODO
        }

        return OkHttpClient.Builder()

            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("finnhub")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFinnHubApiService(@Named("finnhub") retrofit: Retrofit): FinnHubApiService {
        return retrofit.create(FinnHubApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("polygon")
    fun providePolygonRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.polygon.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePolygonApiService(@Named("polygon") retrofit: Retrofit): PolygonApiService {
        return retrofit.create(PolygonApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("exchange")
    fun provideExchangeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.exchangerate.host/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Usamos Gson como en los otros
            .build()
    }

    @Provides
    @Singleton
    fun provideExchangeRateApiService(@Named("exchange") retrofit: Retrofit): ExchangeRateApiService {
        return retrofit.create(ExchangeRateApiService::class.java)
    }

}