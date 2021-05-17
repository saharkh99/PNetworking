package com.example.pnetworking.ui.pchat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivityPrivateChatBinding
import com.example.pnetworking.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.viewmodel.ext.android.viewModel

class PrivateChat : AppCompatActivity() {
    //image(attachment)-notification-delete-reply-seen-new message-typing
    var username = ""
    var userImage = ""
    var chatId=""
    var userId = ""
    lateinit var nameTextView: TextView
    lateinit var imageCircle: CircleImageView
    lateinit var toolbar: Toolbar
    lateinit var send:ImageButton
    lateinit var editChat:EditText
    lateinit var imageSend:ImageButton
    lateinit var binding: ActivityPrivateChatBinding
    val adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var chatRecyclerView: RecyclerView
    private val mainViewModel by viewModel<PrivateChateViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        username = intent.getStringExtra("KEY_USER")!!
        userImage = intent.getStringExtra("KEY_USER2")!!
        userId = intent.getStringExtra("KEY_USER3")!!
        init()

        uploadImage()
        send.setOnClickListener {
            Log.d("TAG", "Attempt to send message....")
            performSendMessage(editChat.text.toString())
        }
        mainViewModel.addChat(userId).observe(this,{
            Log.d("add chat",it)
            if(it!="false")
                chatId=it
            listenForMessages()
        })
    }

    private fun performSendMessage(text: String) {
        if(selectedPhotoUri==null)
            selectedPhotoUri=Uri.parse("")
        Log.d("selected",selectedPhotoUri?.path!!)
       mainViewModel.performSendMessage(text,chatId,selectedPhotoUri!!).observe(this,{
           Log.d("send message",it)
           if(it=="true"){
               editChat.setText("")
               var msg= Message()
               msg.context=text
               mainViewModel.sendNotification(msg,chatId,true)
           }
           else{
               Log.d("send message","try again")
           }
       })
    }

    private fun uploadImage() {
        imageSend.setOnClickListener {
            Log.d("1", "1")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("TAG", "Photo was selected")
            selectedPhotoUri = data.data
            Log.d("chat",selectedPhotoUri.toString()+"1")

        }
    }

    private fun listenForMessages() {
        mainViewModel.listenForMessages(chatId).observe(this,{chatMessage->
            Log.d("from", "1")
            if (chatMessage.idUSer == FirebaseAuth.getInstance().uid) {
                adapter.add(0,ChatItem(chatMessage,userImage, false))
            } else {
                adapter.add(0,ChatItem(chatMessage,userImage,true))
            }
        })
    }

    private fun init() {
        toolbar = binding.chatToolbar
        imageCircle = binding.chatImage
        nameTextView = binding.chatName
        send=binding.chatSendMessageButton
        chatRecyclerView=binding.chatRecyclerViewsMessages
        editChat=binding.chatMessageBoxEt
        imageSend=binding.chatAttachmentButton
        if (userImage != "true")
            Picasso.get().load(Uri.parse(userImage)).into(imageCircle)
        else
            imageCircle.setImageResource(R.drawable.user)
        nameTextView.text = username
        chatRecyclerView?.adapter = adapter
        chatRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )
    }
}