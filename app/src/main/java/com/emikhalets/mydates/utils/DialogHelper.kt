package com.emikhalets.mydates.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.databinding.DialogAddDateBinding
import com.emikhalets.mydates.databinding.DialogDatePickerBinding
import java.util.*

class DialogHelper {

    @SuppressLint("ClickableViewAccessibility")
    fun startAddDateDialog(context: Context, callback: (DateItem) -> Unit) {
        val dialog = Dialog(context)
        val binding = DialogAddDateBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        var name = ""
        var date = 0L
        val dateItem = DateItem(name)

        binding.apply {
            inputDate.setOnTouchListener { v, event ->
                v.performClick()
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= v.right - inputDate.totalPaddingRight) {
                        event.action = MotionEvent.ACTION_CANCEL
                        startDatePickerDialog(context, date) {
                            date = it
                            inputDate.setText(it.dateFormat("d MMMM YYYY"))
                        }
                    }
                }
                true
            }

            btnAdd.setOnClickListener {
                name = inputName.text.toString()
                if (name != "" || date != 0L) {
                    dateItem.apply {
                        this.name = name
                        this.date = date
                    }
                    callback.invoke(dateItem)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun startDatePickerDialog(context: Context, init: Long = 0L, callback: (Long) -> Unit) {
        val dialog = Dialog(context)
        val binding = DialogDatePickerBinding.inflate(LayoutInflater.from(context))
        val calendar = Calendar.getInstance()
        var ts = 0L
        dialog.setContentView(binding.root)

        if (init != 0L) calendar.timeInMillis = init
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        binding.datePicker.init(year, month, day) { _, newYear, newMonth, newDay ->
            val picked = Calendar.getInstance()
            picked.set(newYear, newMonth, newDay)
            ts = picked.timeInMillis
        }

        binding.btnAdd.setOnClickListener {
            callback.invoke(ts)
            dialog.dismiss()
        }

        dialog.show()
    }
}