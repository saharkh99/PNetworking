package com.example.pnetworking.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.databinding.FragmentProfileBinding
import com.example.pnetworking.models.User
import com.example.pnetworking.ui.connection.UserList
import com.example.pnetworking.ui.features.SettingsActivity
import com.example.pnetworking.utils.ChatFragments
import com.example.pnetworking.utils.findAge
import com.example.pnetworking.utils.zodiac
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment : ChatFragments() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModel<ProfileViewModel>()
    private val followViewModel by viewModel<FollowViewModel>()
    lateinit var edit: Button
    lateinit var image: CircleImageView
    lateinit var setting:ImageView
    lateinit var name: TextView
    lateinit var bio: TextView
    lateinit var age:TextView
    lateinit var zodiac:TextView
    lateinit var email: TextView
    lateinit var connetion: TextView
    lateinit var favs: TextView
    lateinit var online: ImageView
    lateinit var onlineText:TextView
    lateinit var connectionRecyclerView: RecyclerView
    var running: Boolean = false
    lateinit var user: User
    val adapter = GroupAdapter<GroupieViewHolder>()

    companion object{
         val TAG = "profile"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getProfile()
        edit.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToProfileEditActivity2(user.name,user.bio)
            view.findNavController().navigate(action)

        }
        setting.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRec() {
        connectionRecyclerView?.adapter = adapter
        connectionRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            true
        )
        followViewModel.getFollowers().observe(viewLifecycleOwner, {
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
            ).show(parentFragmentManager, CardProfileFragment.TAG)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getProfile() {
        mainViewModel.getIDUser().observe(viewLifecycleOwner, Observer {
            showProgressDialog2(requireContext())
            if (it != null) {
                Log.d("uids", it)
                mainViewModel.getCurrentUser(it).observe(viewLifecycleOwner, Observer {
                    val currentUser = it
                    user = User()
                    user.name=currentUser.name
                    user.bio=currentUser.bio
                    Log.d("image", currentUser.imageProfile)
                    if (currentUser.imageProfile != "")
                        Picasso.get().load(Uri.parse(currentUser.imageProfile)).into(image)
                    else
                        image.background = resources.getDrawable(R.drawable.user)
                    name.text = currentUser.name
                    email.text = currentUser.emailText
                    connetion.text = currentUser.connection.toString()
                    favs.text = currentUser.favorites
                    bio.text = currentUser.bio
                    if (currentUser.online) {
                        online.visibility = View.VISIBLE
                        onlineText.visibility = View.VISIBLE
                    } else {
                        online.visibility = View.GONE
                        onlineText.visibility = View.GONE
                    }
                    age.text =
                        com.example.pnetworking.utils.findAge(currentUser.birthday).toString()
                    zodiac.text = com.example.pnetworking.utils.zodiac(currentUser.birthday)
                })
                initRec()
            }

        })
        running = true
    }

    override fun onPause() {
        super.onPause()
        running = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        if (!running)
            getProfile()
    }

    private fun init() {
        edit = binding.profileEdit
        name = binding.profileNameUser
        image = binding.profilePictureUser
        bio = binding.profileBioUser
        email = binding.profileEmailUser
        connetion = binding.profileFriendsUser
        favs = binding.profileFavoritesUser
        online = binding.profileOnline
        onlineText=binding.profileOnlineText
        connectionRecyclerView=binding.profileRecConnections
        age=binding.profileBirthdayUser
        zodiac=binding.profileAstro
        setting=binding.profileSetting
    }

}