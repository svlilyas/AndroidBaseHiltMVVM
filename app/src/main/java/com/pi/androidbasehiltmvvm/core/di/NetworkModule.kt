package com.pi.androidbasehiltmvvm.core.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.pi.androidbasehiltmvvm.BuildConfig
import com.pi.androidbasehiltmvvm.core.common.PreferenceManager
import com.pi.androidbasehiltmvvm.core.db.AppDao
import com.pi.androidbasehiltmvvm.core.db.AppDatabase
import com.pi.androidbasehiltmvvm.core.di.qualifers.DefaultOkHttpClientBuilder
import com.pi.androidbasehiltmvvm.core.di.qualifers.ProjectOkHttpClient
import com.pi.androidbasehiltmvvm.core.di.qualifers.ProjectRetrofit
import com.pi.androidbasehiltmvvm.core.network.*
import com.pi.androidbasehiltmvvm.core.network.interceptor.ApiRequestInterceptor
import com.pi.androidbasehiltmvvm.core.network.interceptor.AuthenticationInterceptor
import com.pi.androidbasehiltmvvm.core.network.interceptor.UserAgentInterceptor
import com.pi.data.remote.ProjectService
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
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getAppDbInstance(context)

    @Provides
    @Singleton
    fun provideAppDao(
        appDatabase: AppDatabase
    ): AppDao = appDatabase.getAppDao()

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
        baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(projectOkHttpClient)
    }.build()

    @ProjectOkHttpClient
    @Provides
    fun provideProjectOkHttpClient(
        @DefaultOkHttpClientBuilder okHttpClientBuilder: OkHttpClient.Builder,
        @ApplicationContext context: Context
    ) = okHttpClientBuilder.apply {
        addInterceptor(
            provideApiRequestInterceptor(context)
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

    @Provides
    @Singleton
    fun provideNetworkController(
        @ApplicationContext context: Context
    ) = NetworkController(context)

    /** Provides special OkHttp client for ONLY refresh token request **/
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(provideAuthenticationInterceptor())
            .addInterceptor(provideUserAgentInterceptor())
            .addNetworkInterceptor(provideLoggingInterceptor())
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .build()
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
            provideNetworkController(context),
            PreferenceManager(context)
        )

    @Provides
    @Singleton
    fun provideUserAgentInterceptor(): UserAgentInterceptor =
        UserAgentInterceptor()
}
