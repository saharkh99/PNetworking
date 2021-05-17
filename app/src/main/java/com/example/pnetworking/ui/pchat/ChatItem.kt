package com.example.pnetworking.ui.pchat
import android.icu.util.UniversalTimeScale.toLong
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.pnetworking.models.Message
import com.example.pnetworking.R
import com.example.pnetworking.ui.base.test.s
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class ChatItem(val text: Message, val image: String, val type:Boolean) : Item<GroupieViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val textview = viewHolder.itemView.findViewById<TextView>(R.id.chat_message_tv)
        val image_message=viewHolder.itemView.findViewById<CircleImageView>(R.id.chat_image_user)
        val image_Text=viewHolder.itemView.findViewById<ImageView>(R.id.chat_message_image_tv)
        val time=viewHolder.itemView.findViewById<TextView>(R.id.chat_massage_date)
        if(text.type=="photo"){
            image_Text.visibility= View.VISIBLE
            textview.visibility= View.GONE
            Log.d("chat",text.imageUrl)
            Picasso.get().load(Uri.parse(text.imageUrl)).into(image_Text)
        }
        else{
            image_Text.visibility= View.GONE
            textview.visibility= View.VISIBLE
            textview.text = text.context
        }
        val uri = image
//        if (uri != "" && !type)
//            Picasso.get().load(uri).into(imageView)
        val formatter =  SimpleDateFormat("hh:mm");
        val dateString = formatter.format( Date(toLong(text.timestamp,0)));
        time.text=dateString
    }
    override fun getLayout(): Int {
        if(type)
          return R.layout.row_from_chat
        else
            return R.layout.row_to_chat

    }
}