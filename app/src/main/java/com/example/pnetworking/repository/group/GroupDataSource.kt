package com.example.pnetworking.repository.group

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.GroupChat
import com.example.pnetworking.models.Participant
import com.example.pnetworking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class GroupDataSource {
    fun createGroup(image: Uri, name: String, bio: String): MutableLiveData<String> {
        val result = MutableLiveData<String>()
        val uid = FirebaseAuth.getInstance().uid!!
        val chatId = UUID.randomUUID().toString()
        val pid=UUID.randomUUID().toString()

        val filename = UUID.randomUUID().toString()
        val ref3 =
            FirebaseStorage.getInstance()
                .getReference("group_chat/$chatId/image/$filename")
        ref3.putFile(image).addOnSuccessListener {
            Log.d("xxx", ref3.downloadUrl.toString())
            val ref =
                FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId")
            val gChat = GroupChat()
            gChat.idChat = chatId
            gChat.idUSer = uid
            ref3.downloadUrl.addOnCompleteListener {
                gChat.image = it.toString()
            }
            gChat.name = name
            gChat.summery = bio
            gChat.type = "group"
            ref.setValue(gChat)
                .addOnSuccessListener {
                    Log.d("TAG", "Saved our chat message: ${ref.key}")
                    result.value = chatId
                }
            val ref2 =
                FirebaseDatabase.getInstance()
                    .getReference("/chat/group_chat/$chatId/participants/$pid")
            val par = Participant()
            par.idChat = chatId
            par.idUSer = uid
            par.role = "admin"
            ref2.setValue(par).addOnSuccessListener {
                Log.d("TAG", "Saved our chat message: ${ref.key}")
            }

        }
        return result
    }

    fun addAParticipant(user: User, chatId: String) {
        val pid=UUID.randomUUID().toString()
        val ref =
            FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId/participants/$pid")
        val par = Participant()
        par.idChat = chatId
        par.idUSer = user.id
        par.role = "typical"
        ref.setValue(par)
            .addOnSuccessListener {
                Log.d("TAG", "Saved our chat message: ${ref.key}")
            }

    }

    fun getGroupChat(chatId: String): MutableLiveData<GroupChat> {
        val result = MutableLiveData<GroupChat>()
        val ref =
            FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("NewMessage", p0.toString())
                val chat = p0.getValue(GroupChat::class.java)
                if (chat != null) {
                    result.value = chat
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return result
    }
    fun getParticipants(chatId: String):MutableLiveData<List<Participant>>{
        val result = MutableLiveData<List<Participant>>()
        val l=ArrayList<Participant>()
        val ref =
            FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId/participants")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("NewMessage", p0.toString())
                p0.children.forEach {
                    val p = it.getValue(Participant::class.java)
                    l.add(p!!)
                }
                result.value=l
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return result
    }
}