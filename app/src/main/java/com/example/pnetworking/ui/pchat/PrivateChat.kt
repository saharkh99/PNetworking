package com.example.pnetworking.ui.pchat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivityPrivateChatBinding
import com.example.pnetworking.ui.profile.ProfileViewModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.viewmodel.ext.android.viewModel

class PrivateChat : AppCompatActivity() {
    //(send message and receive with image and notification-notification)- image-recent message-delete/edit
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
        listenForMessages(editChat.text.toString())
        uploadImage()
        send.setOnClickListener {
            Log.d("TAG", "Attempt to send message....")
            performSendMessage(editChat.text.toString())
        }
        mainViewModel.addChat(userId).observe(this,{
            Log.d("add chat",it)
            if(it!="false")
                chatId=it
        })
    }

    private fun performSendMessage(text: String) {
       mainViewModel.performSendMessage(text,chatId,selectedPhotoUri!!).observe(this,{
           Log.d("send message",it)
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

    private fun listenForMessages(text: String) {
        
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