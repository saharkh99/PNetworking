package com.example.pnetworking.repository.chatfragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragmentDataSource {
    fun getRecentMessage(fromId:String): MutableLiveData<Message> {
        var result = MutableLiveData<Message>()
        val latestMessageRef =
            FirebaseDatabase.getInstance().getReference("chat/latest-messages/$fromId/")
        Log.d("group",latestMessageRef.child("context").key!!)
        if(latestMessageRef.child("context").key!=null){
            Log.d("group","group")
            latestMessageRef.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("get",p0.childrenCount.toString())
                    val chatMessage = p0.getValue(Message::class.java)
                    if (chatMessage != null) {
                        Log.d("TAG1", chatMessage.context)
                        result.value=chatMessage
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
       else{
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

        }


      return result
    }
    fun getChats():MutableLiveData<ArrayList<String>>{
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

    fun getIdUser():MutableLiveData<String>{
        val fromId = FirebaseAuth.getInstance().uid
        var result = MutableLiveData<String>()
        result.value=fromId
        return result
    }
}