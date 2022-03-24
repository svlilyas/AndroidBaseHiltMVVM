package com.pi.androidbasehiltmvvm.core.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pi.androidbasehiltmvvm.BuildConfig
import com.pi.androidbasehiltmvvm.core.common.PreferenceManager
import com.pi.androidbasehiltmvvm.core.di.qualifers.DefaultOkHttpClientBuilder
import com.pi.androidbasehiltmvvm.core.di.qualifers.ProjectOkHttpClient
import com.pi.androidbasehiltmvvm.core.di.qualifers.ProjectRetrofit
import com.pi.androidbasehiltmvvm.core.network.*
import com.pi.data.remote.ProjectService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideProjectService(
        @ProjectRetrofit projectRetrofit: Retrofit
    ): ProjectService = projectRetrofit.create(ProjectService::class.java)

    @ProjectRetrofit
    @Provides
    fun provideProjectRetrofit(
        @ProjectOkHttpClient projectOkHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder().apply {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        baseUrl(BuildConfig.SERVICE_URL)
        addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        addConverterFactory(GsonConverterFactory.create(gson))
        addConverterFactory(ScalarsConverterFactory.create())
        client(projectOkHttpClient)
    }.build()

    @ProjectOkHttpClient
    @Provides
    fun provideProjectOkHttpClient(
        @DefaultOkHttpClientBuilder okHttpClientBuilder: OkHttpClient.Builder,
        @ApplicationContext context: Context
    ) = okHttpClientBuilder.apply {
        addInterceptor(
            ApiRequestInterceptor(
                provideNetworkController(context),
                PreferenceManager(context)
            )
        )
        /**
         *  If you want to add SSlPinning Certificate
         *
         val sslPinningApplier = APISSLPinningApplier()
         sslPinningApplier.apply(this, context.assets, sslPinningApplier.getCertificatePaths())
         sslPinningApplier.apply(
         this,
         BuildConfig.SERVICE_URL,
         sslPinningApplier.getSSLPublicKeys()
         )
         //For DEBUG
         sslPinningApplier.trustAllCertificatesForDebug(this)
         */
    }.build()

    @Provides
    @DefaultOkHttpClientBuilder
    fun provideDefaultOkHttpBuilder(
        @ApplicationContext context: Context
    ): OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(provideLoggingInterceptor())
        .addInterceptor(
            ApiRequestInterceptor(
                provideNetworkController(context),
                PreferenceManager(context)
            )
        )
        .callTimeout(DEFAULT_CALL_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)

    @Provides
    @Singleton
    fun provideNetworkController(
        @ApplicationContext context: Context
    ) = NetworkController(context)

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    /** Provides special OkHttp client for ONLY refresh token request **/
    @Provides
    fun provideClient(
        refreshToken: String
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.addHeader("Authorization", "Bearer $refreshToken")
                chain.proceed(builder.build())
            }.addNetworkInterceptor(loggingInterceptor)
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .build()
    }
}
