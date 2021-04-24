package com.example.pnetworking.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.example.pnetworking.R
import com.google.android.material.snackbar.Snackbar


private lateinit var mProgressDialog: Dialog
private var doubleBackToExitPressedOnce = false

abstract class ChatActivity : AppCompatActivity(), ChatView {
    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            "please click back again to exit",
            Toast.LENGTH_SHORT
        ).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}

abstract class ChatFragments : Fragment(), ChatView {

}

interface ChatView {
    fun showProgressDialog(context: Context) {
        mProgressDialog = Dialog(context)
        mProgressDialog.setContentView(R.layout.view_loading)
        mProgressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mProgressDialog.show()
        mProgressDialog.setCancelable(false)
    }

    fun showProgressDialog2(context: Context) {
        mProgressDialog = Dialog(context)
        mProgressDialog.setContentView(R.layout.view_loading)
        mProgressDialog.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        mProgressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        mProgressDialog.show()
        mProgressDialog.setCancelable(false)
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun showErrorSnackBar(message: String, content: Context, view: View) {
        val snackBar =
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                content,
                R.color.purple_200
            )
        )
        snackBar.show()
    }


}

abstract class ChatViewmodel : ViewModel() {
//    val compositeDisposable:CompositeDisposable()
//    override fun onCleared() {
//        compositeDisposable.clear()
//        super.onCleared()
//    }
}