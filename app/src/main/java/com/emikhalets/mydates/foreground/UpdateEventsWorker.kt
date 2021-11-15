package com.emikhalets.mydates.foreground

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.database.CompleteResult
import com.emikhalets.mydates.data.repositories.RoomRepositoryImpl
import com.emikhalets.mydates.utils.AppNotificationManager
import com.emikhalets.mydates.utils.di.appComponent
import java.util.*

class UpdateEventsWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.get(applicationContext).eventDao
        val repo = RoomRepositoryImpl(database)

        when (repo.updateEvents()) {
            is CompleteResult.Error -> {
                AppNotificationManager.sendErrorUpdateNotification(applicationContext)
            }
            CompleteResult.Complete -> {
                applicationContext.appComponent.appPreferences.setEventsLastUpdateTime(Date().time)
            }
        }

        return Result.success()
    }
}