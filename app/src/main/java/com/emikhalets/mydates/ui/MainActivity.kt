package com.emikhalets.mydates.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.ActivityMainBinding
import com.emikhalets.mydates.utils.EventType
import com.emikhalets.mydates.utils.navigateEventsToAddEvent
import com.emikhalets.mydates.utils.startAddEventDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private val destinationChangeListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.bottom_home, R.id.bottom_calendar -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    setOnAddEventClick()
                }
                R.id.addEventFragment, R.id.eventDetailsFragment, R.id.settingsFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupBottomBar()
        navController.addOnDestinationChangedListener(destinationChangeListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(destinationChangeListener)
    }

    private fun setupBottomBar() {
        navController = findNavController(R.id.nav_host)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bottom_home,
                R.id.bottom_calendar
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun setOnAddEventClick() {
        binding.btnAddEvent.setOnClickListener { v ->
            startAddEventDialog { eventType ->
                when (eventType) {
                    EventType.ANNIVERSARY -> navController.navigateEventsToAddEvent(EventType.ANNIVERSARY)
                    EventType.BIRTHDAY -> navController.navigateEventsToAddEvent(EventType.BIRTHDAY)
                }
            }

        }
    }
}