package com.example.pnetworking.ui.pchat

import android.Manifest
import android.R.menu
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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivityPrivateChatBinding
import com.example.pnetworking.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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


class PrivateChat : AppCompatActivity() {
    //-seen-new message-typing-docs
    var username = ""
    var userImage = ""
    var chatId = ""
    var userId = ""
    var reply = ""
    var edit = false
    var type = false
    var isText=true
    var mute=false
    var preMessageDate=""
    lateinit var refrence: FirebaseDatabase
    lateinit var seenListener: ValueEventListener
    lateinit var nameTextView: TextView
    lateinit var imageCircle: CircleImageView
    lateinit var toolbar: Toolbar
    lateinit var send: ImageButton
    lateinit var editChat: EditText
    lateinit var chatMute:ImageView
    lateinit var imageSend: ImageButton
    lateinit var mainlayout: ConstraintLayout
    lateinit var binding: ActivityPrivateChatBinding
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
            isText=true
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
            selectedPhotoUri=ArrayList()
//        Log.d("selected", selectedPhotoUri?.path!!)
        if (edit) {
            mainViewModel.editMessage(text, chatId)
        } else {
            mainViewModel.performSendMessage(text, chatId, selectedPhotoUri!!, reply, userId,isText).observe(
                this,
                {
                    Log.d("send message", it)
                    if (it == "true") {
                        editChat.setText("")
                        var msg = Message()
                        msg.context = text
                        if(!mute)
//                            calculate number of new messages
                             mainViewModel.sendNotification(msg, chatId, true)

                        hideKeyboardFrom(this, mainlayout)
                    } else {
                        Log.d("send message", "try again")
                    }
                })
        }


    }

    private fun uploadImage() {
        imageSend.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
//                    val intent = Intent(Intent.ACTION_PICK)
//                    intent.type = "image/*"
//                    startActivityForResult(intent, 0)
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
            selectedPhotoUri=photoPaths
            isText=false
            Log.d("photo", photoPaths.toString())
            performSendMessage("sent photo")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("RestrictedApi")
    private fun listenForMessages() {
        adapter.clear()
        mainViewModel.listenForMessages(chatId).observe(this, { chatMessage ->
            Log.d("from", chatMessage.id)
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val date = Date(chatMessage.timestamp * 1000)
            if (preMessageDate != sdf.format(date)) {
                adapter.add(0, ChatItem(this, chatMessage, userImage, "date", chatMessage.reply))
                preMessageDate = sdf.format(date)
            }
            if (chatMessage.idUSer == FirebaseAuth.getInstance().uid) {
                adapter.add(0, ChatItem(this, chatMessage, userImage, "false", chatMessage.reply))
                type = false

            } else {
                adapter.add(0, ChatItem(this, chatMessage, userImage, "true", chatMessage.reply))
                type = true
                mainViewModel.seenMessage(chatId, chatMessage.id)
                    .observe(this@PrivateChat, {
                        Log.d("get it", it.toString())
                    })

            }

            adapter.notifyDataSetChanged()
            adapter.setOnItemClickListener { i, view ->
                val popup = PopupMenu(this, view)
                val msg = i as ChatItem
                popup.inflate(R.menu.options_menu)
                if (popup is MenuBuilder) {
                    val m = popup as MenuBuilder
                    m.setOptionalIconsVisible(true)
                }
                popup.gravity=Gravity.RIGHT
                popup.setForceShowIcon(true)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit -> {
                            item.icon=getDrawable(R.drawable.edit)
                            view.setBackgroundColor(Color.parseColor("#000000"))
                            edit = true
                            true
                        }
                        R.id.delete -> {
                            if (!type)
                                mainViewModel.removeMessage(chatId, msg.id.toString())
                            true
                        }
                        R.id.reply -> {
                            view.setBackgroundColor(Color.parseColor("#000000"))
                            reply = msg.text.context
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.block -> {
                mainViewModel.addToBlackList(chatId).observe(this,{
                    if(it){
                        item?.title = "blocked"
                        Toast.makeText(this,"blocked successfully" ,Toast.LENGTH_SHORT)
                    }
                })
                return true
            }
            R.id.mute -> {
                Log.d("x",item?.title.toString())
                if(item?.title=="mute") {
                    item.icon = getDrawable(R.drawable.unmute)
                    item?.title = "unmute"
                    mute=false
                }
                else {
                    item?.title = "mute"
                    item?.icon = getDrawable(R.drawable.mute)
                    mute=true
                }
               return true
            }
            R.id.search -> {
                TODO()
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