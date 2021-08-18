package com.example.pnetworking.ui.userchat

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Message
import com.example.pnetworking.repository.chatfragment.ChatFragmentRepository
import com.example.pnetworking.repository.pchat.PChatRepository
import com.example.pnetworking.utils.ChatViewmodel

class ChatFViewModel(private val pChatRepository: ChatFragmentRepository): ChatViewmodel()
{
    fun getRecentMessages(): MutableLiveData<Message> =pChatRepository.getRecentMessages()
    fun getIdUser():MutableLiveData<String> =pChatRepository.getIdUser()
}