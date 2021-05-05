package com.example.pnetworking.repository.pchat

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User

interface PChatRepository {
    fun addChat(fid:String): MutableLiveData<String>
    fun performSendMessage(text: String, chat: String, selectedPhotoUri: Uri):MutableLiveData<String>
}