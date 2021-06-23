package com.emikhalets.mydates.foreground

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.sendErrorUpdateNotification

class UpdateEventsWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.get(applicationContext).eventDao
        val repo = RoomRepository(database)

        when (repo.updateEvents()) {
            is CompleteResult.Error -> sendErrorUpdateNotification(applicationContext)
            CompleteResult.Complete -> {
            }
        }

        return Result.success()
    }
}