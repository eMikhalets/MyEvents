package com.emikhalets.mydates.data.database.dao

import androidx.room.*
import com.emikhalets.mydates.data.database.entities.Event

@Dao
interface EventDao {

    @Query("SELECT * FROM events_table ORDER BY daysLeft ASC")
    suspend fun getAll(): List<Event>

    @Query("SELECT * FROM events_table WHERE daysLeft = :days ORDER BY daysLeft ASC")
    suspend fun getAllAfterDays(days: Int): List<Event>

    @Query("SELECT * FROM events_table WHERE id = :id")
    suspend fun getItem(id: Long): Event

    @Insert
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Update
    suspend fun updateAll(events: List<Event>)

    @Delete
    suspend fun delete(event: Event)
}