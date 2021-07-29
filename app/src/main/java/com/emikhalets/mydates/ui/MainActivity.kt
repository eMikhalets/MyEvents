package com.emikhalets.mydates.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.emikhalets.mydates.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityMainBinding
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
            }
        }
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)
//        setupBottomBar()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

//    private fun setupBottomBar() {
//        navController = findNavController(R.id.nav_host)
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.bottom_home,
//                R.id.bottom_calendar
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        binding.bottomNav.setupWithNavController(navController)
//    }
}