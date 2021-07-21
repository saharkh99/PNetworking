package com.example.pnetworking.ui.userchat

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.databinding.FragmentChatBinding
import com.example.pnetworking.models.Message
import com.example.pnetworking.ui.connection.UserList
import com.example.pnetworking.ui.pchat.PrivateChateViewModel
import com.example.pnetworking.ui.profile.CardProfileFragment
import com.example.pnetworking.ui.profile.FollowViewModel
import com.example.pnetworking.ui.profile.ProfileViewModel
import com.example.pnetworking.utils.ChatFragments
import com.example.pnetworking.utils.findAge
import com.example.pnetworking.utils.zodiac
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception

open class ChatFragment : ChatFragments() {
    private val followViewModel by viewModel<FollowViewModel>()
    private val mainViewModel by viewModel<ProfileViewModel>()
    private val chatViewModel by viewModel<ChatFViewModel>()
    private val pChatViewModel by viewModel<PrivateChateViewModel>()

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    lateinit var rec: RecyclerView
    lateinit var recRecent: RecyclerView
    lateinit var recNew: RecyclerView
    val adapter = GroupAdapter<GroupieViewHolder>()
    val adapter2 = GroupAdapter<GroupieViewHolder>()
    val adapter3 = GroupAdapter<GroupieViewHolder>()

    companion object {
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
        recRecent = binding.recentMessageRecyclerview
        recNew = binding.recentMessageNewRecyclerview
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getRecentMessage()
        getNewMessages()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getNewMessages() {
        adapter3.clear()
        recNew?.adapter = adapter3
        recNew?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)

        pChatViewModel.numberOfNewMessages("").observe(viewLifecycleOwner, {
            it.forEach { (t, u) ->
                mainViewModel.getCurrentUser(t).observe(viewLifecycleOwner, { user ->
                    Log.d("totalss", " $t $u")
                    val myInt = u as? Int ?: 0
                    Log.d("totalss", user.id)
                    adapter3.add(UserProList(user, requireContext(), myInt))
                    adapter3.notifyDataSetChanged()


                })

            }
        })
    }

    private fun getRecentMessage() {
        try {
            chatViewModel.getRecentMessages().observe(viewLifecycleOwner, {
                adapter2.clear()
                recRecent?.adapter = adapter2
                recRecent?.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                mainViewModel.getCurrentUser(it.idUSer).observe(viewLifecycleOwner, { u ->
                    recRecent?.adapter = adapter2
                    recRecent?.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                    u.bio = it.context
                    val x = it.idTo.removePrefix(it.idUSer)
                    mainViewModel.getCurrentUser(x).observe(viewLifecycleOwner, { to ->
                        u.emailText = to.emailText
                        Log.d("x", u.emailText)
                        u.imageProfile = to.imageProfile
                        adapter2.add(UserList(u, requireContext()))
                        adapter2.notifyDataSetChanged()
                    })
                    Log.d("u", u.emailText)
                })

            })
        } catch (e: Exception) {
            Log.d("recent", e.message!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView() {
        showProgressDialog(requireContext())
        adapter.clear()
        followViewModel.getRequests().observe(viewLifecycleOwner, {
            hideProgressDialog()
            for (s: String in it) {
                mainViewModel.getCurrentUser(s).observe(viewLifecycleOwner, { u ->
                    rec?.adapter = adapter
                    rec?.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                    adapter.add(UserList(u, requireContext()))
                    Log.d("u", u.id)
                })
            }
        })
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserList
            Log.d("image", item.user.imageProfile)
            val age = findAge(item.user.birthday).toString() + ", " + zodiac(item.user.birthday)
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