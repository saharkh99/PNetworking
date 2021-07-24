package com.example.pnetworking.ui.connection

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.ui.main.connection.ConnectionViewModel
import com.example.pnetworking.R
import com.example.pnetworking.models.User
import com.example.pnetworking.ui.profile.CardProfileFragment
import com.example.pnetworking.utils.ChatFragments
import com.example.pnetworking.utils.findAge
import com.example.pnetworking.utils.zodiac
import com.paulrybitskyi.persistentsearchview.PersistentSearchView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.coroutineContext


class ConnectionFragment : ChatFragments() {
    //search, add features like group chat or setting
    private val connectionViewModel by viewModel<ConnectionViewModel>()
    val adapter = GroupAdapter<GroupieViewHolder>()
    val binding = view?.findViewById<EditText>(R.id.input)
    val l = ArrayList<User>()
    lateinit var persistentSearchView:PersistentSearchView



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

    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        persistentSearchView=view.findViewById<PersistentSearchView>(R.id.persistentSearchView)
        with(persistentSearchView) {
            setOnLeftBtnClickListener {
                l.clear()
                adapter.clear()
                onBackPressed()
            }
            setOnClearInputBtnClickListener {
                l.clear()
                adapter.clear()
            }

            setOnSearchConfirmedListener { searchView, query ->
                showProgressDialog(requireContext())
                searchView.collapse()
                initRecyclerView(query)
                searchView.onCancelPendingInputEvents()
                dismissKeyboard(windowToken)
            }

            setSuggestionsDisabled(true)
        }

    }



    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView(query:String) {
        adapter.clear()
        l.clear()
        val rec = view?.findViewById<RecyclerView>(R.id.find_friends_recycler_View)
        rec?.adapter = adapter
        connectionViewModel.getUsers().observe(viewLifecycleOwner,{
            hideProgressDialog()
            for (u: User in it) {
                Log.d("u",u.id)
                if(u!=null ) {
                    if(u.name.lowercase().contains(query.lowercase()) || u.emailText.lowercase().contains(query.lowercase())) {
                        if(!l.contains(u)) {
                            adapter.notifyDataSetChanged()
                            adapter.add(UserList(u, requireContext()))
                            l.add(u)
                        }
                    }
                }
            }
        })
        adapter.setOnItemClickListener { item, _ ->
            val userItem = item as UserList
            val age= findAge(item.user.birthday).toString() +", "+ zodiac(item.user.birthday)
            Log.d("image",item.user.imageProfile)
            CardProfileFragment.newInstance(
                item.user,
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
    private fun onBackPressed() {
        if(persistentSearchView.isExpanded) {
            persistentSearchView.collapse()
            return
        }
    }


}

