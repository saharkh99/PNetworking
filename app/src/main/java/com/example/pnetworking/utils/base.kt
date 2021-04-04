package com.example.pnetworking.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.example.pnetworking.R


private lateinit var mProgressDialog: Dialog

abstract class ChatActivity : AppCompatActivity(), ChatView {

}

abstract class ChatFragment : Fragment(), ChatView {

}

interface ChatView {

    fun showProgressDialog(show: Boolean, context: Context) {
        mProgressDialog = Dialog(context)
        mProgressDialog.setContentView(R.layout.view_loading)
        mProgressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (show)
            mProgressDialog.show()
        else
            mProgressDialog.cancel()
    }

}

abstract class ChatViewmodel : ViewModel() {
//    val compositeDisposable:CompositeDisposable()
//    override fun onCleared() {
//        compositeDisposable.clear()
//        super.onCleared()
//    }
}