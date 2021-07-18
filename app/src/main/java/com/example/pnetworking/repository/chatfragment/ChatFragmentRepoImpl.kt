package com.example.pnetworking.repository.chatfragment

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Message
import com.example.pnetworking.repository.pchat.PChatDataSource
import com.example.pnetworking.repository.pchat.PChatRepository

class ChatFragmentRepoImpl( val chatDataSource: ChatFragmentDataSource): ChatFragmentRepository {
    override fun getRecentMessages(): MutableLiveData<Message> {
        return chatDataSource.getRecentMessage()
    }

}