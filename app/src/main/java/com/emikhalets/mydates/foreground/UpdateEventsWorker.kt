package com.emikhalets.mydates.foreground

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.APP_SHARED_PREFERENCES
import com.emikhalets.mydates.utils.APP_SP_UPDATE_EVENTS_TIME
import com.emikhalets.mydates.utils.sendErrorUpdateNotification
import java.util.*

class UpdateEventsWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.get(applicationContext).eventDao
        val repo = RoomRepository(database)

        when (repo.updateEvents()) {
            is CompleteResult.Error -> sendErrorUpdateNotification(applicationContext)
            CompleteResult.Complete -> {
                applicationContext
                    .getSharedPreferences(APP_SHARED_PREFERENCES, Application.MODE_PRIVATE).edit()
                    .putLong(APP_SP_UPDATE_EVENTS_TIME, Date().time).apply()
            }
        }

        return Result.success()
    }
}