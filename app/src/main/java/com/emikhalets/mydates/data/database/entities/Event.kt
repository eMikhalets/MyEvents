package com.emikhalets.mydates.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.emikhalets.mydates.utils.EventType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "events_table")
data class Event(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "lastname") var lastName: String,
    @ColumnInfo(name = "middle_name") var middleName: String,
    @ColumnInfo(name = "date_ts") var date: Long,
    @ColumnInfo(name = "daysLeft") var daysLeft: Int,
    @ColumnInfo(name = "age") var age: Int,
    @ColumnInfo(name = "event_type") var eventType: Int,
    @ColumnInfo(name = "group") var group: String,
    @ColumnInfo(name = "notes") var notes: String,
    @ColumnInfo(name = "without_year") var withoutYear: Boolean
) : Parcelable {

    fun fullName(): String {
        return when (eventType) {
            0 -> "Divider"
            EventType.ANNIVERSARY.value -> name
            EventType.BIRTHDAY.value -> {
                if (middleName.isEmpty()) {
                    if (lastName.isEmpty()) name
                    else "$lastName $name"
                } else {
                    if (lastName.isEmpty()) "$name $middleName"
                    else "$lastName $name $middleName"
                }
            }
            else -> "Error"
        }
    }

    @Ignore
    constructor(
        month: Int
    ) : this(
        0,
        "",
        "",
        "",
        0,
        0,
        month,
        0,
        "",
        "",
        false
    )

    @Ignore
    constructor(
        name: String,
        date: Long,
        withoutYear: Boolean
    ) : this(
        0,
        name,
        "",
        "",
        date,
        0,
        0,
        EventType.ANNIVERSARY.value,
        "",
        "",
        withoutYear
    )

    @Ignore
    constructor(
        name: String,
        lastName: String,
        middleName: String,
        date: Long,
        withoutYear: Boolean
    ) : this(
        0,
        name,
        lastName,
        middleName,
        date,
        0,
        0,
        EventType.BIRTHDAY.value,
        "",
        "",
        withoutYear
    )
}