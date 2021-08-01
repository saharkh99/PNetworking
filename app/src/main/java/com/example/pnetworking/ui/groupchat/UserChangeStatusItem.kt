package com.example.pnetworking.ui.groupchat

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.R
import com.example.pnetworking.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class UserChangeStatusItem(
    val user: User,
    val context: Context,
    val text: String,
    val text2: String,
    val status: MutableLiveData<String>
) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val name = viewHolder.itemView.findViewById<TextView>(R.id.user_row_name)
        val button = viewHolder.itemView.findViewById<Button>(R.id.add_user)
        val img = viewHolder.itemView.findViewById<CircleImageView>(R.id.user_row_image)
        name.text = user.name.capitalize()
        if (user.imageProfile.trim() != "true") {
            Picasso.get().load(Uri.parse(user.imageProfile)).into(img)
        }
        button.text = text
        button.setOnClickListener {
            button.text = text2
            status.value = "true"
        }


    }

    override fun getLayout(): Int {
        return R.layout.row_changing_status_user
    }

}