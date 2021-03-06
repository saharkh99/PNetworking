package com.example.pnetworking.ui.pchat

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Chat
import com.example.pnetworking.models.Message
import com.example.pnetworking.models.User
import com.example.pnetworking.repository.notifications.NotificationDataSource
import com.example.pnetworking.repository.pchat.PChatRepository
import com.example.pnetworking.utils.ChatViewmodel

class PrivateChatViewModel(private val pChatRepository: PChatRepository,
                           private val notificationDataSource: NotificationDataSource):ChatViewmodel() {
     fun addChat(fid: String): MutableLiveData<String> =pChatRepository.addChat(fid)
     fun performSendMessage(
          text: String,
          chat: String,
          selectedPhotoUri: ArrayList<Uri>,
          reply:String,
          toId:String,
          isText:Boolean
     ): MutableLiveData<String> {
          return pChatRepository.performSendMessage(text,  chat, selectedPhotoUri,reply,toId,isText)
     }
     fun listenForMessages(chat: String): MutableLiveData<Message> {
          return pChatRepository.listenForMessages(chat)
     }
     fun sendNotification(msg:Message,toChat:String,notify:Boolean){
          notificationDataSource.sendNotification(msg, toChat, notify)
     }
     fun removeMessage(toChat:String,msgId:String){
          pChatRepository.removeMessage(toChat, msgId)
     }
     fun editMessage(msgId: String,toChat:String,text: String){
          pChatRepository.editMessage(msgId, toChat,text)
     }
     fun seenMessage(toChat:String,userId:String):MutableLiveData<Boolean>{
         return pChatRepository.seenMessage(toChat, userId)
     }
     fun addToBlackList(toChat: String,userId:String):MutableLiveData<Boolean>{
          return pChatRepository.addToBlackList(toChat,userId)
     }
     fun numberOfNewMessages(toChat:String):MutableLiveData<String>{
          return pChatRepository.numberOfNewMessages(toChat)
     }
     fun checkBlackList(toChat: String): MutableLiveData<Boolean>{
          return pChatRepository.checkBlackList(toChat)
     }
     fun removeFromBlackList(toChat: String): MutableLiveData<Boolean>{
          return pChatRepository.removeFromBlackList(toChat)
     }
     fun addToMuteList(toChat: String): MutableLiveData<Boolean>{
          return pChatRepository.addToMuteList(toChat)
     }
     fun removeFromMuteList(toChat: String): MutableLiveData<Boolean>{
          return pChatRepository.removeFromMuteList(toChat)
     }
     fun checkMuteList(toChat: String): MutableLiveData<Boolean>{
          return pChatRepository.checkMuteList(toChat)
     }
     fun IsInMuteList(toChat: String): MutableLiveData<Boolean>{
          return pChatRepository.IsInMuteList(toChat)
     }
}