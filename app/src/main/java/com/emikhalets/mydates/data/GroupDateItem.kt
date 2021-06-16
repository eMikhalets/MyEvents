package com.emikhalets.mydates.data

import android.os.Parcelable
import com.emikhalets.mydates.data.database.entities.DateItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupDateItem(
    var id: Long,
    var name: String,
    var date: Long,
    var daysLeft: Int,
    var age: Int,
    var day: Int,
    var month: Int,
    var year: Int,
    var type: Int
) : Parcelable {

    constructor(type: Int, month: Int) : this(0, "", 0, 0, 0, 0, month, 0, type)

    constructor(type: Int, item: DateItem) :
            this(
                item.id,
                item.name,
                item.date,
                item.daysLeft,
                item.age,
                item.day,
                item.month,
                item.year,
                type
            )
}