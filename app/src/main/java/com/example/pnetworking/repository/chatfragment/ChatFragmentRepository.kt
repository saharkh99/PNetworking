package com.example.pnetworking.repository.chatfragment

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Message

interface ChatFragmentRepository {
    fun getRecentMessages(fromId:String): MutableLiveData<Message>
    fun getIdUser():MutableLiveData<String>
    fun getChats():MutableLiveData<ArrayList<String>>

}