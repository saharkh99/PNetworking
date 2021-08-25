package com.example.pnetworking.ui.groupchat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivityPrivateChatBinding
import com.example.pnetworking.models.Message
import com.example.pnetworking.ui.pchat.ChatItem
import com.example.pnetworking.ui.pchat.PrivateChatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
    lateinit var mainlayout: ConstraintLayout
    private val PERMISSION_REQUEST_CODE = 3
    val adapter = GroupAdapter<GroupieViewHolder>()
    var isText = true
    var reply = ""
    var editId = ""
    var editContext: Int = 0
    lateinit var editView: View
    var edit = false
    var type = false
    lateinit var replyView: View
    var mute = false
    var group=false
    var preMessageDate = ""
    lateinit var chatID: String
    private val mainViewModel2 by viewModel<PrivateChatViewModel>()


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = intent
        if(intent.getStringExtra("group_chat")!=null){
            chatID = intent.getStringExtra("group_chat")!!
            group=true

        }
        if(intent.getStringExtra("chat_fragment")!=null){
            chatID = intent.getStringExtra("chat_fragment")!!
        }
        init()

        mainViewModel.getGroupChat(chatID).observe(this, { g ->
            if (g.image != "") {
                Picasso.get().load(g.image).into(groupImage)
            } else
                groupImage.setImageResource(R.drawable.group)

            groupName.text = g.name
        })
        toolbar.setOnClickListener {
            GroupProfileFragment.newInstance(chatID).show(supportFragmentManager, "KEY_ID")
        }
        uploadImage()
        send.setOnClickListener {
            Log.d("TAG", "Attempt to send message....")
            isText = true
            performSendMessage(editChat.text.toString())
        }
        listenForMessages()


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
        mainlayout = binding.mainLayout
        chatRecyclerView?.adapter = adapter
        chatRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )
        setSupportActionBar(toolbar)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("RestrictedApi")
    private fun listenForMessages() {
        adapter.clear()
        if(group){
            val msg=Message()
            msg.context="the group is created!!"
            group=false
            adapter.add(0, ChatItem(this, msg, "", "group", "",""))
        }
        mainViewModel2.checkBlackList(chatID).observe(this, { blocked ->
            var userName=""
            if (blocked != true) {
                Log.d("from", blocked.toString())
                mainViewModel.listenForMessages(chatID).observe(this, { chatMessage ->
                    Log.d("from", chatMessage.id)
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val date = Date(chatMessage.timestamp * 1000)
                    mainViewModel.getNameOfUser(chatMessage.idUSer).observe(this,{name->
                        userName=name
                        Log.d("group name",userName)
                        if (preMessageDate != sdf.format(date)) {
                            adapter.add(
                                0,
                                ChatItem(this, chatMessage, "", "date", chatMessage.reply,"")
                            )
                            preMessageDate = sdf.format(date)
                        }
                        if (chatMessage.idUSer == FirebaseAuth.getInstance().uid) {
                            Log.d("from", chatMessage.context+ chatMessage.seen)
                            adapter.add(
                                0,
                                ChatItem(this, chatMessage, "", "false", chatMessage.reply,userName)
                            )
                            type = false

                        }
                        else {

                            adapter.add(
                                0,
                                ChatItem(this, chatMessage, "", "true", chatMessage.reply,userName)
                            )
                            type = true
                            mainViewModel2.seenMessage(chatID, chatMessage.id)
                                .observe(this, {
                                    Log.d("seen message", it.toString())
                                    adapter.notifyItemChanged(adapter.itemCount-1)
                                    adapter.notifyDataSetChanged()
                                })

                        }
                    })

                    adapter.setOnItemClickListener { i, view ->
                        Log.d("adapter", i.id.toString())
                        val popup = PopupMenu(this, view)
                        val msg = i as ChatItem
                        Log.d("adapter", msg.text.context)
                        popup.inflate(R.menu.options_menu)
                        if (popup is MenuBuilder) {
                            val m = popup as MenuBuilder
                            m.setOptionalIconsVisible(true)
                        }
                        if (i.text.idUSer == FirebaseAuth.getInstance().uid)
                            popup.gravity = Gravity.END
                        else
                            popup.gravity = Gravity.START
                        popup.setForceShowIcon(true)
                        if (i.text.idUSer != FirebaseAuth.getInstance().uid) {
                            popup.menu.getItem(1).isVisible = false
                            popup.menu.getItem(2).isVisible = false
                        }
                        popup.setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.edit -> {
                                    item.icon = getDrawable(R.drawable.edit)
                                    view.setBackgroundColor(Color.parseColor("#33efe6fa"))
                                    editChat.setText(i.text.context)
                                    edit = true
                                    editId = i.text.id
                                    editView = view
                                    Log.d("edit1", adapter.getAdapterPosition(i).toString())
                                    editContext = adapter.getAdapterPosition(i)
                                    true
                                }
                                R.id.delete -> {
                                    Log.d(
                                        "message",
                                        i.text.idUSer + " " + FirebaseAuth.getInstance().uid
                                    )
                                    if (i.text.idUSer == FirebaseAuth.getInstance().uid) {
                                        Log.d("message", i.text.context)
                                        mainViewModel.removeMessage(chatID, i.text.id)
                                        adapter.notifyDataSetChanged()
                                        adapter.remove(i)
                                        Toast.makeText(
                                            this,
                                            " massage is deleted",
                                            Toast.LENGTH_SHORT
                                        )
                                    } else
                                        item.isVisible = false
                                    true
                                }
                                R.id.reply -> {
                                    view.setBackgroundColor(Color.parseColor("#33efe6fa"))
                                    reply = msg.text.context
                                    replyView = view
                                    true
                                }
                                else -> false
                            }
                        }
                        popup.show()
                    }
                })
            }
        })

    }

    private fun uploadImage() {
        imageSend.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    FilePickerBuilder.instance
                        .setMaxCount(5) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickPhoto(this, 0)

                } else {
                    requestPermission()
                }
            }
        }
    }

    var selectedPhotoUri: ArrayList<Uri>? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("TAG", "Photo was selected")
            val photoPaths = ArrayList<Uri>()
            photoPaths.addAll(data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)!!)
            selectedPhotoUri = photoPaths
            isText = false
            Log.d("photo", photoPaths.toString())
            performSendMessage("sent photo")
        }
    }

    private fun performSendMessage(text: String) {
        if (selectedPhotoUri == null)
            selectedPhotoUri = ArrayList()
        if (text != "") {
            if (edit && editId != "") {
                Log.d("edit", editId)
                mainViewModel.editMessage(editId, chatID, text)
                editId = ""
                editChat.setText("")
                editView.setBackgroundColor(0)
                Log.d("edit", editContext.toString())
                adapter.notifyDataSetChanged()
                adapter.notifyItemChanged(editContext)
                hideKeyboardFrom(this, mainlayout)
            } else {
                mainViewModel.performSendMessage(
                    text,
                    chatID,
                    selectedPhotoUri!!,
                    reply,
                    chatID,
                    isText
                ).observe(
                    this,
                    {
                        Log.d("send message", it)
                        if (it == "true") {
                            editChat.setText("")
                            var msg = Message()
                            msg.context = text
                            mainViewModel2.checkMuteList(chatID).observe(this, { mute ->
                                if (!mute) {
                                    mainViewModel2.sendNotification(msg, chatID, true)
                                }
                            })
//                            calculate number of new messages
                            if (reply != "") {
                                replyView.setBackgroundColor(0)
                                reply = ""
                            }
                            hideKeyboardFrom(this, mainlayout)
                        } else {
                            Log.d("send message", "try again")
                        }
                    })
            }
        }


    }


    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this,
                "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .")
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .")
            }
        }
    }
}