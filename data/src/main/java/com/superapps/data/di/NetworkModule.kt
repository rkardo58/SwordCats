package com.superapps.data.di

import com.superapps.data.BuildConfig
import com.superapps.data.network.CatsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideJson() = Json { ignoreUnknownKeys = true }

    @Provides
    fun provideHttpClient() =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val req =
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("x-api-key", BuildConfig.CAT_API_KEY)
                        .build()
                chain.proceed(req)
            }.build()

    @Provides
    fun provideRetrofit(
        json: Json,
        client: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    fun provideCatsApi(retrofit: Retrofit): CatsApi = retrofit.create(CatsApi::class.java)
}
