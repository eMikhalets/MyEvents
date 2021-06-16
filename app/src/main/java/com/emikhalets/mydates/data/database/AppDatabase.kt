package com.emikhalets.mydates.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emikhalets.mydates.data.database.dao.EventDao
import com.emikhalets.mydates.data.database.entities.Event

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val eventDao: EventDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "MyDates"
        ).build()
    }
}