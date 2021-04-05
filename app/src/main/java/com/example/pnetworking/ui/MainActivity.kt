package com.example.pnetworking.ui

import android.os.Bundle
import com.example.pnetworking.R
import com.example.pnetworking.utils.ChatActivity

class MainActivity : ChatActivity() {
    //intro-check internet connection-sign up-sign in-questions-model users-
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showProgressDialog(true,this)
    }
}