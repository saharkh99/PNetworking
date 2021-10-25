package com.example.pnetworking.ui.connection

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.ui.main.connection.ConnectionViewModel
import com.example.pnetworking.R
import com.example.pnetworking.models.User
import com.example.pnetworking.ui.profile.CardProfileFragment
import com.example.pnetworking.ui.profile.ProfileViewModel
import com.example.pnetworking.utils.ChatFragments
import com.example.pnetworking.utils.findAge
import com.example.pnetworking.utils.zodiac
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.paulrybitskyi.persistentsearchview.PersistentSearchView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.viewmodel.ext.android.viewModel


class ConnectionFragment : ChatFragments() {
    private val connectionViewModel by viewModel<ConnectionViewModel>()
    private val profileViewModel by viewModel<ProfileViewModel>()
    val adapter = GroupAdapter<GroupieViewHolder>()
    val binding = view?.findViewById<EditText>(R.id.input)
    lateinit var rec:RecyclerView
    lateinit var persistentSearchView:PersistentSearchView
    lateinit var findUsers:Button
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0



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
        rec  =view?.findViewById<RecyclerView>(R.id.find_friends_recycler_View)
        findUsers  =view?.findViewById<Button>(R.id.find_Friends)
        with(persistentSearchView) {
            setOnLeftBtnClickListener {
                adapter.clear()
                rec.adapter=adapter
                onBackPressed()
            }
            setOnClearInputBtnClickListener {
                adapter.clear()
            }

            setOnSearchConfirmedListener { searchView, query ->
                showProgressDialog(requireContext())
                searchView.collapse()
                adapter.clear()
                Log.d("adapter",adapter.itemCount.toString())
                rec?.adapter=adapter
                initRecyclerView(query)
                searchView.onCancelPendingInputEvents()
                dismissKeyboard(windowToken)

            }

            setSuggestionsDisabled(true)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        findUsers.setOnClickListener {
//            getCurrentLocation()
            showProgressDialog(requireContext())
            val local = requireContext()!!.resources.configuration.locale.country
            Log.d("local",local)
            connectionViewModel.closestUsers("4", "3",local).observe(viewLifecycleOwner,{
                it.forEach { s->
                    Log.d("s",s)
                    rec?.removeAllViews()
                    hideProgressDialog()
                    profileViewModel.getCurrentUser(s).observe(viewLifecycleOwner,{u->
                        adapter.clear()
                        adapter.add(UserList(u, requireContext(),""))
                    })
                }
                rec?.adapter = adapter
            })
        }
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



    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView(query:String) {
        Log.d("rec","ec")
        rec?.removeAllViews()
        adapter.clear()
        rec?.adapter = adapter
        val l = ArrayList<User>()
        connectionViewModel.getUsers().observe(viewLifecycleOwner,{
            hideProgressDialog()
            adapter.clear()
            l.clear()
            for (u: User in it) {
                Log.d("u",u.id)
                if(u!=null  ) {
                    if(u.name.lowercase().contains(query.lowercase()) || u.emailText.lowercase().contains(query.lowercase())) {
                        adapter.clear()
                        if(!l.contains(u)) {
                            l.add(u)
                            adapter.add(UserList(u, requireContext(),""))
                            rec?.adapter = adapter
                        }
                    }
                }
            }
            l.clear()
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

    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE);
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                latitude = location.latitude
                longitude = location.longitude
                Log.d("location", "$latitude $longitude")
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed on getting current location",
                    Toast.LENGTH_SHORT).show()
            }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(requireContext(), "You need to grant permission to access location",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun openMap() {
        val uri = Uri.parse("geo:${latitude},${longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}


