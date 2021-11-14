package com.senex.androidlab1.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.senex.androidlab1.R
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.utils.generateUsers

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Singleton initialization
        AppDatabaseMain.init(this)

        // Setting up navigation host for Jetpack's Navigation component framework
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Setting up navigation view for Jetpack's Navigation component framework
        binding.bottomNavigationViewMain.setupWithNavController(navController)

        // Debugging block
        val userDao = AppDatabaseMain.database.userDao()
        userDao.deleteAll()
        userDao.insertAll(
            *generateUsers(8).toTypedArray()
        )
    }
}

