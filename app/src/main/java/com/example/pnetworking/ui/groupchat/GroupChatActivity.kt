package com.example.pnetworking.ui.groupchat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivityPrivateChatBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.viewmodel.ext.android.viewModel

class GroupChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrivateChatBinding
    private val mainViewModel by viewModel<GroupViewModel>()
    lateinit var groupName: TextView
    lateinit var groupImage: CircleImageView
    lateinit var toolbar: Toolbar
    lateinit var editChat: EditText
    lateinit var imageSend: ImageButton
    lateinit var send: ImageButton
    lateinit var chatRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = intent
        val chatID = intent.getStringExtra("group_chat")
        init()

        mainViewModel.getGroupChat(chatID!!).observe(this, { g ->
            if (g.image != "") {
                Picasso.get().load(g.image).into(groupImage)
            } else
                groupImage.setImageResource(R.drawable.user)

            groupName.text = g.name
        })
        toolbar.setOnClickListener {
            GroupProfileFragment.newInstance(chatID!!).show(supportFragmentManager, "KEY_ID")
        }

    }
    override fun onDestroy() {
        Log.w("TAG", "group destroyed")
        super.onDestroy()
    }

    private fun init() {
        toolbar = binding.chatToolbar
        groupImage = binding.chatImage
        groupName = binding.chatName
        send = binding.chatSendMessageButton
        chatRecyclerView = binding.chatRecyclerViewsMessages
        editChat = binding.chatMessageBoxEt
        imageSend = binding.chatAttachmentButton

    }
    fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}