package com.example.pnetworking.ui.connection

import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.ui.main.connection.ConnectionViewModel

import com.example.pnetworking.R
import com.example.pnetworking.models.User
import com.example.pnetworking.ui.chat.ChatFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.viewmodel.ext.android.viewModel

class ConnectionFragment : ChatFragment() {

    private val connectionViewModel by viewModel<ConnectionViewModel>()
    val adapter = GroupAdapter<GroupieViewHolder>()
    val binding = view?.findViewById<EditText>(R.id.input)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initRecyclerView()
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun initRecyclerView() {

        val rec = view?.findViewById<RecyclerView>(R.id.find_friends_recycler_View)
        rec?.adapter = adapter
            for (u: User in connectionViewModel.getUsers(viewLifecycleOwner)) {
                Log.d("u",u.id)
                adapter.add(UserList(u,requireContext()))
            }
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}

