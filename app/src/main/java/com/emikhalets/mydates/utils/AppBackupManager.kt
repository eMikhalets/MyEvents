package com.emikhalets.mydates.utils

import android.content.Context
import android.net.Uri
import com.emikhalets.mydates.data.database.entities.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.FileReader

object AppBackupManager {

    fun export(context: Context, uri: Uri, events: List<Event>): Boolean {
        val json = Gson().toJson(events)
        return try {
            context.contentResolver.openFileDescriptor(uri, "w")?.use { descriptor ->
                FileOutputStream(descriptor.fileDescriptor).use { stream ->
                    stream.write(json.toByteArray())
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun import(context: Context, uri: Uri): List<Event> {
        return try {
            var json = ""
            context.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
                BufferedReader(FileReader(descriptor.fileDescriptor)).use { stream ->
                    json += stream.readLine()
                }
            }
            val type = object : TypeToken<List<Event>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}