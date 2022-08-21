package com.pi.data.di

import android.content.Context
import com.pi.data.BuildConfig
import com.pi.data.network.*
import com.pi.data.network.interceptor.ApiRequestInterceptor
import com.pi.data.network.interceptor.AuthenticationInterceptor
import com.pi.data.network.interceptor.HttpRequestInterceptor
import com.pi.data.network.interceptor.UserAgentInterceptor
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkController(
        @ApplicationContext context: Context
    ) = NetworkController(context)

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideLoggingInterceptor())
            .addInterceptor(
                provideAuthenticationInterceptor()
            )
            .addInterceptor(provideUserAgentInterceptor())
            .addInterceptor(
                provideApiRequestInterceptor(context)
            )
            .callTimeout(DEFAULT_CALL_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpRequestInterceptor())
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMainService(retrofit: Retrofit): MainService {
        return retrofit.create(MainService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainClient(mainService: MainService): MainClient {
        return MainClient(mainService = mainService)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideAuthenticationInterceptor(): AuthenticationInterceptor =
        AuthenticationInterceptor(BuildConfig.SERVICE_API_KEY)

    @Provides
    @Singleton
    fun provideApiRequestInterceptor(@ApplicationContext context: Context): ApiRequestInterceptor =
        ApiRequestInterceptor(
            provideNetworkController(context)
        )

    @Provides
    @Singleton
    fun provideUserAgentInterceptor(): UserAgentInterceptor =
        UserAgentInterceptor()
}
