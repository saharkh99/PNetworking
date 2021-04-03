package com.example.pnetworking.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.pnetworking.R
import com.example.pnetworking.ui.MainActivity

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({
            //   val currentUserID = FireStoreClass().getCurrentUserID()
            //  if (currentUserID.isNotEmpty()) {
            //   startActivity(Intent(this@SplashActivity, LatestMessageActivity::class.java))
            //  } else {
            startActivity(Intent(this, MainActivity::class.java))
            //  }
            finish()
        }, 2500)
    }
}