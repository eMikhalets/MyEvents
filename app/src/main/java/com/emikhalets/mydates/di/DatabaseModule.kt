package com.emikhalets.mydates.di

import android.content.Context
import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.database.dao.DatesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.get(context)

    @Singleton
    @Provides
    fun providesDatesDao(database: AppDatabase): DatesDao = database.datesDao
}