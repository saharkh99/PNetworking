package com.example.pnetworking.ui.connection

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.ui.main.connection.ConnectionViewModel

import com.example.pnetworking.R
import com.example.pnetworking.models.User
import com.example.pnetworking.ui.chat.ChatFragment
import com.example.pnetworking.ui.profile.CardProfileFragment
import com.example.pnetworking.utils.ChatFragments
import com.example.pnetworking.utils.findAge
import com.example.pnetworking.utils.zodiac
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.viewmodel.ext.android.viewModel

class ConnectionFragment : ChatFragments() {

    private val connectionViewModel by viewModel<ConnectionViewModel>()
    val adapter = GroupAdapter<GroupieViewHolder>()
    val binding = view?.findViewById<EditText>(R.id.input)
    val l = ArrayList<User>()


    companion object {
        val USER_KEY = "USER_KEY"
        val Tag="Profile"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearchInputListener()
    }

    private fun initSearchInputListener() {

        binding?.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        binding?.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = binding?.text.toString()
        dismissKeyboard(v.windowToken)
        connectionViewModel.setQuery(query)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView() {

        val rec = view?.findViewById<RecyclerView>(R.id.find_friends_recycler_View)
        rec?.adapter = adapter
        showProgressDialog(requireContext())
        connectionViewModel.getUsers().observe(viewLifecycleOwner,{
            hideProgressDialog()
            for (u: User in it) {
                Log.d("u",u.id)
                if(u!=null )
                adapter.add(UserList(u,requireContext()))
                l.add(u)
            }
        })
        adapter.setOnItemClickListener { item, _ ->
            val userItem = item as UserList
            val age= findAge(item.user.birthday).toString() +", "+ zodiac(item.user.birthday)
            Log.d("image",item.user.imageProfile)
            CardProfileFragment.newInstance(
                item.user.id,
                item.user.name,
                item.user.bio,
                item.user.imageProfile,
                "friends: "+item.user.connection.toString(),
                item.user.favorites,
                age,
                ConnectionFragment.Tag
            ).show(parentFragmentManager, CardProfileFragment.TAG)
        }

    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}

