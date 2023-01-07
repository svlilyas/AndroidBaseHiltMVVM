package com.pi.data.di

import com.pi.data.network.MainClient
import com.pi.data.persistence.room.AppDao
import com.pi.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideMainRepository(
        mainClient: MainClient,
        appDao: AppDao,
        coroutineDispatcher: CoroutineDispatcher
    ): MainRepository {
        return MainRepository(
            mainClient = mainClient,
            appDao = appDao,
            ioDispatcher = coroutineDispatcher
        )
    }
}
