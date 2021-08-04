package com.example.pnetworking.ui.groupchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.ui.profile.CardProfileFragment
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.viewmodel.ext.android.viewModel

class GroupProfileFragment : DialogFragment() {
    companion object {
        val adapter = GroupAdapter<GroupieViewHolder>()
        const val TAG = "TAG"
        private const val KEY_ID = "KEY_ID"

        fun newInstance(
            id: String,
        ): CardProfileFragment {

            val args = Bundle()
            args.putString(KEY_ID, id)
            val fragment = CardProfileFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private val viewGroup by viewModel<GroupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_group_profile, container, false)
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
        viewGroup.getGroupChat(KEY_ID).observe(viewLifecycleOwner, { g ->
            view.findViewById<TextView>(R.id.profile_group_name).text = g.name
            val img = view.findViewById<ImageView>(R.id.profile_card_image)
            if (g.image != "") {
                Picasso.get().load(g.image).into(img)
            } else
                img.setImageResource(R.drawable.user)
            view.findViewById<EditText>(R.id.profile_group_bio).setText(g.summery)
            val rec = view.findViewById<RecyclerView>(R.id.recyclerview_members)
            rec.adapter = adapter
            rec.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                true
            )
            viewGroup.getParticipants(KEY_ID).observe(viewLifecycleOwner, {
                it.forEach { p ->
                    //TODO
                }
            })


        })


    }

    private fun setupClickListeners(view: View) {
        view.findViewById<TextView>(R.id.profile_card_connect).setOnClickListener { view1 ->

        }
    }

}