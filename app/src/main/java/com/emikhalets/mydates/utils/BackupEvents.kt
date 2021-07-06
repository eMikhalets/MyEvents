package com.emikhalets.mydates.utils

import android.net.Uri
import com.emikhalets.mydates.data.database.entities.Event
import org.json.JSONObject
import java.io.*
import java.util.*

object BackupEvents {

    fun createJsonEvents(filePath: File?, events: List<Event>): String {
        val list = mutableListOf<JSONObject>()
        events.forEach { list.add(createJsonEvent(it)) }
        val file = File(filePath, "backup_${Date().time}.json")
        file.createNewFile()
        val output = FileOutputStream(file)
        output.write(list.toString().toByteArray())
        output.close()
        return file.absolutePath
    }

    private fun createJsonEvent(event: Event): JSONObject {
        val item = JSONObject()
        try {
            item.put("id", event.id)
            item.put("event_type", event.eventType)
            item.put("name", event.name)
            item.put("lastname", event.lastName)
            item.put("middle_name", event.middleName)
            item.put("date_ts", event.date)
            item.put("daysLeft", event.daysLeft)
            item.put("age", event.age)
            item.put("group", event.group)
            item.put("notes", event.notes)
            item.put("without_year", event.withoutYear)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return item
    }
}