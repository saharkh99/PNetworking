package com.example.pnetworking.ui.base.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivitySigninBinding
import com.example.pnetworking.databinding.ActivitySignupBinding
import com.example.pnetworking.ui.MainActivity
import com.example.pnetworking.ui.base.signup.SignUpViewModel
import com.example.pnetworking.utils.ChatActivity
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.viewmodel.ext.android.viewModel

class SigninActivity : ChatActivity() {
    lateinit var binding: ActivitySigninBinding
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var start: Button
    private val mainViewModel by viewModel<SignInViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        start.setOnClickListener {
            signin()
        }
    }

    private fun signin() {
        if (email.text.toString().isEmpty()) {
            email.error = "Please enter your email"
            return
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Please enter your password"
            return
        }
        showProgressDialog(this)
        mainViewModel.signin(email.text.toString().trim(), password.text.toString().trim())
            .observe(this,
                Observer {
                    if (it == "true") {
                        hideProgressDialog()
                        Toast.makeText(
                            this,
                            "You have successfully signed in.",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            this,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

    }

    private fun init() {
        email = binding.signinEmail
        password = binding.signinPassword
        start = binding.signinStart
    }
}