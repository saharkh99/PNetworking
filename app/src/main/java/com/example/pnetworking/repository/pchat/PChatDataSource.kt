package com.example.pnetworking.repository.pchat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Chat
import com.example.pnetworking.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PChatDataSource {
    fun performSendMessage(
        text: String,
        chat: String,
        selectedPhotoUri: ArrayList<Uri>,
        reply: String,
        toID: String,
        isText: Boolean
    ): MutableLiveData<String> {
        val value = MutableLiveData<String>()
        val fromId = FirebaseAuth.getInstance().uid
        var toChat = ""
        if (chat != null) {
            toChat = chat
        }
        if (fromId == null) return value
        val reference =
            FirebaseDatabase.getInstance().getReference("/user_message/$fromId/$toID").push()
        val reference2= FirebaseDatabase.getInstance().getReference("/user_message/$toID/$fromId").push()
        val toReference =
            FirebaseDatabase.getInstance().getReference("/chat/$toChat/message/$fromId").push()
        val fid = toChat.removePrefix(fromId)
        val toId2 = fid + fromId
        Log.d("from", toId2)
        val ref2 = FirebaseDatabase.getInstance().getReference("/chat/$toId2/message/$fid").push()
        var chatMessage = Message()
        Log.d("time", System.currentTimeMillis().toString())
        if (isText) {
            chatMessage =
                Message(
                    ref2.key!!,
                    fromId,
                    text,
                    toChat,
                    System.currentTimeMillis() / 1000,
                    "text",
                    "",
                    reply,
                    false
                )
            reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d("TAG", "Saved our chat message: ${reference.key}")
                    value.value = "true"
                }
            toReference.setValue(chatMessage).addOnSuccessListener {
                value.value = "true"
            }
            ref2.setValue(chatMessage).addOnSuccessListener {
                value.value = "true"
            }
            reference2.setValue(chatMessage).addOnSuccessListener {
                value.value = "true"
            }

            val latestMessageRef =
                FirebaseDatabase.getInstance().getReference("chat/latest-messages/$fromId/$toID")
            latestMessageRef.setValue(chatMessage).addOnSuccessListener {
                Log.d("shod", "shod")
            }
            val latestMessageRef2 =
                FirebaseDatabase.getInstance().getReference("chat/latest-messages/$toID/$fromId")
            latestMessageRef2.setValue(chatMessage).addOnSuccessListener {
                Log.d("shod", "shod")
            }


        } else {
            for (i in selectedPhotoUri) {
                Log.d("image", i.path!!)
                val filename = UUID.randomUUID().toString()
                val ref =
                    FirebaseStorage.getInstance()
                        .getReference("chat/$toChat/image_messages/$filename")
                ref.putFile(i)
                    .addOnSuccessListener {
                        Log.d("TAG", "Successfully uploaded image: ${it.metadata?.path}")

                        ref.downloadUrl.addOnSuccessListener {
                            Log.d("TAG", "File Location: $it")
                            chatMessage = Message(
                                ref2.key!!,
                                fromId,
                                "sent photo",
                                toChat,
                                System.currentTimeMillis() / 1000,
                                "photo",
                                it.toString(),
                                "",
                                false

                            )
                            reference.setValue(chatMessage)
                                .addOnSuccessListener {
                                    Log.d("TAG", "Saved our chat message: ${reference.key}")
                                }
                            toReference.setValue(chatMessage).addOnSuccessListener {
                                Log.d("TAG", "Saved our chat message: ${reference.key}")
                            }
                            ref2.setValue(chatMessage).addOnSuccessListener {
                                Log.d("TAG", "Saved our chat message: ${reference.key}")
                            }
                            val latestMessageRef =
                                FirebaseDatabase.getInstance()
                                    .getReference("chat/latest-messages/$fromId/$toID")
                            latestMessageRef.setValue(chatMessage).addOnSuccessListener {
                                Log.d("shod", "shod")
                            }
                            val latestMessageRef2 =
                                FirebaseDatabase.getInstance()
                                    .getReference("chat/latest-messages/$toID/$fromId")
                            latestMessageRef2.setValue(chatMessage).addOnSuccessListener {
                                Log.d("shod", "shod")
                            }
                        }

                    }
                    .addOnFailureListener {
                        Log.d("TAG", "Failed to upload image to storage: ${it.message}")
                    }
            }

        }
        return value
    }

    fun listenForMessages(chat: String): MutableLiveData<Message> {
        Log.d("get", "getmessage")
        val result = MutableLiveData<Message>()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = chat
        Log.d("toid", toId)
        Log.d("from", fromId.toString())
        val fid = toId.removePrefix(fromId!!)
        val toId2 = fid + fromId
        Log.d("from", toId2)
        val ref2 = FirebaseDatabase.getInstance().getReference("/chat/$toId2/message/$fid")
        ref2.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d("get", "getmessage")
                val chatMessage = p0.getValue(Message::class.java)
                if (chatMessage != null) {
                    Log.d("TAG1", chatMessage.id)
                    result.value = chatMessage
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
        val result = MutableLiveData<String>()
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

    fun removeMessage(toChat: String, msgId: String) {
        val fromId = FirebaseAuth.getInstance().uid
        Log.d("delete", msgId)
        val fid = toChat.removePrefix(fromId!!)
        val toId2 = fid + fromId
        Log.d("from", toId2)
        val ref = FirebaseDatabase.getInstance()
            .getReference("/chat/$toChat/message/$fromId")
        val ref3 = FirebaseDatabase.getInstance()
            .getReference("/chat/$toId2/message/$fid")
        ref.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.child("id").value == msgId) {
                        Log.d("delete44", it.ref.toString())
                        it.ref.removeValue()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        ref3.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.child("id").value == msgId) {
                        Log.d("delete44", it.ref.toString())
                        it.ref.removeValue()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        val latestMessageRef =
            FirebaseDatabase.getInstance()
                .getReference("chat/$toChat/latest-messages/$fromId/")
        latestMessageRef.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.child("id").value == msgId) {
                        Log.d("delete44", it.ref.toString())
                        it.ref.removeValue()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun editMessage(msgId: String, toChat: String, text: String) {
        val fromId = FirebaseAuth.getInstance().uid
        val fid = toChat.removePrefix(fromId!!)
        val toId2 = fid + fromId
        val ref = FirebaseDatabase.getInstance()
            .getReference("/chat/$toChat/message/$fromId")
        val ref3 = FirebaseDatabase.getInstance()
            .getReference("/chat/$toId2/message/$fid")
        ref.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    Log.d("edit server", it.child("id").value.toString())
                    Log.d("edit server", msgId)
                    if (it.child("id").value == msgId) {
                        Log.d("edit server", it.ref.toString())
                        val hashmap = HashMap<String, Any>()
                        hashmap["context"] = text
                        it.ref.updateChildren(hashmap)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        ref3.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.child("id").value == msgId) {
                        val hashmap = HashMap<String, Any>()
                        hashmap["context"] = text
                        it.ref.updateChildren(hashmap)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    fun seenMessage(toChat: String, messageId: String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val fromId = FirebaseAuth.getInstance().uid
        val toId=toChat.replace(fromId!!,"")
        val toId2 = toId + fromId
        val ref = FirebaseDatabase.getInstance().getReference("/chat/$toChat/message/$fromId")
        val ref2 = FirebaseDatabase.getInstance().getReference("/chat/$toId2/message/$toId")
        val hashmap = HashMap<String, Any>()
        hashmap["seen"] = true
        ref.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.child("id").value == messageId) {
                        ref.child(it.key.toString()).updateChildren(hashmap)
                        result.value=true
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
        ref2.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.child("id").value == messageId) {
                        Log.d("seen3",it.key.toString())
                        ref2.child(it.key.toString()).updateChildren(hashmap)
                        result.value=true
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
        return result
    }

    fun addToBlackList(toChat: String, userId: String): MutableLiveData<Boolean> {
        val value = MutableLiveData<Boolean>()
        value.value = false
        val fromId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("users/$fromId/black_lists/$toChat")
        reference.setValue(toChat)
            .addOnSuccessListener {
                Log.d("TAG", "Saved our chat message: ${reference.key}")
                value.value = true
            }
        return value
    }

    fun removeFromBlackList(toChat: String): MutableLiveData<Boolean> {
        val value = MutableLiveData<Boolean>()
        value.value = false
        val fromId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("users/$fromId/black_lists/$toChat")
        reference.removeValue().addOnSuccessListener {
            Log.d("TAG", "Saved our chat message: ${reference.key}")
            value.value = true
        }
        return value
    }

    fun checkBlackList(toChat: String): MutableLiveData<Boolean> {
        val value = MutableLiveData<Boolean>()
        val fromId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("users/$fromId/black_lists/$toChat")
                .child(toChat)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value.value = false

                if (snapshot.value != null) {
                    Log.d("TAG33333333", " ${snapshot.value}")
                    value.value = true
                    Log.d("TAG33333333", " ${value.value}")

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return value
    }

    fun addToMuteList(toChat: String): MutableLiveData<Boolean> {
        val value = MutableLiveData<Boolean>()
        value.value = false
        val fromId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("users/$fromId/mute_list/$toChat")
        reference.setValue(toChat)
            .addOnSuccessListener {
                Log.d("TAG", "Saved our chat message: ${reference.key}")
                value.value = true
            }
        return value
    }

    fun removeFromMuteList(toChat: String): MutableLiveData<Boolean> {
        val value = MutableLiveData<Boolean>()
        value.value = false
        val fromId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("users/$fromId/mute_list/$toChat")
        reference.removeValue().addOnSuccessListener {
            Log.d("TAG", "Saved our chat message: ${reference.key}")
            value.value = true
        }
        return value
    }
    fun IsInMuteList(toChat: String): MutableLiveData<Boolean> {
        val value = MutableLiveData<Boolean>()
        value.value = false
        val fromId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("users/$fromId/mute_list/$toChat")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value.value = false
                if (snapshot.value != null) {
                    Log.d("TAG33333333", " ${snapshot.value}")
                    value.value = true
                    Log.d("TAG33333333", " ${value.value}")

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
        return value
    }

    fun checkMuteList(toChat: String): MutableLiveData<Boolean> {
        var value = MutableLiveData<Boolean>()
        val fromId = FirebaseAuth.getInstance().uid
        val fid = toChat.replace(fromId!!,"")
        Log.d("TAG33333333", "$fid")
        val reference =
            FirebaseDatabase.getInstance().getReference("users/$fid/mute_list/$toChat")
                .child(toChat)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value.value = false

                if (snapshot.value != null) {
                    Log.d("TAG33333333", " ${snapshot.value}")
                    value.value = true
                    Log.d("TAG33333333", " ${value.value}")

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return value
    }

    fun numberOfNewMessages(toChat: String): MutableLiveData<HashMap<String, Any>> {
        var result = MutableLiveData<HashMap<String, Any>>()
        var total = 0
        val hashmap = HashMap<String, Any>()
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/user_message/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.children.forEach {
                    val chatMessage = it.getValue(Message::class.java)
                    if (!chatMessage!!.seen) {
                        total++
                    }
                }
                Log.d("total hashmap", hashmap.keys.toString())
                hashmap[snapshot.key!!.removePrefix(fromId.toString())] = total
                total = 0
                result.value = hashmap

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
        return result
    }

}