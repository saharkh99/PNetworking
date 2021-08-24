package com.example.pnetworking.ui.userchat

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Message
import com.example.pnetworking.repository.chatfragment.ChatFragmentRepository
import com.example.pnetworking.repository.pchat.PChatRepository
import com.example.pnetworking.utils.ChatViewmodel

class ChatFViewModel(private val pChatRepository: ChatFragmentRepository): ChatViewmodel()
{
    fun getRecentMessages(fromId:String): MutableLiveData<Message> =pChatRepository.getRecentMessages(fromId)
    fun getIdUser():MutableLiveData<String> =pChatRepository.getIdUser()
    fun getChats():MutableLiveData<ArrayList<String>> =pChatRepository.getChats()
}