package com.example.pnetworking.repository.chatfragment

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Message
import com.example.pnetworking.repository.pchat.PChatDataSource
import com.example.pnetworking.repository.pchat.PChatRepository

class ChatFragmentRepoImpl( val chatDataSource: ChatFragmentDataSource): ChatFragmentRepository {
    override fun getRecentMessages(fromId:String): MutableLiveData<Message> {
        return chatDataSource.getRecentMessage(fromId)
    }

    override fun getIdUser(): MutableLiveData<String> {
        return chatDataSource.getIdUser()
    }

    override fun getChats(): MutableLiveData<ArrayList<String>> {
        return chatDataSource.getChats()
    }


}