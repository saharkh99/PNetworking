package com.example.pnetworking.ui.base.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivitySignupBinding
import com.example.pnetworking.databinding.ActivityTestBinding
import com.example.pnetworking.ui.MainActivity
import com.example.pnetworking.ui.base.test.TestViewModel
import com.example.pnetworking.utils.ChatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.viewmodel.ext.android.viewModel

class SignupActivity : ChatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var image: CircleImageView
    lateinit var birthday: EditText
     var gender: String=""
    lateinit var start: Button
    var selectedPhotoUri: Uri? = null
    private val mainViewModel by viewModel<SignUpViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        start.setOnClickListener {
            performanceRegistration()
        }
        image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }


    }

    private fun performanceRegistration() {
        if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter text in email/password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("TAG", "Attempting to create user with email: $email")
        Log.d("email", email.text.toString())
        mainViewModel.signup(email.text.toString(), password.text.toString())
            .observe(this, Observer {
                if (it) {
                    mainViewModel.uploadImageToFirebaseStorage(selectedPhotoUri!!)
                        .observe(this, Observer {
                            if (it != "false") {
                                mainViewModel.saveUserToFirebaseDatabase(
                                    email.text.toString(),
                                    birthday.text.toString(),
                                    gender,
                                    it
                                )
                                    .observe(this, Observer {
                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                    })
                            }
                        })
                }
            })

    }
    private fun init() {
        email = binding.signupEmailEt
        password = binding.signupPasswordEt
        image = binding.signupImage
        birthday = binding.signinBirthday
        if (binding.radioButton1.isSelected) {
            gender = "femail"
        }
        if (binding.radioButton2.isSelected) {
            gender = "male"
        }
        if (binding.radioButton3.isSelected) {
            gender = "other"
        }
        start = binding.signinStart
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("TAG", "Photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            image.setImageBitmap(bitmap)

        }
    }

}