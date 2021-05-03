package com.example.pnetworking.ui.pchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pnetworking.R
import com.example.pnetworking.models.User

class PrivateChat : AppCompatActivity() {
    //send message-notification- image-recent message-delete/edit
    var toUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_chat)
        toUser = intent.getParcelableExtra("KEY_USER")
        Log.d("user",toUser!!.name)
        if (toUser != null)
            supportActionBar?.title = toUser!!.emailText
    }
}