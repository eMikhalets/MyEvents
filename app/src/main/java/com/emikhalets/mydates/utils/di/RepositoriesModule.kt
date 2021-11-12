package com.emikhalets.mydates.utils.di

import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.repositories.DatabaseRepository
import com.emikhalets.mydates.data.repositories.RoomRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Singleton
    @Provides
    fun provideDatabaseRepository(database: AppDatabase): DatabaseRepository {
        return RoomRepositoryImpl(database.eventDao)
    }
}