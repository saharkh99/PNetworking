package com.example.pnetworking.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pnetworking.R
import com.example.pnetworking.models.User
import com.example.pnetworking.ui.pchat.PrivateChat
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel

class CardProfileFragment : DialogFragment() {
    companion object {
        val TAG = "TAG"
        private const val KEY_ID = "KEY_ID"
        private const val KEY_NAME = "KEY_NAME"
        private const val KEY_BIO = "KEY_BIO"
        private const val KEY_IMG = "KEY_IMG"
        private const val KEY_FRIENDS = "KEY_FRIENDS"
        private const val KEY_FAVORITE = "KEY_FAVORITE"
        private const val KEY_TAG = "KEY_TAG"
        private const val KEY_AGE = "KEY_AGE"
        private const val KEY_USER= "KEY_USER"

        fun newInstance(
            user: User,
            id: String,
            name: String,
            bio: String,
            img: String,
            friends: String,
            fav: String,
            age:String,
            tag: String
        ): CardProfileFragment {

            val args = Bundle()
            args.putParcelable(KEY_USER,user)
            args.putString(KEY_ID, id)
            args.putString(KEY_NAME, name)
            args.putString(KEY_BIO, bio)
            args.putString(KEY_IMG, img)
            args.putString(KEY_FRIENDS, friends)
            args.putString(KEY_FAVORITE, fav)
            args.putString(KEY_AGE, age)
            args.putString(KEY_TAG,tag)
            val fragment = CardProfileFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private val profileViewModel by viewModel<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.profile_card_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
        view.findViewById<TextView>(R.id.profile_card_name).text = "name: "+arguments?.getString(KEY_NAME)
        view.findViewById<TextView>(R.id.profile_card_bio).text = "BIO: "+arguments?.getString(KEY_BIO)
        view.findViewById<TextView>(R.id.profile_card_favorites).text ="FAVORITES: "+arguments?.getString(
            KEY_FAVORITE
        )
        view.findViewById<TextView>(R.id.profile_card_age).text="BIRTH: "+arguments?.getString(
            KEY_AGE)
        view.findViewById<TextView>(R.id.profile_card_friends).text =  arguments?.getString(
            KEY_FRIENDS
        )
        val img = view.findViewById<ImageView>(R.id.profile_card_image)
        Log.d("image", arguments?.getString(KEY_IMG)!!)
        if (arguments?.getString(KEY_IMG) != "true") {
            Picasso.get().load(Uri.parse(arguments?.getString(KEY_IMG))).into(img)
        }
        else
            img.setImageResource(R.drawable.user)

    }

    private fun setupClickListeners(view: View) {
        view.findViewById<TextView>(R.id.profile_card_connect).setOnClickListener { view1->
            Log.d("tag",arguments?.getString(KEY_TAG).toString())
            if(arguments?.getString(KEY_TAG)=="Chat") {
                profileViewModel.follow(arguments?.getString(KEY_ID)!!)
                    .observe(viewLifecycleOwner, {
                        if (it) {
                            Log.d("shod", "shod")
                            profileViewModel.increasingConnections(arguments?.getString(KEY_ID)!!)
                                .observe(viewLifecycleOwner, { it1 ->
                                    if (it1) {
                                        view.findViewById<TextView>(R.id.profile_card_connect).setBackgroundColor(getResources().getColor(R.color.teal_200))
                                        view1.background = resources.getDrawable(R.color.teal_200)
                                        view1.isClickable =
                                            false
                                        profileViewModel.deleteRequest(arguments?.getString(KEY_ID)!!).observe(viewLifecycleOwner,{
                                           if(it) {
                                               Log.d("shod3", "shod3")
                                               dismiss()
                                           }
                                        })
                                    }
                                })
                        }
                    })
            }

            if(arguments?.getString(KEY_TAG)=="Profile")
            profileViewModel.sendRequest(arguments?.getString(KEY_ID)!!).observe(viewLifecycleOwner,{
                if(it){
                    Log.d("shod","shod")
                    dismiss()
                }
            })

        }
        view.findViewById<TextView>(R.id.profile_card_Message).setOnClickListener {
            val intent = Intent(this.requireActivity(), PrivateChat::class.java)
            Log.d("user",(arguments?.getParcelable(KEY_USER) as User?)!!.id)
            intent.putExtra("KEY_USER", arguments?.getString(KEY_NAME))
            intent.putExtra("KEY_USER2", arguments?.getString(KEY_IMG))
            intent.putExtra("KEY_USER3", arguments?.getString(KEY_ID))
            //intent.putExtra("KEY_USER3", arguments?.getParcelable(KEY_USER) as User?)
            startActivity(intent)
            //dismiss()
        }
    }


}
