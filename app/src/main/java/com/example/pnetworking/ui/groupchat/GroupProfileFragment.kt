package com.example.pnetworking.ui.groupchat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.R
import com.example.pnetworking.ui.profile.ProfileViewModel
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
        ): GroupProfileFragment {

            val args = Bundle()
            args.putString(KEY_ID, id)
            Log.d("id",id)
            val fragment = GroupProfileFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private val viewGroup by viewModel<GroupViewModel>()
    private val viewGroup2 by viewModel<ProfileViewModel>()

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
        viewGroup.getGroupChat(arguments?.getString(KEY_ID)!!).observe(viewLifecycleOwner, { g ->
            view.findViewById<EditText>(R.id.profile_group_name).setText(g.name)
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
                val status= MutableLiveData<String>()
                it.forEach { p ->
                    viewGroup2.getCurrentUser(p.idUSer).observe(viewLifecycleOwner,{ u->
                        if(u!=null)
                            adapter.add(UserChangeStatusItem(u,requireContext(),"ADD","REMOVE",status,p.role))
                        Log.d("u", u.id)
                    })
                }
            })


        })


    }

    private fun setupClickListeners(view: View) {
        view.findViewById<TextView>(R.id.profile_group_save).setOnClickListener { view1 ->

        }
    }

}