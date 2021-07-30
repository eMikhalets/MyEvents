package com.emikhalets.mydates.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.emikhalets.mydates.ui.theme.AppTheme
import com.emikhalets.mydates.utils.navigation.AppDestinations.EVENTS_LIST
import com.emikhalets.mydates.utils.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyDatesAppContent()
        }
    }
}

@Composable
fun MyDatesAppContent() {
    AppTheme {
        val navController = rememberNavController()

        AppNavGraph(
            navController = navController,
            startDestination = EVENTS_LIST
        )
    }
}