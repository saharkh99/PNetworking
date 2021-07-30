package com.example.pnetworking.ui.features

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.databinding.ActivityFriendsBinding
import com.example.pnetworking.ui.connection.UserList
import com.example.pnetworking.ui.profile.CardProfileFragment
import com.example.pnetworking.ui.profile.FollowViewModel
import com.example.pnetworking.ui.profile.ProfileFragment
import com.example.pnetworking.ui.profile.ProfileViewModel
import com.example.pnetworking.utils.ChatActivity
import com.example.pnetworking.utils.findAge
import com.example.pnetworking.utils.zodiac
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.viewmodel.ext.android.viewModel

class FriendsActivity : ChatActivity() {
    lateinit var binding: ActivityFriendsBinding
    val adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var friendsRecyclerView: RecyclerView
    private val followViewModel by viewModel<FollowViewModel>()
    private val mainViewModel by viewModel<ProfileViewModel>()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        friendsRecyclerView?.adapter = adapter
        friendsRecyclerView.layoutManager = LinearLayoutManager(
           this,
            LinearLayoutManager.VERTICAL,
            true
        )
        showProgressDialog(this)
        followViewModel.getFollowers().observe(this,{
            hideProgressDialog()
            for (s: String in it) {
                mainViewModel.getCurrentUser(s).observe(this, { u ->
                    if(u!=null)
                        adapter.add(UserList(u,this))
                    Log.d("u", u.id)
                })
            }
        })
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserList

            Log.d("image", item.user.imageProfile)

            val age= findAge(item.user.birthday).toString() +", "+ zodiac(item.user.birthday)
            CardProfileFragment.newInstance(
                item.user,
                item.user.id,
                item.user.name,
                item.user.bio,
                item.user.imageProfile,
                "friends: " + item.user.connection.toString(),
                item.user.favorites,
                age,
                ProfileFragment.TAG
            ).show(supportFragmentManager,CardProfileFragment.TAG)
        }

    }

    private fun init() {
        friendsRecyclerView=binding.friendsRecyclerView
    }
}