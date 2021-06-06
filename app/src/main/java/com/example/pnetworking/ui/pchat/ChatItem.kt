package com.example.pnetworking.ui.pchat
import android.content.Context
import android.icu.util.UniversalTimeScale.toLong
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.pnetworking.models.Message
import com.example.pnetworking.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext


class ChatItem(val context: Context, val text: Message, val image: String, val type:Boolean, val reply:String) : Item<GroupieViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val textview = viewHolder.itemView.findViewById<TextView>(R.id.chat_message_tv)
        val textReply = viewHolder.itemView.findViewById<TextView>(R.id.chat_message__reply_tv)
        val image_message=viewHolder.itemView.findViewById<CircleImageView>(R.id.chat_image_user)
        val image_Text=viewHolder.itemView.findViewById<ImageView>(R.id.chat_message_image_tv)
        val time=viewHolder.itemView.findViewById<TextView>(R.id.chat_massage_date)
        if(reply!=""){
            textReply.text=reply
            textReply.visibility=View.VISIBLE
        }
        if(text.type=="photo"){
            image_Text.visibility= View.VISIBLE
            textview.visibility= View.GONE
            Log.d("chat",text.imageUrl)
            Glide.with(context)
                .load(Uri.parse(text.imageUrl))
                .into(image_Text)
        }
        else{
            image_Text.visibility= View.GONE
            textview.visibility= View.VISIBLE
            textview.text = text.context
        }
        val uri = image
        if(!type){
            image_message.visibility=View.GONE
        }
//        if (uri != "" && !type)
//            Picasso.get().load(uri).into(imageView)
        val formatter =  SimpleDateFormat("hh:mm");
        val dateString = formatter.format( Date(toLong(text.timestamp,0)))
        Log.d("date",text.timestamp.toString())
        time.text=dateString
    }
    override fun getLayout(): Int {
        if(type)
          return R.layout.row_from_chat
        else
            return R.layout.row_to_chat

    }

}