package com.example.pnetworking.ui.pchat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivityPrivateChatBinding
import com.example.pnetworking.models.Message
import com.example.pnetworking.utils.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.paulrybitskyi.persistentsearchview.PersistentSearchView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst.KEY_SELECTED_MEDIA
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PrivateChat : ChatActivity() {
    //-seen-new message-typing-docs
    var username = ""
    var userImage = ""
    var chatId = ""
    var userId = ""
    var reply = ""
    var editId = ""
    var edit = false
    var type = false
    var isText = true
    var mute = false
    var preMessageDate = ""
    var editContext:Int=0
    lateinit var editView:View
    lateinit var replyView:View
    lateinit var nameTextView: TextView
    lateinit var imageCircle: CircleImageView
    lateinit var toolbar: Toolbar
    lateinit var send: ImageButton
    lateinit var editChat: EditText
    lateinit var chatMute: ImageView
    lateinit var imageSend: ImageButton
    lateinit var mainlayout: ConstraintLayout
    lateinit var binding: ActivityPrivateChatBinding
    lateinit var chatSearch: PersistentSearchView
    val adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var chatRecyclerView: RecyclerView
    private val PERMISSION_REQUEST_CODE = 1
    private val mainViewModel by viewModel<PrivateChateViewModel>()


    @RequiresApi(Build.VERSION_CODES.Q)
    @ExperimentalStdlibApi
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
            isText = true
            performSendMessage(editChat.text.toString())
        }
        mainViewModel.addChat(userId).observe(this, {
            Log.d("add chat", it)
            if (it != "false")
                chatId = it
            listenForMessages()
        })
        changingTypingStatus()
    }

    private fun changingTypingStatus() {
        editChat.doOnTextChanged { text, start, before, count ->

        }
        editChat.doAfterTextChanged {

        }
    }

    private fun performSendMessage(text: String) {

        if (selectedPhotoUri == null)
            selectedPhotoUri = ArrayList()
        if (text != "") {
            if (edit && editId != "") {
                Log.d("edit",editId)
                mainViewModel.editMessage(editId, chatId, text)
                editId = ""
                editChat.setText("")
                editView.setBackgroundColor(0)
                Log.d("edit",editContext.toString())
                adapter.notifyDataSetChanged()
                adapter.notifyItemChanged(editContext)
                hideKeyboardFrom(this, mainlayout)
            } else {
                mainViewModel.performSendMessage(
                    text,
                    chatId,
                    selectedPhotoUri!!,
                    reply,
                    userId,
                    isText
                ).observe(
                    this,
                    {
                        Log.d("send message", it)
                        if (it == "true") {
                            editChat.setText("")
                            var msg = Message()
                            msg.context = text
                            if (!mute)
//                            calculate number of new messages
                                mainViewModel.sendNotification(msg, chatId, true)
                            if(reply!=""){
                                replyView.setBackgroundColor(0)
                                reply=""
                            }
                            hideKeyboardFrom(this, mainlayout)
                        } else {
                            Log.d("send message", "try again")
                        }
                    })
            }
        }

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
            photoPaths.addAll(data.getParcelableArrayListExtra<Uri>(KEY_SELECTED_MEDIA)!!)
            selectedPhotoUri = photoPaths
            isText = false
            Log.d("photo", photoPaths.toString())
            performSendMessage("sent photo")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("RestrictedApi")
    private fun listenForMessages() {
        adapter.clear()
        mainViewModel.checkBlackList(chatId).observe(this, { blocked ->
            if (blocked != true) {
                Log.d("from", blocked.toString())
                mainViewModel.listenForMessages(chatId).observe(this, { chatMessage ->
                    Log.d("from", chatMessage.id)
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val date = Date(chatMessage.timestamp * 1000)
                    if (preMessageDate != sdf.format(date)) {
                        adapter.add(
                            0,
                            ChatItem(this, chatMessage, userImage, "date", chatMessage.reply)
                        )
                        preMessageDate = sdf.format(date)
                    }
                    if (chatMessage.idUSer == FirebaseAuth.getInstance().uid) {
                        adapter.add(
                            0,
                            ChatItem(this, chatMessage, userImage, "false", chatMessage.reply)
                        )
                        type = false

                    } else {
                        adapter.add(
                            0,
                            ChatItem(this, chatMessage, userImage, "true", chatMessage.reply)
                        )
                        type = true
                        mainViewModel.seenMessage(chatId, chatMessage.id)
                            .observe(this@PrivateChat, {
                                Log.d("get it", it.toString())
                            })
                    }
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
                                    editView=view
                                    Log.d("edit1",adapter.getAdapterPosition(i).toString())
                                    editContext=adapter.getAdapterPosition(i)
                                    true
                                }
                                R.id.delete -> {
                                    Log.d(
                                        "message",
                                        i.text.idUSer + " " + FirebaseAuth.getInstance().uid
                                    )
                                    if (i.text.idUSer == FirebaseAuth.getInstance().uid) {
                                        Log.d("message", i.text.context)
                                        mainViewModel.removeMessage(chatId, i.text.id)
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
                                    replyView=view
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

    @ExperimentalStdlibApi
    private fun init() {
        toolbar = binding.chatToolbar
        imageCircle = binding.chatImage
        nameTextView = binding.chatName
        send = binding.chatSendMessageButton
        chatRecyclerView = binding.chatRecyclerViewsMessages
        editChat = binding.chatMessageBoxEt
        mainlayout = binding.mainLayout
        imageSend = binding.chatAttachmentButton
        chatSearch = binding.chatSearchMessage
        if (userImage != "true")
            Picasso.get().load(Uri.parse(userImage)).into(imageCircle)
        else
            imageCircle.setImageResource(R.drawable.user)
        nameTextView.text = username.uppercase()
        chatRecyclerView?.adapter = adapter
        chatRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )
        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.private_chat_menu)
        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.more)
        chatRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount =
                    (chatRecyclerView.layoutManager as LinearLayoutManager).childCount
                val totalItemCount =
                    (chatRecyclerView.layoutManager as LinearLayoutManager).itemCount
                val firstVisibleItemPosition =
                    (chatRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
//                    if (!type)
//                        mainViewModel.seenMessage(chatId, userId)
//                            .observe(this@PrivateChat, {
//                                Log.d("getit", it.toString())
//                            })
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.private_chat_menu, menu)
        return true
    }

    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.block -> {
                mainViewModel.addToBlackList(chatId, userId).observe(this, {
                    if (it) {
                        item?.title = "blocked"
                        Toast.makeText(this, "blocked successfully", Toast.LENGTH_SHORT)

                    }
                })
                return true
            }
            R.id.mute -> {
                Log.d("x", item?.title.toString())
                if (item?.title == "mute") {
                    item.icon = getDrawable(R.drawable.unmute)
                    item?.title = "unmute"
                    mute = false
                } else {
                    item?.title = "mute"
                    item?.icon = getDrawable(R.drawable.mute)
                    mute = true
                }
                return true
            }
            R.id.search -> {
                val l = ArrayList<Message>()
                chatSearch.visibility = View.VISIBLE
                with(chatSearch) {
                    setOnLeftBtnClickListener {
                        onBackPressed()
                    }
                    setOnClearInputBtnClickListener {
                        l.clear()
                    }

                    setOnSearchConfirmedListener { searchView, query ->
                        searchView.collapse()
                        searchView.onCancelPendingInputEvents()
                        Log.d("has it?", query)
                        for (i in 0 until chatRecyclerView.adapter!!.itemCount) {
                            val msg = adapter.getItem(i) as ChatItem
                            Log.d("has it?", msg.text.context.lowercase())
                            if (msg.text.context.lowercase().contains(query.lowercase())) {
                                Log.d("has it?", "yes")
                                chatRecyclerView.getChildAt(i)
                                    .setBackgroundColor(Color.parseColor("#33efe6fa"))
                            }
                        }


                    }
                    setSuggestionsDisabled(true)

                }



                return true
            }
        }
        return true

    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
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

    @SuppressLint("RestrictedApi")
    override fun onPrepareOptionsPanel(view: View?, menu: Menu): Boolean {
        if (menu != null) {
            if (menu.javaClass.simpleName == "MenuBuilder") {
                try {
                    val m = menu.javaClass.getDeclaredMethod(
                        "setOptionalIconsVisible", java.lang.Boolean.TYPE
                    )
                    m.isAccessible = true
                    m.invoke(menu, true)
                } catch (e: java.lang.Exception) {
                    Log.e(
                        javaClass.simpleName,
                        "onMenuOpened...unable to set icons for overflow menu",
                        e
                    )
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu)
    }
}