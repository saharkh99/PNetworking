package com.example.pnetworking.ui.base.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.pnetworking.R
import com.example.pnetworking.databinding.ActivityMainBinding.inflate
import com.example.pnetworking.databinding.ActivityTestBinding
import com.example.pnetworking.databinding.TestRowLayoutBinding
import com.example.pnetworking.models.Question
import com.example.pnetworking.ui.base.signin.SigninActivity
import com.example.pnetworking.ui.base.signup.SignupActivity
import com.example.pnetworking.utils.ChatActivity
import com.google.android.gms.auth.api.signin.SignInAccount
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import org.koin.android.viewmodel.ext.android.viewModel

val s =IntArray(12)

var emotionScore = 0
var extroScore = 0

class TestActivity : ChatActivity() {
    private val mainViewModel by viewModel<TestViewModel>()
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val rec=binding.testRecyclerview
        mainViewModel.question.observe(this, Observer { list ->
            list.forEach{
                adapter.add(TestItem(it))
            }
        })
        rec.adapter=adapter
        binding.testFinish.setOnClickListener {
             for ( i in s.indices){
                 if(i<6){
                     emotionScore += s[i]
                 }
                 if(i>=6){
                     extroScore += s[i]
                 }
             }
            mainViewModel.storeScores(emotionScore, extroScore)
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }
    }
}
class TestItem(val question: Question) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.test_row_layout

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val testQuestion = viewHolder.itemView.findViewById<TextView>(R.id.questions_context)
        val yesAnswer = viewHolder.itemView.findViewById<Button>(R.id.questions_yes)
        yesAnswer.setOnClickListener {
            s[position]=1
            Log.d("position",s[position].toString()+position.toString())

        }
        val noAnswer = viewHolder.itemView.findViewById<Button>(R.id.questions_no)
        noAnswer.setOnClickListener {
            s[position]=0
            Log.d("position",s[position].toString()+position.toString())

        }
        testQuestion.text=question.title
    }
}