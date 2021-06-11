package com.emikhalets.mydates.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.emikhalets.mydates.data.database.dao.DatesDao
import com.emikhalets.mydates.data.database.entities.DateItem

@Database(entities = [DateItem::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val datesDao: DatesDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "dates.db"
        ).addMigrations(MIGRATION_1_2).build()

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE dates_table ADD COLUMN day INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE dates_table ADD COLUMN month INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE dates_table ADD COLUMN year INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}