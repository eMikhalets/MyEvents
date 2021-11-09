package com.emikhalets.mydates.utils.di

import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.repositories.DatabaseRepository
import com.emikhalets.mydates.data.repositories.RoomRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoriesModule {

    @Singleton
    @Provides
    fun providesDatabaseRepository(database: AppDatabase): DatabaseRepository {
        return RoomRepositoryImpl(database.eventDao)
    }
}