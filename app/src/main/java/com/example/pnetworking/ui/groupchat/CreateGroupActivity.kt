package com.example.pnetworking.ui.groupchat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivityCreateGroupBinding
import com.example.pnetworking.ui.base.signin.SigninActivity
import com.example.pnetworking.ui.profile.FollowViewModel
import com.example.pnetworking.ui.profile.ProfileViewModel
import com.example.pnetworking.utils.ChatActivity
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import org.koin.android.viewmodel.ext.android.viewModel

class CreateGroupActivity : ChatActivity() {
    lateinit var binding: ActivityCreateGroupBinding
    val adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var friendsRecyclerView: RecyclerView
    lateinit var groupImage: CircleImageView
    lateinit var groupName: TextInputEditText
    lateinit var groupBio: TextInputEditText
    lateinit var createBtn: Button
    private val mainViewModel by viewModel<ProfileViewModel>()
    private val followViewModel by viewModel<FollowViewModel>()
    private val groupViewModel by viewModel<GroupViewModel>()
    val statuses = ArrayList<MutableLiveData<String>>()
    private val PERMISSION_REQUEST_CODE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        initRecyclerView()
        groupImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    FilePickerBuilder.instance
                        .setMaxCount(1) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickPhoto(this, 0)

                } else {
                    requestPermission()
                }
            }
        }
        createBtn.setOnClickListener {
            showProgressDialog(this)
            if (groupName.text.toString().isEmpty()) {
                groupName.error = "Please enter your email"
            } else {
                val name = groupName.text.toString()
                val bio = groupBio.text.toString()
                groupViewModel.createGroup(selectedPhotoUri!![0], name, bio)
                    .observe(this, { chatid ->
                        hideProgressDialog()
                        statuses.forEach {
                            it.observe(this, { s ->
                                if (s != "") {
                                    val item = adapter.getItem(s.toInt()) as UserChangeStatusItem
                                    val participant = item.user
                                    groupViewModel.addAParticipant(participant, chatid)
                                }
                            })
                        }
                        val intent = Intent(this, GroupChatActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("group_chat", chatid)
                        startActivity(intent)
                    })

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
            groupImage.setBackgroundColor(0)
            Picasso.get().load(Uri.parse(selectedPhotoUri!![0].toString())).into(groupImage)

        }
    }

    private fun initRecyclerView() {
        friendsRecyclerView?.adapter = adapter
        friendsRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )
        showProgressDialog(this)
        followViewModel.getFollowers().observe(this, {
            hideProgressDialog()
            for (s: String in it) {
                mainViewModel.getCurrentUser(s).observe(this, { u ->
                    val status = MutableLiveData<String>()
                    if (u != null)
                        adapter.add(UserChangeStatusItem(u, this, "ADD", "REMOVE", status))
                    Log.d("u", u.id)
                    statuses.add(status)
                })
            }
        })

    }

    private fun init() {
        friendsRecyclerView = binding.groupRecyclerView
        groupImage = binding.groupImage
        groupName = binding.groupNameText
        groupBio = binding.groupSummeryText
        createBtn = binding.createGroupBtn
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