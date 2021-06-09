package com.emikhalets.mydates.data.repositories

import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.SingleResult
import com.emikhalets.mydates.data.database.dao.DatesDao
import com.emikhalets.mydates.data.database.entities.DateItem
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val datesDao: DatesDao
) : DatabaseRepository {

    override suspend fun getAllDates(): ListResult<List<DateItem>> {
        return try {
            val result = datesDao.getAll()
            if (result.isEmpty()) ListResult.EmptyList
            else ListResult.Success(result)
        } catch (ex: Exception) {
            ListResult.Error(ex)
        }
    }

    override suspend fun getDateById(id: Long): SingleResult<DateItem> {
        return try {
            val result = datesDao.getItem(id)
            SingleResult.Success(result)
        } catch (ex: Exception) {
            SingleResult.Error(ex)
        }
    }

    override suspend fun insertDate(dateItem: DateItem): CompleteResult<Nothing> {
        return try {
            datesDao.insert(dateItem)
            CompleteResult.Complete
        } catch (ex: Exception) {
            CompleteResult.Error(ex)
        }
    }

    override suspend fun updateDate(dateItem: DateItem): CompleteResult<Nothing> {
        return try {
            datesDao.update(dateItem)
            CompleteResult.Complete
        } catch (ex: Exception) {
            CompleteResult.Error(ex)
        }
    }

    override suspend fun deleteDate(dateItem: DateItem): CompleteResult<Nothing> {
        return try {
            datesDao.delete(dateItem)
            CompleteResult.Complete
        } catch (ex: Exception) {
            CompleteResult.Error(ex)
        }
    }
}