package com.emikhalets.mydates.data.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "dates_table")
data class DateItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var date: Long,
    var daysLeft: Int = 0,
    var age: Int = 0,
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
) : Parcelable {

    @Ignore
    constructor(name: String) : this(0, name, 0, 0, 0, 0, 0, 0)
}