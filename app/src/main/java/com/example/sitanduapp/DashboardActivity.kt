package com.example.sitanduapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Setup Navigation
        setupNavigation()
    }

    private fun setupNavigation() {
        // Get BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)

        // Get NavController from NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup BottomNavigationView with NavController
        bottomNavigationView.setupWithNavController(navController)

        // Debug: Tambahkan listener untuk melihat apakah menu diklik
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardFragment -> {
                    navController.navigate(R.id.dashboardFragment)
                    true
                }
                R.id.jadwalFragment -> {
                    navController.navigate(R.id.jadwalFragment)
                    true
                }
                R.id.jtFragment -> {
                    navController.navigate(R.id.jtFragment)
                    true
                }
                R.id.ttdFragment -> {
                    navController.navigate(R.id.ttdFragment)
                    true
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    // Function untuk logout (dipanggil dari ProfileFragment)
    fun logout() {
        // Hapus semua data user dari SharedPreferences
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            // Hapus semua data user
            remove("userEmail")
            remove("userName")
            remove("userRole")
            remove("userInfo")

            // Hapus data temp jika ada
            remove("temp_userName")
            remove("temp_userRole")
            remove("temp_userEmail")
            remove("temp_userInfo")

            apply()
        }

        // Navigasi ke LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}