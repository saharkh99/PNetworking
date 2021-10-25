package com.example.pnetworking.ui.groupchat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.GroupChat
import com.example.pnetworking.models.Message
import com.example.pnetworking.models.Participant
import com.example.pnetworking.models.User
import com.example.pnetworking.repository.group.GroupRepository
import com.example.pnetworking.utils.ChatViewmodel

class GroupViewModel(private val groupRepo: GroupRepository):ChatViewmodel(){
    companion object {
        val participants=MutableLiveData<String>()
        val a=ArrayList<String>()
        val listParticipant=MutableLiveData<ArrayList<String>>(a)

    }
    fun createGroup(image: Uri, name:String, bio:String): MutableLiveData<String>{
        return groupRepo.createGroup(image, name, bio)
    }
    fun addAParticipant(user: User, chatId: String)=groupRepo.addAParticipant(user, chatId)
    fun getGroupChat(chatId: String): MutableLiveData<GroupChat>{
       return groupRepo.getGroupChat(chatId)
    }
    fun getParticipants(chatId: String):MutableLiveData<List<Participant>>{
        return groupRepo.getParticipants(chatId)
    }
    fun editGroup(name: String, bio: String,chatId: String): MutableLiveData<Boolean>{
        return groupRepo.editGroup(name, bio, chatId)
    }
    fun setAddedMembers(id:String){
        participants.value=id
        listParticipant.value!!.add(id)
    }
    fun removeAddedMembers(id:String){
        participants.value=id
        listParticipant.value!!.remove(id)
        Log.d("xxxx",listParticipant.value!!.toString())

    }
    fun getAddedMembers():MutableLiveData<ArrayList<String>>{
        return listParticipant
    }
    fun listenForMessages(chat: String): MutableLiveData<Message> =groupRepo.listenForMessages(chat)
    fun performSendMessage(
        text: String,
        chat: String,
        selectedPhotoUri: ArrayList<Uri>,
        reply: String,
        toID: String,
        isText: Boolean
    ): MutableLiveData<String> =groupRepo.performSendMessage(text, chat, selectedPhotoUri, reply, toID, isText)
    fun getNameOfUser(userId:String):MutableLiveData<String> =groupRepo.getNameOfUser(userId)

    fun editMessage(msgId: String, toChat: String, text: String) =groupRepo.editMessage(msgId, toChat, text)

    fun removeMessage(toChat: String, msgId: String) =groupRepo.removeMessage(toChat, msgId)

    fun removeChat(chatId: String)=groupRepo.removeChat(chatId)

}