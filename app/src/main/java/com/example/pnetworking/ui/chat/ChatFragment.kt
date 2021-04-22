package com.example.pnetworking.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pnetworking.databinding.FragmentChatBinding
import com.example.pnetworking.ui.connection.UserList
import com.example.pnetworking.ui.profile.CardProfileFragment
import com.example.pnetworking.ui.profile.FollowViewModel
import com.example.pnetworking.ui.profile.ProfileViewModel
import com.example.pnetworking.utils.ChatFragments
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.viewmodel.ext.android.viewModel

open class ChatFragment:ChatFragments() {
    private val followViewModel by viewModel<FollowViewModel>()
    private val mainViewModel by viewModel<ProfileViewModel>()
    val adapter = GroupAdapter<GroupieViewHolder>()
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

    }

    private fun initRecyclerView(){
        val rec = binding.recentMessageRequests
        rec?.adapter = adapter
        rec?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        showProgressDialog(requireContext())
        followViewModel.getRequests().observe(viewLifecycleOwner,{
            hideProgressDialog()
            for (s: String in it) {
                mainViewModel.getCurrentUser(s).observe(viewLifecycleOwner, { u ->
                    if(u!=null)
                     adapter.add(UserList(u, requireContext()))
                     Log.d("u", u.id)
                })
            }
        })
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserList
            Log.d("image", item.user.imageProfile)
            CardProfileFragment.newInstance(
                item.user.id,
                item.user.name,
                item.user.bio,
                item.user.imageProfile,
                "friends: " + item.user.connection.toString(),
                item.user.favorites,
                ChatFragment.TAG
            ).show(parentFragmentManager, CardProfileFragment.TAG)
        }

    }
}