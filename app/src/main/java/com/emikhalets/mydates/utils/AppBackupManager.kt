package com.emikhalets.mydates.utils

import android.content.Context
import android.net.Uri
import com.emikhalets.mydates.data.database.entities.Event
import org.json.JSONArray
import org.json.JSONObject
import java.io.*

object AppBackupManager {

    private const val ID = "id"
    private const val EVENT_TYPE = "event_type"
    private const val NAME = "name"
    private const val LASTNAME = "lastname"
    private const val MIDDLE_NAME = "middle_name"
    private const val DATE_TS = "date_ts"
    private const val DAYS_LEFT = "daysLeft"
    private const val AGE = "age"
    private const val GROUP = "group"
    private const val NOTES = "notes"
    private const val WITHOUT_YEAR = "without_year"

    fun fillCreatedFile(context: Context, uri: Uri, events: List<Event>): Boolean {
        val list = mutableListOf<JSONObject>()
        events.forEach { list.add(createJsonEvent(it)) }

        return try {
            context.contentResolver.openFileDescriptor(uri, "w")?.use { descriptor ->
                FileOutputStream(descriptor.fileDescriptor).use { stream ->
                    stream.write(list.toString().toByteArray())
                }
            }
            true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun createJsonEvent(event: Event): JSONObject {
        val item = JSONObject()
        try {
            item.put(ID, event.id)
            item.put(EVENT_TYPE, event.eventType)
            item.put(NAME, event.name)
            item.put(LASTNAME, event.lastName)
            item.put(MIDDLE_NAME, event.middleName)
            item.put(DATE_TS, event.date)
            item.put(DAYS_LEFT, event.daysLeft)
            item.put(AGE, event.age)
            item.put(GROUP, event.group)
            item.put(NOTES, event.notes)
            item.put(WITHOUT_YEAR, event.withoutYear)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return item
    }

    fun readFileAndCreateEventsList(context: Context, uri: Uri): List<Event> {
        return try {
            var json = ""
            context.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
                BufferedReader(FileReader(descriptor.fileDescriptor)).use { stream ->
                    json += stream.readLine()
                }
            }
            createEventsList(json)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun createEventsList(json: String): List<Event> {
        val array = JSONArray(json)
        val list = mutableListOf<Event>()
        for (i in 0 until array.length()) {
            list.add(
                Event(
                    id = array.getJSONObject(i).getLong(ID),
                    name = array.getJSONObject(i).getString(NAME),
                    lastName = array.getJSONObject(i).getString(LASTNAME),
                    middleName = array.getJSONObject(i).getString(MIDDLE_NAME),
                    date = array.getJSONObject(i).getLong(DATE_TS),
                    daysLeft = array.getJSONObject(i).getInt(DAYS_LEFT),
                    age = array.getJSONObject(i).getInt(AGE),
                    eventType = array.getJSONObject(i).getInt(EVENT_TYPE),
                    group = array.getJSONObject(i).getString(GROUP),
                    notes = array.getJSONObject(i).getString(NOTES),
                    withoutYear = array.getJSONObject(i).getBoolean(WITHOUT_YEAR)
                )
            )
        }
        return list
    }
}