package com.example.pnetworking.ui

import android.os.Bundle
import com.example.pnetworking.R
import com.example.pnetworking.utils.ChatActivity

class MainActivity : ChatActivity() {
    //check internet connection-sign in-profile-find friends
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showProgressDialog(this)
    }
}