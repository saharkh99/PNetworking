package com.example.pnetworking.ui.userchat

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.pnetworking.R
import com.example.pnetworking.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class UserProList(val user: User, val context:Context,val numInt:Int) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val name = viewHolder.itemView.findViewById<TextView>(R.id.pro_card_name)
        val num = viewHolder.itemView.findViewById<TextView>(R.id.pro_card_num)
        val img = viewHolder.itemView.findViewById<CircleImageView>(R.id.pro_card_image)
        Log.d("user name","hiiiiiiiiiiiiiiiiiiiiiiiii")
        name.text = user.name.capitalize()
        num.text=numInt.toString()
        if (user.imageProfile.trim()!="true") {
            Picasso.get().load(Uri.parse(user.imageProfile)).into(img)
        }

    }
    override fun getLayout(): Int {
        return R.layout.user_small_row
    }

}