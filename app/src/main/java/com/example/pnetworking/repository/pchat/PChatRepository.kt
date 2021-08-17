package com.example.pnetworking.repository.pchat

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Chat
import com.example.pnetworking.models.Message
import com.example.pnetworking.models.User

interface PChatRepository {
    fun addChat(fid:String): MutableLiveData<String>
    fun performSendMessage(text: String, chat: String, selectedPhotoUri: ArrayList<Uri>,reply:String,toId:String,isText:Boolean):MutableLiveData<String>
    fun listenForMessages( chat: String):MutableLiveData<Message>
    fun removeMessage(toChat:String,msgId:String)
    fun editMessage(msgId: String,toChat:String,text: String)
    fun seenMessage(toChat:String,userId:String):MutableLiveData<Boolean>
    fun addToBlackList(toChat:String,userId: String):MutableLiveData<Boolean>
    fun numberOfNewMessages(toChat:String):MutableLiveData<HashMap<String, Any>>
    fun checkBlackList(toChat: String): MutableLiveData<Boolean>
    fun removeFromBlackList(toChat: String): MutableLiveData<Boolean>
    fun addToMuteList(toChat: String): MutableLiveData<Boolean>
    fun removeFromMuteList(toChat: String): MutableLiveData<Boolean>
    fun checkMuteList(toChat: String): MutableLiveData<Boolean>
    fun IsInMuteList(toChat: String): MutableLiveData<Boolean>
}