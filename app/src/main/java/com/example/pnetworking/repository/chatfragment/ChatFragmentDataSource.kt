package com.example.pnetworking.repository.chatfragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChatFragmentDataSource {
    fun getRecentMessage(): MutableLiveData<Message> {

        val fromId = FirebaseAuth.getInstance().uid
        var result = MutableLiveData<Message>()
        val latestMessageRef =
            FirebaseDatabase.getInstance().getReference("chat/latest-messages/$fromId/")
        latestMessageRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d("get",p0.childrenCount.toString())
                val chatMessage = p0.getValue(Message::class.java)
                if (chatMessage != null) {
                    Log.d("TAG1", chatMessage.context)
                    result.value=chatMessage
                }

            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

    })
      return result
    }
}