package com.emikhalets.mydates.utils.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import java.io.Serializable

fun NavController.navigate(
    route: String,
    params: Bundle?,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    this.currentBackStackEntry?.arguments = params
    navigate(route, builder)
}

@Composable
fun <T : Parcelable> NavController.getArgParcelable(
    key: String,
    content: @Composable (T) -> Unit
) {
    this.previousBackStackEntry?.arguments?.getParcelable<T>(key)?.let {
        content(it)
    }
}

@Composable
fun NavController.getIntArgument(
    key: String,
    content: @Composable (Int) -> Unit
) {
    this.previousBackStackEntry?.arguments?.getInt(key)?.let {
        content(it)
    }
}