package com.example.pnetworking.ui.base.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivitySignupBinding
import com.example.pnetworking.ui.MainActivity
import com.example.pnetworking.ui.base.signin.SigninActivity
import com.example.pnetworking.utils.ChatActivity
import com.google.android.gms.auth.api.signin.SignInAccount
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupActivity : ChatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var image: CircleImageView
    lateinit var birthday: EditText
    lateinit var account:TextView
    var gender: String = ""
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
        account.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performanceRegistration() {
        if (email.text.toString().isEmpty()) {
            email.error = "Please enter your email"
            return
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Please enter your password"
            return
        }
        if(passwordCharValidation(password.text.toString().trim())){
            password.error = "make sure your password contains capital letter and numbers"
            return
        }
        showProgressDialog(this)
        Log.d("TAG", "Attempting to create user with email: $email")
        Log.d("email", email.text.toString())
        mainViewModel.signup(email.text.toString(), password.text.toString())
            .observe(this, Observer { s ->
                if (s == "true") {
                    if (selectedPhotoUri == null) selectedPhotoUri = Uri.EMPTY
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
                                        hideProgressDialog()
                                        val intent = Intent(this, SigninActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                    })
                            } else {
                                showErrorSnackBar(
                                    "something wrong with your image",
                                    this,
                                    binding.signupLayout
                                )
                                hideProgressDialog()
                            }
                        })

                } else {
                    showErrorSnackBar(s, this, binding.signupLayout)
                    hideProgressDialog()
                }
            })

    }

    private fun init() {
        email = binding.signupEmailEt
        password = binding.signupPasswordEt
        image = binding.signupImage
        birthday = binding.signinBirthday
        account=binding.signupAccount
        if (binding.radioButton1.isChecked) {
            gender = "female"
        }
        if (binding.radioButton2.isChecked) {
            gender = "male"
        }
        if (binding.radioButton3.isChecked) {
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
    fun passwordCharValidation(passwordEd: String): Boolean {
        val PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[@_.]).*$"
        val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
        return !pattern.matcher(passwordEd).matches()
    }

}