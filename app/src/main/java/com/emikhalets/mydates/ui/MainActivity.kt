package com.emikhalets.mydates.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.ActivityMainBinding
import com.emikhalets.mydates.utils.AppDialogManager
import com.emikhalets.mydates.utils.AppNavigationManager
import com.emikhalets.mydates.utils.AppPermissionManager
import com.emikhalets.mydates.utils.di.appComponent
import com.emikhalets.mydates.utils.enums.AppTheme
import com.emikhalets.mydates.utils.enums.EventType
import com.emikhalets.mydates.utils.enums.Language
import com.emikhalets.mydates.utils.extentions.launchMainScope
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val contactsRequestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) {
                requestContactsPermission()
            }
        }

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
        appComponent.inject(this)
        setAppTheme()
        if (savedInstanceState == null) checkLanguage()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupBottomBar()
        requestContactsPermission()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(destinationChangeListener)
    }

    private fun setupBottomBar() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHost.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bottom_home,
                R.id.bottom_calendar
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(destinationChangeListener)
    }

    private fun setOnAddEventClick() {
        binding.btnAddEvent.setOnClickListener {
            AppDialogManager.showAddEvent(this) { eventType ->
                when (eventType) {
                    EventType.ANNIVERSARY -> {
                        AppNavigationManager.toAddEvent(navController, EventType.ANNIVERSARY)
                    }
                    EventType.BIRTHDAY -> {
                        AppNavigationManager.toAddEvent(navController, EventType.BIRTHDAY)
                    }
                }
            }
        }
    }

    private fun checkLanguage() {
        launchMainScope {
            val language = appComponent.appPreferences.getLanguage()
            val locale = Locale(language)
            Locale.setDefault(locale)
            resources.configuration.setLocale(locale)
            resources.updateConfiguration(resources.configuration, resources.displayMetrics)
        }
    }

    fun setLanguage(language: Language) {
        launchMainScope {
            val locale = Locale(language.langCode)
            Locale.setDefault(locale)
            resources.configuration.setLocale(locale)
            resources.updateConfiguration(resources.configuration, resources.displayMetrics)
            appComponent.appPreferences.setLanguage(language.langCode)
            recreate()
        }
    }

    private fun setAppTheme() {
        runBlocking {
            val savedTheme = appComponent.appPreferences.getTheme()
            setTheme(AppTheme.get(savedTheme).themeRes)
        }
    }

    private fun requestContactsPermission() {
        if (!AppPermissionManager.isContactsGranted(this)) {
            AppPermissionManager.requestContactsPermission(contactsRequestPermission)
        }
    }
}