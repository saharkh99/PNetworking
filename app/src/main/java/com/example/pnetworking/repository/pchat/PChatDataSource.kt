package com.example.pnetworking.repository.pchat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Chat
import com.example.pnetworking.models.Message
import com.example.pnetworking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PChatDataSource {
    fun performSendMessage(
        text: String,
        chat: String,
        selectedPhotoUri: Uri
    ): MutableLiveData<String> {
        val value = MutableLiveData<String>()
        val fromId = FirebaseAuth.getInstance().uid
        var toChat = ""
        if (chat != null) {
            toChat = chat
        }
        if (fromId == null) return value
        val reference =
            FirebaseDatabase.getInstance().getReference("/user_message/$fromId/$toChat").push()
        val toReference =
            FirebaseDatabase.getInstance().getReference("/chat/$toChat/message/$fromId").push()
        var chatMessage = Message()
        if (selectedPhotoUri.path =="") {
            chatMessage =
                Message(
                    reference.key!!,
                    fromId,
                    text,
                    toChat,
                    System.currentTimeMillis() / 1000,
                    "text",
                    ""
                )
            reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d("TAG", "Saved our chat message: ${reference.key}")
                    value.value = "true"
                }
            toReference.setValue(chatMessage).addOnSuccessListener {
                value.value = "true"
            }
            val latestMessageRef =
                FirebaseDatabase.getInstance().getReference("chat/$toChat/latest-messages/$fromId/")
            latestMessageRef.setValue(chatMessage).addOnSuccessListener {
                Log.d("shod", "shod")
            }


        } else {
            Log.d("image",selectedPhotoUri.path!!)
            val filename = UUID.randomUUID().toString()
            val ref =
                FirebaseStorage.getInstance().getReference("chat/$toChat/image_messages/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("TAG", "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("TAG", "File Location: $it")
                        chatMessage = Message(
                            reference.key!!,
                            fromId,
                            "sent photo",
                            toChat,
                            System.currentTimeMillis() / 1000,
                            "photo",
                            it.toString()

                        )
                        reference.setValue(chatMessage)
                            .addOnSuccessListener {
                                Log.d("TAG", "Saved our chat message: ${reference.key}")
                            }
                        toReference.setValue(chatMessage).addOnSuccessListener {
                            Log.d("TAG", "Saved our chat message: ${reference.key}")
                        }
                        val latestMessageRef =
                            FirebaseDatabase.getInstance()
                                .getReference("chat/$toChat/latest-messages/$fromId/")
                        latestMessageRef.setValue(chatMessage)
                        Log.d("chat", it.toString() + " 2 " + chatMessage.type)
                    }

                }
                .addOnFailureListener {
                    Log.d("TAG", "Failed to upload image to storage: ${it.message}")
                }
        }
        return value
    }

    fun listenForMessages( chat: String):MutableLiveData<Message> {
        Log.d("get","getmessage")
        var result = MutableLiveData<Message>()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = chat
        Log.d("toid",toId)
        Log.d("from",fromId.toString())
        val ref = FirebaseDatabase.getInstance().getReference("/chat/$toId/message/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d("get","getmessage")
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

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
      return result
    }

    fun addChat(fid: String): MutableLiveData<String> {
        var result = MutableLiveData<String>()
        val uid = FirebaseAuth.getInstance().currentUser.uid
        val chatId = uid.plus(fid)
        val refrence = FirebaseDatabase.getInstance().getReference("/chat/users/$chatId").key
        if (refrence == null) {
            val ref = FirebaseDatabase.getInstance().getReference("/chat/users/")
            Log.d("ref", ref.key!!)
            val chat = Chat()
            val chat1 = Chat()
            with(chat) {
                idChat = chatId
                idUSer = uid
                type = "private"
            }
            ref.setValue(chat).addOnSuccessListener {
                Log.d("TAG", "add chat")
                result.value = chatId
            }
                .addOnFailureListener {
                    Log.e("TAG", "Failed to set value to database: ${it.message}")
                    result.value = "false"
                }
            with(chat1) {
                idChat = uid.plus(fid)
                idUSer = uid
                type = "private"
            }
            ref.setValue(chat1).addOnSuccessListener {
                Log.d("TAG", "Finally we saved the user to Firebase Database")
                result.value = chatId
            }
                .addOnFailureListener {
                    Log.e("TAG", "Failed to set value to database: ${it.message}")
                    result.value = "false"
                }

        } else {
            result.value = chatId
        }
        return result
    }
}