package com.example.pnetworking.ui.userchat

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.databinding.FragmentChatBinding
import com.example.pnetworking.ui.connection.UserList
import com.example.pnetworking.ui.profile.CardProfileFragment
import com.example.pnetworking.ui.profile.FollowViewModel
import com.example.pnetworking.ui.profile.ProfileViewModel
import com.example.pnetworking.utils.ChatFragments
import com.example.pnetworking.utils.findAge
import com.example.pnetworking.utils.zodiac
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.viewmodel.ext.android.viewModel

open class ChatFragment:ChatFragments() {
    private val followViewModel by viewModel<FollowViewModel>()
    private val mainViewModel by viewModel<ProfileViewModel>()
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    lateinit var rec:RecyclerView
    val adapter = GroupAdapter<GroupieViewHolder>()

    companion object{
        val TAG = "Chat"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        rec = binding.recentMessageRequests
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          initRecyclerView()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView(){
        showProgressDialog(requireContext())
        followViewModel.getRequests().observe(viewLifecycleOwner,{
            hideProgressDialog()
            for (s: String in it) {
                mainViewModel.getCurrentUser(s).observe(viewLifecycleOwner, { u ->
                    rec?.adapter = adapter
                    rec?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                     adapter.add(UserList(u, requireContext()))
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
                ChatFragment.TAG
            ).show(parentFragmentManager, CardProfileFragment.TAG)

        }

    }

}