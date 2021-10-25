package com.example.pnetworking.ui.userchat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.databinding.FragmentChatBinding
import com.example.pnetworking.models.User
import com.example.pnetworking.ui.connection.UserList
import com.example.pnetworking.ui.features.SettingsActivity
import com.example.pnetworking.ui.groupchat.CreateGroupActivity
import com.example.pnetworking.ui.groupchat.GroupChatActivity
import com.example.pnetworking.ui.groupchat.GroupViewModel
import com.example.pnetworking.ui.groupchat.UserChangeStatusItem
import com.example.pnetworking.ui.pchat.ChatItem
import com.example.pnetworking.ui.pchat.PrivateChat
import com.example.pnetworking.ui.pchat.PrivateChatViewModel
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
    private val pChatViewModel by viewModel<PrivateChatViewModel>()
    private val groupViewModel by viewModel<GroupViewModel>()

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    lateinit var rec: RecyclerView
    lateinit var recRecent: RecyclerView
    lateinit var recNew: RecyclerView
    lateinit var friendsCard: CardView
    lateinit var settingCard: CardView
    lateinit var groupCard: CardView
    val adapter = GroupAdapter<GroupieViewHolder>()
    val adapter2 = GroupAdapter<GroupieViewHolder>()

    companion object {
        const val TAG = "Chat"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        rec = binding.recentMessageRequests
        recRecent = binding.recentMessageRecyclerview
        friendsCard = binding.cartFriends
        friendsCard.setOnClickListener {
            val action = ChatFragmentDirections.actionChatFragmentToFriendsActivity()
            view.findNavController().navigate(action)
        }
        settingCard = binding.cartSetting
        settingCard.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
        groupCard = binding.cartAddGroup
        groupCard.setOnClickListener {
            startActivity(Intent(requireContext(), CreateGroupActivity::class.java))
        }
        checkPhrase()
        followViewModel.getStatusConnection().observe(viewLifecycleOwner, {
            if (!it) {
                initRecyclerView()
                followViewModel.changeConnection(true)
            }
        })

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkPhrase() {
        mainViewModel.getPhaseOfRequest().observe(viewLifecycleOwner, {
            if (it == "connected") {
                initRecyclerView()
                showErrorSnackBar("ssssss", requireContext(), requireView())
                Log.d("phase", requireActivity().toString())
                mainViewModel.changePhaseOfRequest("normal")
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getRecentMessage()

    }

    private fun getRecentMessage() {
        adapter2.clear()
        recRecent.adapter = adapter2
        recRecent.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        try {
            Log.d("chats",chatViewModel.getIdUser().value!!)
            chatViewModel.getRecentMessages(chatViewModel.getIdUser().value!!).observe(viewLifecycleOwner, {

                if (it.idTo.contains(it.idUSer)) {
                    mainViewModel.getCurrentUser(it.idUSer).observe(viewLifecycleOwner, { u ->
                        adapter2.clear()
                        recRecent.adapter = adapter2
                        recRecent.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                true
                            )
                        u.bio = it.context
                        chatViewModel.getIdUser().observe(viewLifecycleOwner, { id ->
                            val x = it.idTo.replace(id, "")
                            mainViewModel.getCurrentUser(x).observe(viewLifecycleOwner, { to ->
                                val chatId=id+to.id
                                pChatViewModel.numberOfNewMessages(chatId).observe(viewLifecycleOwner,{ new->
                                    if (to != null) {
                                        u.id=to.id
                                        u.emailText = to.emailText
                                        Log.d("x", u.emailText)
                                        u.imageProfile = to.imageProfile
                                        u.name = to.name
                                        adapter2.add(UserList(u, requireContext(),new))
                                        adapter2.notifyDataSetChanged()
                                    }
                                })

                            })
                        })
                    })
                }


            })
              chatViewModel.getChats().observe(viewLifecycleOwner,{ chats->
                Log.d("chats","chats")
                chats.forEach { c->
                    chatViewModel.getRecentMessages(c).observe(viewLifecycleOwner, {
                        if (!it.idTo.contains(it.idUSer)){
                            Log.d("chats",c)
                            adapter2.clear()
                            recRecent.adapter = adapter2
                            recRecent.layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    true
                                )
                            groupViewModel.getGroupChat(c).observe(viewLifecycleOwner, { g ->
                                val u = User()
                                u.id=g.idChat
                                u.imageProfile = g.image
                                u.emailText = "Group: " + g.name
                                u.name = g.name
                                u.bio = it.context
                                u.gender="group"
                                Log.d("group2",u.id)
                                adapter2.add(UserList(u, requireContext(),""))
                                adapter2.notifyDataSetChanged()

                            })
                        }
                    })
                }
            })

            adapter2.setOnItemClickListener { item, view ->
                val user = item as UserList
                if(item.user.gender=="group"){
                    val intent = Intent(requireContext(), GroupChatActivity::class.java)
                    Log.d("group",item.user.id)
                    intent.putExtra("chat_fragment", item.user.id)
                    startActivity(intent)
                }
                else{
                    Log.d("pricate",item.user.name)
                    val intent = Intent(this.requireActivity(), PrivateChat::class.java)
                    intent.putExtra("KEY_USER", item.user.name)
                    intent.putExtra("KEY_USER2",item.user.imageProfile)
                    intent.putExtra("KEY_USER3",item.user.id)
                    startActivity(intent)
                }

            }
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
                Log.d("u", s)

                mainViewModel.getCurrentUser(s).observe(viewLifecycleOwner, { u ->
                    rec.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                    adapter.add(
                        UserChangeStatusItem(
                            u,
                            requireContext(),
                            "remove",
                            "remove",
                            "",
                            followViewModel
                        )
                    )
                    rec.adapter = adapter
                })
            }
        })
        adapter.setOnItemClickListener { item, _ ->
            val userItem = item as UserChangeStatusItem
            Log.d("image1212", item.user.id)
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
                TAG
            ).show(parentFragmentManager, CardProfileFragment.TAG)
//            adapter.notifyDataSetChanged()
        }
    }


}