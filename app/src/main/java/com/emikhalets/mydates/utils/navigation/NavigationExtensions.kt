package com.emikhalets.mydates.utils.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

interface Router {
    fun routeTo(screen: String, params: Bundle = bundleOf()) {
        throw NotImplementedError(message = "You used router, but don't implemented it for screen $screen with params $params")
    }
}

@Composable
fun NavigationController(
    router: Router? = null,
    startDestination: String,
    screens: List<Pair<String, @Composable (NavController, Router?, Bundle?) -> Unit>> = emptyList()
) {
    val navigation = rememberNavController()

    NavHost(navController = navigation, startDestination = startDestination) {
        screens.forEach { screen ->
            composable(screen.first) {
                screen.second.invoke(
                    navigation,
                    router,
                    navigation.previousBackStackEntry?.arguments
                )
            }
        }
    }
}

fun createExternalRouter(block: (String, Bundle) -> Unit): Router = object : Router {
    override fun routeTo(screen: String, params: Bundle) {
        block.invoke(screen, params)
    }
}

fun NavController.navigate(
    route: String,
    param: Pair<String, Parcelable>?,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    param?.let { this.currentBackStackEntry?.arguments?.putParcelable(param.first, param.second) }
    navigate(route, builder)
}

fun NavController.navigate(
    route: String,
    params: List<Pair<String, Parcelable>>?,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    params?.let {
        val arguments = this.currentBackStackEntry?.arguments
        params.forEach { arguments?.putParcelable(it.first, it.second) }
    }
    navigate(route, builder)
}

fun NavController.navigate(
    route: String,
    params: Bundle?,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    this.currentBackStackEntry?.arguments?.putAll(params)
    navigate(route, builder)
}