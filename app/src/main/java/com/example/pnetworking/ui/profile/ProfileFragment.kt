package com.example.pnetworking.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.pnetworking.R
import com.example.pnetworking.databinding.FragmentProfileBinding
import com.example.pnetworking.utils.ChatFragments
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : ChatFragments() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModel<ProfileViewModel>()
    lateinit var edit: Button
    lateinit var image: CircleImageView
    lateinit var name: TextView
    lateinit var bio: TextView
    lateinit var email: TextView
    lateinit var connetion: TextView
    lateinit var online: ImageView
    lateinit var onlineText:TextView
    var running: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        init()
        getProfile()
        edit.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_ProfileFragment_to_profileEditActivity)
        }
        return view

    }

    private fun getProfile() {
        mainViewModel.getIDUser().observe(viewLifecycleOwner, Observer {
            showProgressDialog(requireContext())
            if (it != null) {
                Log.d("uids", it)
                mainViewModel.getCurrentUser(it).observe(viewLifecycleOwner, Observer {
                    val currentUser = it
                    hideProgressDialog()
                    Log.d("image", currentUser.imageProfile)
                    if (currentUser.imageProfile != "")
                        Picasso.get().load(Uri.parse(currentUser.imageProfile)).into(image)
                    else
                        image.background = resources.getDrawable(R.drawable.user)
                    name.text = currentUser.name
                    email.text = currentUser.emailText
                    connetion.text = currentUser.connection.toString()
                    bio.text = currentUser.bio
                    if (currentUser.online) {
                        online.visibility = View.VISIBLE
                        onlineText.visibility=View.VISIBLE
                    }
                    else{
                        online.visibility = View.GONE
                        onlineText.visibility = View.GONE
                    }
                })
            }

        })
        running = true
    }

    override fun onPause() {
        super.onPause()
        running = false
    }

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
        online = binding.profileOnline
        onlineText=binding.profileOnlineText
    }

}