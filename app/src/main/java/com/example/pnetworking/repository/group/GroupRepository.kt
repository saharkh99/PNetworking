package com.example.pnetworking.repository.group

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User

interface GroupRepository {
    fun createGroup(image: Uri, name:String, bio:String): MutableLiveData<String>
    fun addAParticipant(user: User, chatId: String)
}