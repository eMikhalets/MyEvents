package com.emikhalets.mydates.data.repositories

import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.SingleResult
import com.emikhalets.mydates.data.database.entities.DateItem

interface DatabaseRepository {

    suspend fun getAllDates(): ListResult<List<DateItem>>
    suspend fun getItemsByDayMonth(day: Int, month: Int): ListResult<List<DateItem>>
    suspend fun getDateById(id: Long): SingleResult<DateItem>
    suspend fun insertDate(dateItem: DateItem): CompleteResult<Nothing>
    suspend fun updateAllDates(): ListResult<List<DateItem>>
    suspend fun updateDate(dateItem: DateItem): CompleteResult<Nothing>
    suspend fun deleteDate(dateItem: DateItem): CompleteResult<Nothing>
}