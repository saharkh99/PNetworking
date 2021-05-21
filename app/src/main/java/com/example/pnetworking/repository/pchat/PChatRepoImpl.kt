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
        selectedPhotoUri: Uri,
        reply:String

    ): MutableLiveData<String> =pChatDataSource.performSendMessage(text, chat, selectedPhotoUri,reply)

    override fun listenForMessages(chat: String): MutableLiveData<Message> {
        return pChatDataSource.listenForMessages(chat)
    }

    override fun removeMessage(toChat: String, msgId: String){
        pChatDataSource.removeMessage(toChat, msgId)
    }

    override fun editMessage(text: String, toChat: String) {
        pChatDataSource.editMessage(text, toChat)
    }


}