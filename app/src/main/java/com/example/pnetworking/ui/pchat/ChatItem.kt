package com.example.pnetworking.ui.pchat
import android.content.Context
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
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*


class ChatItem(val context: Context, val text: Message, val image: String, val type:String, val reply:String) : Item<GroupieViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val textview = viewHolder.itemView.findViewById<TextView>(R.id.chat_message_tv)
        val textReply = viewHolder.itemView.findViewById<TextView>(R.id.chat_message__reply_tv)
        val image_message=viewHolder.itemView.findViewById<CircleImageView>(R.id.chat_image_user)
        val image_Text=viewHolder.itemView.findViewById<ImageView>(R.id.chat_message_image_tv)
        val time=viewHolder.itemView.findViewById<TextView>(R.id.chat_massage_date)
        val seen=viewHolder.itemView.findViewById<TextView>(R.id.chat_is_seen_tv)
        if(type=="date"){
            val textview = viewHolder.itemView.findViewById<TextView>(R.id.chat_date)
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val date = Date(text.timestamp * 1000)
            textview.text=sdf.format(date)
        }
        else{
            if(text.seen){
                Log.d("seenn ",text.seen.toString())
                seen.text = "seen"
            }
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
            if(type=="false"){
                image_message.visibility=View.GONE
            }
//        if (uri != "" && !type)
//            Picasso.get().load(uri).into(imageView)
            val sdf = SimpleDateFormat("hh:mm a")
            val date = Date(text.timestamp * 1000)
            Log.d("date2", sdf.format(date))
            time.text=sdf.format(date)
        }

    }
    override fun getLayout(): Int {
        if (type == "true")
            return R.layout.row_from_chat
        else if (type == "false")
            return R.layout.row_to_chat
        else
            return R.layout.row_date_message

    }
}