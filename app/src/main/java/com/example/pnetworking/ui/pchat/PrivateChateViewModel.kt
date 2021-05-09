package com.example.pnetworking.ui.pchat

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Chat
import com.example.pnetworking.models.Message
import com.example.pnetworking.models.User
import com.example.pnetworking.repository.pchat.PChatRepository
import com.example.pnetworking.utils.ChatViewmodel

class PrivateChateViewModel(private val pChatRepository: PChatRepository):ChatViewmodel() {
     fun addChat(fid: String): MutableLiveData<String> =pChatRepository.addChat(fid)
     fun performSendMessage(
          text: String,
          chat: String,
          selectedPhotoUri: Uri
     ): MutableLiveData<String> {
          return pChatRepository.performSendMessage(text,  chat, selectedPhotoUri)
     }
     fun listenForMessages(chat: String): MutableLiveData<Message> {
          return pChatRepository.listenForMessages(chat)
     }
}