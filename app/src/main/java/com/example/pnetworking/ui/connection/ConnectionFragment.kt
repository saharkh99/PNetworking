package com.example.pnetworking.ui.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.pnetworking.R
import com.example.pnetworking.ui.chat.ChatFragment

class ConnectionFragment: ChatFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connection,container,false)
    }

}