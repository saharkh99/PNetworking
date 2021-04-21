package com.example.pnetworking.ui.profile

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
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.chat.ui.main.connection.ConnectionViewModel
import com.example.pnetworking.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
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

        fun newInstance(
            id: String,
            name: String,
            bio: String,
            img: String,
            friends: String,
            fav: String
        ): CardProfileFragment {

            val args = Bundle()
            args.putString(KEY_ID, id)
            args.putString(KEY_NAME, name)
            args.putString(KEY_BIO, bio)
            args.putString(KEY_IMG, img)
            args.putString(KEY_FRIENDS, friends)
            args.putString(KEY_FAVORITE, fav)
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
        view.findViewById<TextView>(R.id.profile_card_name).text = arguments?.getString(KEY_NAME)
        view.findViewById<TextView>(R.id.profile_card_bio).text = arguments?.getString(KEY_BIO)
        view.findViewById<TextView>(R.id.profile_card_favorites).text = arguments?.getString(
            KEY_FAVORITE
        )
        view.findViewById<TextView>(R.id.profile_card_friends).text = arguments?.getString(
            KEY_FRIENDS
        )
        val img = view.findViewById<ImageView>(R.id.profile_card_image)
        Log.d("image", arguments?.getString(KEY_IMG)!!)
        if (arguments?.getString(KEY_IMG) != "" || arguments?.getString(KEY_IMG) != "true")
            Picasso.get().load(Uri.parse(arguments?.getString(KEY_IMG))).into(img)
        else
            img.setImageResource(R.drawable.user)

    }

    private fun setupClickListeners(view: View) {
        view.findViewById<TextView>(R.id.profile_card_connect).setOnClickListener {
            Log.d("sd", viewLifecycleOwner.toString())
            Log.d("sd", viewLifecycleOwner.lifecycleScope.toString())
            profileViewModel.follow(arguments?.getString(KEY_ID)!!).observe(viewLifecycleOwner, {
                if (it) {
                    Log.d("shod", "shod12")
                    profileViewModel.increasingConnections().observe(viewLifecycleOwner, { it1 ->
                        if (it1) {
                            Log.d("shod", "shod")
                            view.findViewById<TextView>(R.id.profile_card_connect)
                                .setTextColor(resources.getColor(R.color.teal_200))
                            view.findViewById<TextView>(R.id.profile_card_connect).isClickable =
                                false
                            dismiss()
                        }
                    })
                }
            })

        }
        view.findViewById<TextView>(R.id.profile_card_Message).setOnClickListener {
            dismiss()
        }
    }


}