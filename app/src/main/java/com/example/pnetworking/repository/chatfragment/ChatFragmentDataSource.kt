package com.example.pnetworking.repository.chatfragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragmentDataSource {
    fun getRecentMessage(fromId: String): MutableLiveData<Message> {
        var result = MutableLiveData<Message>()
        var group: Boolean
        var additional=true
        val latestMessageRef =
            FirebaseDatabase.getInstance().getReference("chat/latest-messages/$fromId/")
        Log.d("TAG1111", latestMessageRef.ref.toString())
        latestMessageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    try {
                        val chatMessage = it.getValue(Message::class.java)
                        Log.d("get", chatMessage!!.idUSer)
                        if (chatMessage != null) {
                            Log.d("TAG1", chatMessage.context)
                            result.value = chatMessage
                        }
                        group = false
                    } catch (e: Exception) {
                        group = true
                    }
                    if (group && additional) {
                        val chatMessage = p0.getValue(Message::class.java)
                        Log.d("get", chatMessage!!.context)
                        if (chatMessage != null) {
                            Log.d("TAG1", chatMessage.context)
                            result.value = chatMessage
                            group=false
                            additional=false
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return result
    }

    fun getChats(): MutableLiveData<ArrayList<String>> {
        val fromId = FirebaseAuth.getInstance().uid
        val result = MutableLiveData<ArrayList<String>>()
        val ref2 =
            FirebaseDatabase.getInstance().getReference("/users/$fromId/groups/")
        ref2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val l = ArrayList<String>()
                snapshot.children.forEach {
                    Log.d("users", it.key.toString())
                    l.add(it.value.toString())
                }
                result.value = l
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return result
    }

    fun getIdUser(): MutableLiveData<String> {
        val fromId = FirebaseAuth.getInstance().uid
        var result = MutableLiveData<String>()
        result.value = fromId
        return result
    }
}