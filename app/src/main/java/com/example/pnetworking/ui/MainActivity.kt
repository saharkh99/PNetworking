package com.example.pnetworking.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.pnetworking.R
import com.example.pnetworking.models.Token
import com.example.pnetworking.utils.ChatActivity
import com.example.pnetworking.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : ChatActivity() {
    //share preference,search,setting
    private var currentNavController: LiveData<NavController>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        updateToken(FirebaseInstanceId.getInstance().getToken()!!)
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
        changeOnlineStatus(false).observe(this,{
            if(it){
                super.onDestroy()
            }
        })
    }

    private fun changeOnlineStatus(bool: Boolean):MutableLiveData<Boolean> {
        var result = MutableLiveData<Boolean>()
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val hashmap = HashMap<String, Any>()
        hashmap.put("online", bool)
        ref.updateChildren(hashmap).addOnCompleteListener {
            result.value = true
        }
        return result
    }

    fun updateToken(token: String){
        val ref=FirebaseDatabase.getInstance().getReference("token")
        val mtoken=Token(token)
        Log.d("token",mtoken.token)
        ref.child(FirebaseAuth.getInstance().uid!!).setValue(mtoken)

    }

    override fun onResume() {
        super.onResume()
        checkUserStatus()
    }

    private fun checkUserStatus() {
        val sp=getSharedPreferences("SP_USER", MODE_PRIVATE)
        val editor=sp.edit()
        editor.putString("CURRENT_USERID",FirebaseAuth.getInstance().uid!!)
        editor.apply()
    }
}