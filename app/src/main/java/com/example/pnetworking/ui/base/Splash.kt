package com.example.pnetworking.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.pnetworking.R
import com.example.pnetworking.ui.MainActivity
import com.example.pnetworking.ui.base.intro.IntroActivity
import com.example.pnetworking.ui.base.signin.SigninActivity
import com.example.pnetworking.ui.base.signup.SignupActivity
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({
            val currentUserID = FirebaseAuth.getInstance().currentUser
            if (currentUserID!=null) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, 2500)
    }
}