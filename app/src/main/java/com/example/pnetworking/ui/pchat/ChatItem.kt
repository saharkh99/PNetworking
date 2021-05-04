package com.example.pnetworking.ui.pchat
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.pnetworking.models.Message
import com.example.pnetworking.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView


class ChatItem(val text: Message, val image: String, val type:Boolean) : Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val textview = viewHolder.itemView.findViewById<TextView>(R.id.chat_message_tv)
        val image_message=viewHolder.itemView.findViewById<CircleImageView>(R.id.chat_image)
        if(text.type=="photo"){
            image_message.visibility= View.VISIBLE
            textview.visibility= View.GONE
            Log.d("chat",text.imageUrl)
            Picasso.get().load(Uri.parse(text.imageUrl)).into(image_message)
        }
        else{
            image_message.visibility= View.GONE
            textview.visibility= View.VISIBLE
            textview.text = text.context
        }
        val uri = image
        val imageView = viewHolder.itemView.findViewById<ImageView>(R.id.chat_image_user)
        if (uri != "")
            Picasso.get().load(uri).into(imageView)
    }
    override fun getLayout(): Int {
        if(type)
          return R.layout.row_from_chat
        else
            return R.layout.row_to_chat

    }
}