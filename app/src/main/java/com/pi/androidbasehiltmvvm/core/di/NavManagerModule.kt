package com.pi.androidbasehiltmvvm.core.di

import com.pi.androidbasehiltmvvm.core.navigation.NavManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object NavManagerModule {
    @Provides
    fun provideNavManager(): NavManager {
        return NavManager()
    }
}
