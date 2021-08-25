package com.example.pnetworking.repository.group

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.GroupChat
import com.example.pnetworking.models.Message
import com.example.pnetworking.models.Participant
import com.example.pnetworking.models.User


class GroupRepoImpl(private val groupDataSource: GroupDataSource): GroupRepository {
    override fun createGroup(image: Uri, name: String, bio: String): MutableLiveData<String> {
        return groupDataSource.createGroup(image, name, bio)
    }

    override fun addAParticipant(user: User, chatId: String) {
        return groupDataSource.addAParticipant(user, chatId)
    }

    override fun getGroupChat(chatId: String): MutableLiveData<GroupChat> {
        return groupDataSource.getGroupChat(chatId)
    }
    override fun getParticipants(chatId: String):MutableLiveData<List<Participant>>{
        return groupDataSource.getParticipants(chatId)
    }

    override fun editGroup(name: String, bio: String, chatId: String): MutableLiveData<Boolean> {
        return groupDataSource.editGroup(name, bio, chatId)
    }

    override fun listenForMessages(chat: String): MutableLiveData<Message> {
        return groupDataSource.listenForMessages(chat)
    }

    override fun performSendMessage(
        text: String,
        chat: String,
        selectedPhotoUri: ArrayList<Uri>,
        reply: String,
        toID: String,
        isText: Boolean
    ): MutableLiveData<String> {
        return groupDataSource.performSendMessage(text, chat, selectedPhotoUri, reply, toID, isText)
    }

    override fun getNameOfUser(userId: String): MutableLiveData<String> {
        return groupDataSource.getNameOfUser(userId)
    }

    override fun editMessage(msgId: String, toChat: String, text: String) {
        return groupDataSource.editMessage(msgId, toChat, text)
    }

    override fun removeMessage(toChat: String, msgId: String) {
        return groupDataSource.removeMessage(toChat,msgId)
    }

}