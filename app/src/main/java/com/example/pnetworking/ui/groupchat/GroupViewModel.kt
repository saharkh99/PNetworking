package com.example.pnetworking.ui.groupchat

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.GroupChat
import com.example.pnetworking.models.Participant
import com.example.pnetworking.models.User
import com.example.pnetworking.repository.group.GroupRepository
import com.example.pnetworking.utils.ChatViewmodel

class GroupViewModel(private val groupRepo: GroupRepository):ChatViewmodel(){
    fun createGroup(image: Uri, name:String, bio:String): MutableLiveData<String>{
        return groupRepo.createGroup(image, name, bio)
    }
    fun addAParticipant(user: User, chatId: String)=groupRepo.addAParticipant(user, chatId)
    fun getGroupChat(chatId: String): MutableLiveData<GroupChat>{
       return groupRepo.getGroupChat(chatId)
    }
    fun getParticipants(chatId: String):MutableLiveData<List<Participant>>{
        return groupRepo.getParticipants(chatId)
    }
    fun editGroup(name: String, bio: String,chatId: String): MutableLiveData<Boolean>{
        return groupRepo.editGroup(name, bio, chatId)
    }
}