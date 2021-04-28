package com.example.pnetworking.ui

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.pnetworking.R
import com.example.pnetworking.utils.ChatActivity
import com.example.pnetworking.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ChatActivity() {
    // number of friends-follow-signin error
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
        val navGraphIds = listOf(R.navigation.profile, R.navigation.chat, R.navigation.connection)
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

    override fun onStart() {
        super.onStart()
        changeOnlineStatus(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        changeOnlineStatus(false)
    }

    private fun changeOnlineStatus(bool: Boolean) {
        var result = MutableLiveData<Boolean>()
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val hashmap = HashMap<String, Any>()
        hashmap.put("online", bool)
        ref.updateChildren(hashmap).addOnSuccessListener {
            result.value = true
        }
    }
}