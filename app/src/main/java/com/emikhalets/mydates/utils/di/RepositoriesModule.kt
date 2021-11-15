package com.emikhalets.mydates.utils.di

import android.content.Context
import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.repositories.AppPrefsRepository
import com.emikhalets.mydates.data.repositories.DataStoreRepositoryImpl
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

    @Singleton
    @Provides
    fun provideAppPrefsRepository(context: Context): AppPrefsRepository {
        return DataStoreRepositoryImpl(context)
    }
}