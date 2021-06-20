package com.emikhalets.mydates.data.repositories

import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.SingleResult
import com.emikhalets.mydates.data.database.entities.Event

interface DatabaseRepository {

    suspend fun getAllEvents(): ListResult<List<Event>>
    suspend fun getAllAfterDays(days: Int): ListResult<List<Event>>
    suspend fun getEventById(id: Long): SingleResult<Event>
    suspend fun insertEvent(event: Event): CompleteResult<Nothing>
    suspend fun updateEvents(): CompleteResult<Nothing>
    suspend fun updateEvent(event: Event): CompleteResult<Nothing>
    suspend fun deleteEvent(event: Event): CompleteResult<Nothing>
}