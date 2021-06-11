package com.emikhalets.mydates.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.databinding.DialogAddDateBinding
import com.emikhalets.mydates.databinding.DialogDatePickerBinding
import java.util.*

@SuppressLint("ClickableViewAccessibility")
inline fun Fragment.startAddDateDialog(crossinline callback: (DateItem) -> Unit) {
    val dialog = Dialog(requireContext())
    val binding = DialogAddDateBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)

    var name = ""
    var date = Calendar.getInstance().timeInMillis
    val dateItem = DateItem(name)

    binding.apply {
        inputDate.setText(date.dateFormat("d MMMM YYYY"))
        inputDate.setOnDrawableEndClick {
            startDatePickerDialog(requireContext(), date) {
                date = it
                inputDate.setText(it.dateFormat("d MMMM YYYY"))
            }
        }

        btnAdd.setOnClickListener {
            name = inputName.text.toString()
            if (name.isNotEmpty()) {
                dateItem.name = name
                dateItem.date = date
                callback.invoke(dateItem)
                dialog.dismiss()
            }
        }
    }

    dialog.show()
}

inline fun Fragment.startDatePickerDialog(init: Long, crossinline callback: (Long) -> Unit) {
    val dialog = Dialog(requireContext())
    val binding = DialogDatePickerBinding.inflate(LayoutInflater.from(requireContext()))
    val calendar = Calendar.getInstance()
    dialog.setContentView(binding.root)

    calendar.timeInMillis = init
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    binding.datePicker.init(year, month, day) { _, newYear, newMonth, newDay ->
        val picked = Calendar.getInstance()
        picked.set(newYear, newMonth, newDay)
        calendar.timeInMillis = picked.timeInMillis
    }

    binding.btnAdd.setOnClickListener {
        callback.invoke(calendar.timeInMillis)
        dialog.dismiss()
    }

    dialog.show()
}

inline fun startDatePickerDialog(
    context: Context,
    init: Long,
    crossinline callback: (Long) -> Unit
) {
    val dialog = Dialog(context)
    val binding = DialogDatePickerBinding.inflate(LayoutInflater.from(context))
    val calendar = Calendar.getInstance()
    dialog.setContentView(binding.root)

    calendar.timeInMillis = init
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    binding.datePicker.init(year, month, day) { _, newYear, newMonth, newDay ->
        val picked = Calendar.getInstance()
        picked.set(newYear, newMonth, newDay)
        calendar.timeInMillis = picked.timeInMillis
    }

    binding.btnAdd.setOnClickListener {
        callback.invoke(calendar.timeInMillis)
        dialog.dismiss()
    }

    dialog.show()
}