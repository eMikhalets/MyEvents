package com.emikhalets.mydates.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.emikhalets.mydates.data.database.dao.EventDao
import com.emikhalets.mydates.data.database.entities.Event

@Database(entities = [Event::class], version = 2, exportSchema = false)
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
        ).addMigrations(MIGRATION_1_2).build()

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE events_table ADD COLUMN image_uri TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}