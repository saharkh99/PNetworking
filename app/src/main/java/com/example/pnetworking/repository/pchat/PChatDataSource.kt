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

     fun performSendMessage(text: String, user: User, chat: Chat, selectedPhotoUri: Uri) {
        val value = MutableLiveData<Boolean>()
        val fromId = FirebaseAuth.getInstance().uid
        var toChat = ""
        if (chat != null) {
            toChat = chat.idChat
        }
        if (fromId == null) return
        val reference =
            FirebaseDatabase.getInstance().getReference("/user_message/$fromId/$toChat").push()
        val toReference =
            FirebaseDatabase.getInstance().getReference("/chat/$toChat/$fromId").push()
        var chatMessage = Message()
        if (selectedPhotoUri == null) {
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
                    value.value = true
                }
            toReference.setValue(chatMessage).addOnSuccessListener {
                value.value = true
            }
            val latestMessageRef =
                FirebaseDatabase.getInstance().getReference("chat/$toChat/latest-messages/$fromId/")
            latestMessageRef.setValue(chatMessage).addOnSuccessListener {
                Log.d("shod", "shod")
            }


        } else {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/image_messages/$filename")
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

    }
     fun listenForMessages(text: String, chat: Chat) {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = chat?.idChat
        val ref = FirebaseDatabase.getInstance().getReference("/chat/$toId/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(Message::class.java)
                if (chatMessage != null) {
                    Log.d("TAG", chatMessage.idUSer)
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

    }
}