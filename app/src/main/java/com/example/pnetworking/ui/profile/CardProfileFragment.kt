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
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.example.pnetworking.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CardProfileFragment : DialogFragment() {

    companion object {
        val TAG="TAG"
        private const val KEY_NAME = "KEY_NAME"
        private const val KEY_BIO = "KEY_BIO"
        private const val KEY_IMG = "KEY_IMG"
        private const val KEY_FRIENDS = "KEY_FRIENDS"
        private const val KEY_FAVORITE = "KEY_FAVORITE"

        fun newInstance(
            name: String,
            bio: String,
            img: String,
            friends: String,
            fav: String
        ): CardProfileFragment {

            val args = Bundle()
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
        Log.d("image", KEY_IMG)
        if (KEY_IMG != "")
            Picasso.get().load(Uri.parse(KEY_IMG)).into(img)
        else
            img.background = AppCompatResources.getDrawable(requireContext(), R.drawable.user)

    }

    private fun setupClickListeners(view: View) {
        view.findViewById<TextView>(R.id.profile_card_connect).setOnClickListener {
            dismiss()
        }
        view.findViewById<TextView>(R.id.profile_card_Message).setOnClickListener {
            dismiss()
        }
    }

}