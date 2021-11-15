package com.emikhalets.mydates.utils.extentions

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.emikhalets.mydates.ui.MainActivity
import com.emikhalets.mydates.utils.enums.Language

fun Fragment.toast(@StringRes resource: Int) {
    Toast.makeText(requireContext(), resource, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.hideSoftKeyboard() {
    val inputMethodManager =
        activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}

fun Fragment.setActivityLanguage(language: Language) {
    (activity as MainActivity).setLanguage(language)
}