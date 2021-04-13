package com.example.pnetworking.ui

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.pnetworking.R
import com.example.pnetworking.utils.ChatActivity
import com.example.pnetworking.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : ChatActivity() {
    //check internet connection-bugs
    private var currentNavController: LiveData<NavController>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationMain)
        val navGraphIds = listOf(R.navigation.chat, R.navigation.connection, R.navigation.profile)
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        currentNavController = controller
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}