package com.example.pnetworking.repository.group

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Follow
import com.example.pnetworking.models.GroupChat
import com.example.pnetworking.models.Participant
import com.example.pnetworking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class GroupDataSource {
    fun createGroup(image: Uri, name: String, bio: String): MutableLiveData<String> {
        var result = MutableLiveData<String>()
        val uid = FirebaseAuth.getInstance().uid!!
        val chatId = UUID.randomUUID().toString()
        val ref =
            FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId")
        val gChat = GroupChat()
        gChat.idChat = chatId
        gChat.idUSer = uid
        gChat.image = image.path!!
        gChat.name = name
        gChat.summery = bio
        gChat.type = "group"
        ref.setValue(gChat)
            .addOnSuccessListener {
                Log.d("TAG", "Saved our chat message: ${ref.key}")
                result.value = chatId
            }
        val ref2 =
            FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId/participants/")
        val par = Participant()
        par.idChat = chatId
        par.idUSer = uid
        par.role = "typical"
        ref2.setValue(par).addOnSuccessListener {
            Log.d("TAG", "Saved our chat message: ${ref.key}")
        }
        val filename = UUID.randomUUID().toString()
        val ref3 =
            FirebaseStorage.getInstance()
                .getReference("group_chat/$chatId/image/$filename")
        ref3.putFile(image)
        return result
    }

    fun addAParticipant(user: User, chatId: String) {
        val uid = FirebaseAuth.getInstance().uid!!
        val ref =
            FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId/participants/")

        val par = Participant()
        par.idChat = chatId
        par.idUSer = user.id
        par.role = "typical"
        ref.setValue(par)
            .addOnSuccessListener {
                Log.d("TAG", "Saved our chat message: ${ref.key}")
            }

    }
}