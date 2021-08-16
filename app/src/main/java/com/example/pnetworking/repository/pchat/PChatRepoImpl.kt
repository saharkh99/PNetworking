package com.example.pnetworking.repository.pchat

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Chat
import com.example.pnetworking.models.Message
import com.example.pnetworking.models.User

class PChatRepoImpl(private val pChatDataSource: PChatDataSource):PChatRepository {
    override fun addChat(fid: String): MutableLiveData<String> =pChatDataSource.addChat(fid)
    override fun performSendMessage(
        text: String,
        chat: String,
        selectedPhotoUri: ArrayList<Uri>,
        reply:String,
        toId:String,
        isText:Boolean

    ): MutableLiveData<String> =pChatDataSource.performSendMessage(text, chat, selectedPhotoUri,reply,toId,isText)

    override fun listenForMessages(chat: String): MutableLiveData<Message> {
        return pChatDataSource.listenForMessages(chat)
    }

    override fun removeMessage(toChat: String, msgId: String){
        pChatDataSource.removeMessage(toChat, msgId)
    }

    override fun editMessage(msgId: String, toChat: String,text: String) {
        pChatDataSource.editMessage(msgId, toChat,text )
    }
    override fun seenMessage(toChat:String,userId:String):MutableLiveData<Boolean>{
        return pChatDataSource.seenMessage(toChat, userId)
    }

    override fun addToBlackList(toChat: String,userId: String): MutableLiveData<Boolean> {
        return pChatDataSource.addToBlackList(toChat,userId)
    }
    override fun numberOfNewMessages(toChat:String):MutableLiveData<HashMap<String, Any>>{
        return pChatDataSource.numberOfNewMessages(toChat)
    }

    override fun checkBlackList(toChat: String): MutableLiveData<Boolean> {
        return pChatDataSource.checkBlackList(toChat)
    }

}