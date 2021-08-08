package com.example.pnetworking.ui.connection

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

class UserList(val user: User,val context:Context) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val name = viewHolder.itemView.findViewById<TextView>(R.id.user_row_name)
        val desc = viewHolder.itemView.findViewById<TextView>(R.id.user_row_bio)
        val img = viewHolder.itemView.findViewById<CircleImageView>(R.id.user_row_image)
        val online = viewHolder.itemView.findViewById<ImageView>(R.id.profile_online)
        name.text = user.emailText
        desc.text = user.bio
        Log.d("image",user.imageProfile)
        if (user.imageProfile.trim()!="true") {
            Picasso.get().load(Uri.parse(user.imageProfile)).into(img)
            if(user.imageProfile==""){
                img.setBackgroundResource(R.drawable.group)
            }
        }
        else {
            img.setBackgroundResource(R.drawable.user)
        }
        if(user.online)
            online.visibility= View.VISIBLE
        else
            online.visibility=View.GONE
    }
    override fun getLayout(): Int {
        return R.layout.user_row
    }

}