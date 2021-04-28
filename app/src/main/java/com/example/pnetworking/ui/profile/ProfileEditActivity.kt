package com.example.pnetworking.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.NavArgument
import androidx.navigation.Navigation
import androidx.navigation.navArgs
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivityProfileEditBinding
import com.example.pnetworking.databinding.ActivitySignupBinding
import com.example.pnetworking.models.User
import com.example.pnetworking.utils.ChatActivity
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileEditActivity : ChatActivity() {
    lateinit var binding: ActivityProfileEditBinding
    lateinit var name: TextInputEditText
    lateinit var bio: TextInputEditText
    lateinit var save: Button
    lateinit var checkBook: CheckBox
    lateinit var checkEnvironment: CheckBox
    lateinit var checkTech: CheckBox
    lateinit var checkCook: CheckBox
    lateinit var checkMovie: CheckBox
    lateinit var checkMusic: CheckBox
    lateinit var checkDance: CheckBox
    lateinit var checkTravel: CheckBox
     var fav: String=""

    private val mainViewModel by viewModel<ProfileViewModel>()
    val user=navArgs<NavArgs>().value as User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        save.setOnClickListener {
            if (checkBook.isChecked) fav= "book $fav"
            if (checkCook.isChecked)  fav= "cooking $fav"
            if (checkDance.isChecked) fav= "dance $fav"
            if (checkEnvironment.isChecked) fav="environment $fav"
            if (checkMovie.isChecked) fav="movie $fav"
            if (checkMusic.isChecked) fav="music $fav"
            if (checkTech.isChecked) fav="tech $fav"
            if (checkTravel.isChecked) fav="travel $fav"
            showProgressDialog(this)
            mainViewModel.getIDUser().observe(this, { uid ->
                mainViewModel.editProfile(name.text.toString(), bio.text.toString(), uid!!,fav)
                    .observe(this,
                        {
                            hideProgressDialog()
                            if (it) {
                                Toast.makeText(
                                    this,
                                    "You have changed your profile.",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "please try again. something wrong ",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
            })
        }
    }

    private fun init() {
        name = binding.editProfileName
        bio = binding.editProfileBio
        save = binding.editSaveBtn
        checkBook = binding.checkBook
        checkCook = binding.checkCooking
        checkDance = binding.checkDance
        checkEnvironment = binding.checkEnvironment
        checkMovie = binding.checkMovie
        checkMusic = binding.checkMusic
        checkTech = binding.checkTechnology
        checkTravel = binding.checkTravel
        name.setText(user.name)
        bio.setText(user.bio)

    }
}