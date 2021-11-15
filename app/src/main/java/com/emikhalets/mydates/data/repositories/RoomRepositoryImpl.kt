package com.emikhalets.mydates.data.repositories

import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.SingleResult
import com.emikhalets.mydates.data.database.dao.EventDao
import com.emikhalets.mydates.data.database.entities.Event
import java.util.*
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val eventDao: EventDao
) : DatabaseRepository {

    override suspend fun dropEvents() {
        eventDao.drop()
    }

    override suspend fun getAllEvents(lastUpdate: Long): ListResult<List<Event>> {
        return try {
            val now = Date().time
            if (now - lastUpdate > 86400000) {
                val events = eventDao.getAll()
                val newEvents = mutableListOf<Event>()
                events.forEach { newEvents.add(it.calculateParameters()) }
                eventDao.updateAll(newEvents)
            }

            val result = eventDao.getAll()
            if (result.isEmpty()) ListResult.EmptyList
            else ListResult.Success(result)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ListResult.Error(ex)
        }
    }

    override suspend fun getAllByDaysLeft(daysLeft: Int): ListResult<List<Event>> {
        return try {
            val result = eventDao.getAllByDaysLeft(daysLeft)
            if (result.isEmpty()) ListResult.EmptyList
            else ListResult.Success(result)
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

    override suspend fun insertAllEvents(events: List<Event>): CompleteResult<Nothing> {
        return try {
            val list = events.map { it.calculateParameters() }
            eventDao.insertAll(list)
            CompleteResult.Complete
        } catch (ex: Exception) {
            ex.printStackTrace()
            CompleteResult.Error(ex)
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