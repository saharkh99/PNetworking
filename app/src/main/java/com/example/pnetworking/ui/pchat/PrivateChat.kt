package com.example.pnetworking.ui.pchat

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
import java.lang.reflect.Method


class PrivateChat : AppCompatActivity() {
    //image(attachment)-seen-new message-typing
    var username = ""
    var userImage = ""
    var chatId = ""
    var userId = ""
    var reply = ""
    var edit = false
    var type = false
    lateinit var refrence: FirebaseDatabase
    lateinit var seenListener: ValueEventListener
    lateinit var nameTextView: TextView
    lateinit var imageCircle: CircleImageView
    lateinit var toolbar: Toolbar
    lateinit var send: ImageButton
    lateinit var editChat: EditText
    lateinit var imageSend: ImageButton
    lateinit var mainlayout: ConstraintLayout
    lateinit var binding: ActivityPrivateChatBinding
    val adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var chatRecyclerView: RecyclerView
    private val PERMISSION_REQUEST_CODE = 1
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
            selectedPhotoUri = Uri.parse("")
        Log.d("selected", selectedPhotoUri?.path!!)
        if (edit) {
            mainViewModel.editMessage(text, chatId)
        } else {
            mainViewModel.performSendMessage(text, chatId, selectedPhotoUri!!, reply).observe(
                this,
                {
                    Log.d("send message", it)
                    if (it == "true") {
                        editChat.setText("")
                        var msg = Message()
                        msg.context = text
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
//            Log.d("1", "1")


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
                    requestPermission() // Code for permission
                }
            }
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("TAG", "Photo was selected")
//            selectedPhotoUri = data.data
            val photoPaths = ArrayList<Uri>()
            photoPaths.addAll(data.getParcelableArrayListExtra<Uri>(KEY_SELECTED_MEDIA)!!)
            Log.d("photo",photoPaths.toString())
        }
    }

    private fun listenForMessages() {
        mainViewModel.listenForMessages(chatId).observe(this, { chatMessage ->
            Log.d("from", "1")
            if (chatMessage.idUSer == FirebaseAuth.getInstance().uid) {
                adapter.add(0, ChatItem(chatMessage, userImage, false, chatMessage.reply))
                type = false
            } else {
                adapter.add(0, ChatItem(chatMessage, userImage, true, chatMessage.reply))
                type = true
            }
            adapter.notifyDataSetChanged()
            adapter.setOnItemClickListener { i, view ->
                val popup = PopupMenu(this, view)
                val msg = i as ChatItem
                popup.inflate(R.menu.options_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit -> {
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
        nameTextView.text = username
        chatRecyclerView?.adapter = adapter
        chatRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )

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
                    if (!type)
                        mainViewModel.seenMessage(chatId, userId)
                            .observe(this@PrivateChat, {
                                Log.d("getit", it.toString())
                            })
                }
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            if (menu.javaClass.simpleName == "MenuBuilder") {
                try {
                    val m: Method = menu.javaClass.getDeclaredMethod(
                        "setOptionalIconsVisible", java.lang.Boolean.TYPE
                    )
                    m.isAccessible = true
                    m.invoke(menu, true)
                } catch (e: Exception) {
                    Log.e(
                        javaClass.simpleName,
                        "onMenuOpened...unable to set icons for overflow menu",
                        e
                    )
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.block -> {
                    TODO()
                }
                R.id.mute -> {
                    TODO()
                }
                R.id.search -> {
                    TODO()
                }
            }
        }

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
}