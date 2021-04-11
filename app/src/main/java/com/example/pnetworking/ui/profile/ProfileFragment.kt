package com.example.pnetworking.ui.profile

import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.pnetworking.R
import com.example.pnetworking.databinding.FragmentProfileBinding
import com.example.pnetworking.ui.base.signin.SignInViewModel
import com.example.pnetworking.utils.ChatFragments
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment: ChatFragments() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModel<ProfileViewModel>()
    lateinit var edit: Button
    lateinit var image:CircleImageView
    lateinit var name:TextView
    lateinit var email:TextView
    lateinit var connetion:TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        init()
        mainViewModel.getIDUser().observe(viewLifecycleOwner, Observer {
            if(it!=null) {
                Log.d("uids",it)
                mainViewModel.getCurrentUser(it).observe(viewLifecycleOwner, Observer {
                    val currentUser = it
                    Log.d("uids",currentUser.id)
                    if (currentUser.imageProfile != "")
                        Picasso.get().load(Uri.parse(currentUser.imageProfile)).into(image)
                    name.text = currentUser.name
                    email.text = currentUser.emailText
                    connetion.text = currentUser.connection.toString()
                })
            }
        })

        return view

    }

    private fun init() {
        edit=binding.profileEdit
        name=binding.profileNameUser
        image=binding.profilePictureUser
        email=binding.profileEmailUser
        connetion=binding.profileFriendsUser
    }

}