package com.emikhalets.mydates.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.emikhalets.mydates.databinding.DialogAddEventBinding
import com.emikhalets.mydates.databinding.DialogDatePickerBinding
import java.util.*

inline fun Fragment.startAddEventDialog(crossinline callback: (EventType) -> Unit) {
    val dialog = Dialog(requireContext())
    val binding = DialogAddEventBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)

    binding.apply {
        cardBirthday.setOnClickListener { callback.invoke(EventType.BIRTHDAY) }
        cardAnniversary.setOnClickListener { callback.invoke(EventType.ANNIVERSARY) }
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