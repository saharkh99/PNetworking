package com.example.pnetworking.repository.group

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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

        Log.d("image",image.path!!)
        if(image.path.isNullOrEmpty()){
            val ref =
                FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId")
            val gChat = GroupChat()
            gChat.idChat = chatId
            gChat.idUSer = uid
            gChat.image = ""
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
            val ref3 =
                FirebaseDatabase.getInstance().getReference("/users/$uid/groups/$pid")
            ref3.setValue(chatId).addOnSuccessListener {
                Log.d("TAG", "Saved our chat message: ${ref.key}")
            }
            val refrence = FirebaseDatabase.getInstance().getReference("/chat/users/$chatId")
            if (refrence == null) {
                val ref = FirebaseDatabase.getInstance().getReference("/chat/users/")
                Log.d("ref", ref.key!!)
                val chat = Chat()
                with(chat) {
                    idChat = chatId
                    idUSer = uid
                    type = "group"
                }
                refrence.setValue(chat).addOnSuccessListener {
                    Log.d("TAG", "add chat")
                    result.value = chatId
                }
                    .addOnFailureListener {
                        Log.e("TAG", "Failed to set value to database: ${it.message}")
                        result.value = "false"
                    }
            }


        }
        else{
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

                val refrence = FirebaseDatabase.getInstance().getReference("/chat/users/$chatId")
                if (refrence == null) {
                    val ref = FirebaseDatabase.getInstance().getReference("/chat/users/")
                    Log.d("ref", ref.key!!)
                    val chat = Chat()
                    with(chat) {
                        idChat = chatId
                        idUSer = uid
                        type = "group"
                    }
                    refrence.setValue(chat).addOnSuccessListener {
                        Log.d("TAG", "add chat")
                        result.value = chatId
                    }
                        .addOnFailureListener {
                            Log.e("TAG", "Failed to set value to database: ${it.message}")
                            result.value = "false"
                        }
                }

            }
        }

        return result
    }
    fun addAParticipant(user: User, chatId: String) {
        val pid=UUID.randomUUID().toString()
        val ref =
            FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId/participants/$pid")
        val ref2 =
            FirebaseDatabase.getInstance().getReference("/users/${user.id}/groups/$pid")

        Log.d("pid",user.id)
        val par = Participant()
        par.idChat = chatId
        par.idUSer = user.id
        par.role = "typical"
        ref.setValue(par)
            .addOnSuccessListener {
                Log.d("TAG", "Saved our chat message: ${ref.key}")
            }
        ref2.setValue(chatId).addOnSuccessListener {
            Log.d("TAG group", "$pid")
        }

    }
    fun getGroupChat(chatId: String): MutableLiveData<GroupChat> {
        val result = MutableLiveData<GroupChat>()
        val ref =
            FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val g=GroupChat(p0.child("idUser").value.toString()
                ,p0.child("idChat").value.toString()
                ,p0.child("type").value.toString()
                ,p0.child("name").value.toString()
                ,p0.child("image").value.toString()
                ,p0.child("summery").value.toString()
                ,"")
                Log.d("NewMessage",p0.child("summery").value.toString())
                Log.d("NewMessage",p0.child("image").value.toString())
                if (g != null) {
                    result.value = g
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
    fun editGroup(name: String, bio: String,chatId: String): MutableLiveData<Boolean> {
        var result = MutableLiveData<Boolean>()
        val hashmap = HashMap<String, Any>()
        hashmap["name"] = name
        hashmap["summery"] = bio
        val ref = FirebaseDatabase.getInstance().getReference("/chat/group_chat/$chatId")
        ref.updateChildren(hashmap).addOnSuccessListener {
            result.value = true
        }.addOnFailureListener {
            result.value = false
        }
        return result
    }
    fun listenForMessages(chat: String): MutableLiveData<Message> {
        Log.d("get", "getmessage")
        val result = MutableLiveData<Message>()
        val ref2 = FirebaseDatabase.getInstance().getReference("/chat/$chat/message/")
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
            FirebaseDatabase.getInstance().getReference("/user_message/$toID/").push()
        val reference2= FirebaseDatabase.getInstance().getReference("/user_message/$toID/").push()
        val toReference =
            FirebaseDatabase.getInstance().getReference("/chat/$toChat/message/").push()
        var chatMessage = Message()
        Log.d("time", System.currentTimeMillis().toString())
        if (isText) {
            chatMessage =
                Message(
                    reference.key!!,
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
            reference2.setValue(chatMessage).addOnSuccessListener {
                value.value = "true"
            }

            val latestMessageRef2 =
                FirebaseDatabase.getInstance().getReference("chat/latest-messages/$toID/")
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
                                reference.key!!,
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
                            val latestMessageRef2 =
                                FirebaseDatabase.getInstance()
                                    .getReference("chat/latest-messages/$toID/")
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


}