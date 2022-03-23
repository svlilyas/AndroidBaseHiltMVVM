package com.pi.androidbasehiltmvvm.core.di

import android.content.Context
import com.pi.androidbasehiltmvvm.core.common.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideProjectSharedPref(
        @ApplicationContext context: Context
    ) = PreferenceManager(context)
}
