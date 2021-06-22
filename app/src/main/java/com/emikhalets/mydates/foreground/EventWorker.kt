package com.emikhalets.mydates.foreground

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.*

class EventWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.get(applicationContext).eventDao
        val repo = RoomRepository(database)

        val events = HashMap<String, List<Event>>()

        if (inputData.getBoolean(DATA_NOTIF_MONTH, false)) {
            when (val result = repo.getAllAfterDays(30)) {
                is ListResult.Success -> events[DATA_NOTIF_MONTH] = result.data
                is ListResult.Error -> return Result.failure()
                ListResult.EmptyList -> {
                }
            }
        }

        if (inputData.getBoolean(DATA_NOTIF_WEEK, false)) {
            when (val result = repo.getAllAfterDays(7)) {
                is ListResult.Success -> events[DATA_NOTIF_WEEK] = result.data
                is ListResult.Error -> return Result.failure()
                ListResult.EmptyList -> {
                }
            }
        }

        if (inputData.getBoolean(DATA_NOTIF_TWO_DAY, false)) {
            when (val result = repo.getAllAfterDays(2)) {
                is ListResult.Success -> events[DATA_NOTIF_TWO_DAY] = result.data
                is ListResult.Error -> return Result.failure()
                ListResult.EmptyList -> {
                }
            }
        }

        if (inputData.getBoolean(DATA_NOTIF_DAY, false)) {
            when (val result = repo.getAllAfterDays(1)) {
                is ListResult.Success -> events[DATA_NOTIF_DAY] = result.data
                is ListResult.Error -> return Result.failure()
                ListResult.EmptyList -> {
                }
            }
        }

        if (inputData.getBoolean(DATA_NOTIF_TODAY, false)) {
            when (val result = repo.getAllAfterDays(0)) {
                is ListResult.Success -> events[DATA_NOTIF_TODAY] = result.data
                is ListResult.Error -> return Result.failure()
                ListResult.EmptyList -> {
                }
            }
        }

        if (events.isNotEmpty()) sendEventsNotification(applicationContext, events)

        return Result.success()
    }
}