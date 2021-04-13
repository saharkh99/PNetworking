package com.example.pnetworking.ui.connection

import android.widget.TextView
import com.example.pnetworking.R
import com.example.pnetworking.models.User
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class UserList(val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val name = viewHolder.itemView.findViewById<TextView>(R.id.user_row_name)
        val desc = viewHolder.itemView.findViewById<TextView>(R.id.user_row_bio)
        name.text = user.name
        desc.text = user.emailText

    }
    override fun getLayout(): Int {
        return R.layout.user_row
    }

}