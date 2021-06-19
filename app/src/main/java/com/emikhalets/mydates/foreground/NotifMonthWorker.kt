package com.emikhalets.mydates.foreground

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emikhalets.mydates.data.database.AppDatabase
import com.emikhalets.mydates.data.database.ListResult
import com.emikhalets.mydates.data.repositories.RoomRepository
import com.emikhalets.mydates.utils.sendMonthNotification

class NotifMonthWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        Log.d("TAG", "doWork: started")
        val database = AppDatabase.get(applicationContext).eventDao
        val repo = RoomRepository(database)
        when (val result = repo.getAllThisMonth()) {
            is ListResult.Success -> sendMonthNotification(applicationContext, 152, result.data)
            ListResult.EmptyList -> {
            }
            is ListResult.Error -> {
            }
        }
        Log.d("TAG", "doWork: ended")
        return Result.success()
    }
}