package com.emikhalets.mydates.utils.di

import android.content.Context
import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.database.dao.EventDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule() {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return AppDatabase.get(context)
    }

    @Singleton
    @Provides
    fun provideDatesDao(database: AppDatabase): EventDao {
        return database.eventDao
    }
}