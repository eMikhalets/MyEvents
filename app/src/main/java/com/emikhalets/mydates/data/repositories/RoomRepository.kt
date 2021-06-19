package com.emikhalets.mydates.data.repositories

import android.util.Log
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.SingleResult
import com.emikhalets.mydates.data.database.dao.EventDao
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.utils.calculateParameters
import com.emikhalets.mydates.utils.sortWithDividers
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val eventDao: EventDao
) : DatabaseRepository {

    override suspend fun getAllEvents(): ListResult<List<Event>> {
        return try {
            val result = eventDao.getAll()
            if (result.isEmpty()) {
                ListResult.EmptyList
            } else {
                val events = sortWithDividers(result)
                ListResult.Success(events)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            ListResult.Error(ex)
        }
    }

    override suspend fun getAllThisMonth(): ListResult<List<Event>> {
        return try {
            val result = eventDao.getAllLessDays(30)
            Log.d("TAG", "getAllThisMonth: ${result.size} items")
            if (result.isEmpty()) {
                ListResult.EmptyList
            } else {
                val events = sortWithDividers(result)
                ListResult.Success(events)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            ListResult.Error(ex)
        }
    }

    override suspend fun getEventById(id: Long): SingleResult<Event> {
        return try {
            val result = eventDao.getItem(id)
            SingleResult.Success(result)
        } catch (ex: Exception) {
            ex.printStackTrace()
            SingleResult.Error(ex)
        }
    }

    override suspend fun insertEvent(event: Event): CompleteResult<Nothing> {
        return try {
            eventDao.insert(event)
            CompleteResult.Complete
        } catch (ex: Exception) {
            ex.printStackTrace()
            CompleteResult.Error(ex)
        }
    }

    override suspend fun updateEvents(): CompleteResult<Nothing> {
        return try {
            val events = eventDao.getAll()
            val newEvents = mutableListOf<Event>()
            events.forEach { newEvents.add(it.calculateParameters()) }
            eventDao.updateAll(newEvents)
            CompleteResult.Complete
        } catch (ex: Exception) {
            ex.printStackTrace()
            CompleteResult.Error(ex)
        }
    }

    override suspend fun updateEvent(event: Event): CompleteResult<Nothing> {
        return try {
            eventDao.update(event)
            CompleteResult.Complete
        } catch (ex: Exception) {
            ex.printStackTrace()
            CompleteResult.Error(ex)
        }
    }

    override suspend fun deleteEvent(event: Event): CompleteResult<Nothing> {
        return try {
            eventDao.delete(event)
            CompleteResult.Complete
        } catch (ex: Exception) {
            ex.printStackTrace()
            CompleteResult.Error(ex)
        }
    }
}