package com.example.pnetworking.ui.groupchat

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pnetworking.R
import com.example.pnetworking.models.User
import com.example.pnetworking.ui.profile.FollowViewModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.viewmodel.ext.android.viewModel

class UserChangeStatusItem(
    val user: User,
    val context: Context,
    val text: String,
    val text2: String,
    val rol: String,
    val viewmodel: ViewModel
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
            val id=user.id
            if(text=="disconnect"){
                val v=viewmodel as FollowViewModel
                v.disconnect(user.id)
                v.decreaseConnections(user.id)
                button.text=text2
                GroupProfileFragment.adapter.notifyItemRemoved(position)
                Toast.makeText(context,"disconnected successfully",Toast.LENGTH_LONG)
                v.changeConnection(false)

            }
            else if(text=="remove"){
                val v=viewmodel as FollowViewModel
                v.deleteRequest(user.id)
                v.changeConnection(false)
            }
            else if(text=="ADD"){

                if(button.text=="REMOVE USER"){
                    val v=viewmodel as GroupViewModel
                    v.removeAddedMembers(position.toString())
                    button.text=text
                }
                else{
                    val v=viewmodel as GroupViewModel
                    v.setAddedMembers(position.toString())
                    button.text=text2
                }
            }


        }
        if(rol!=""){
            val rol = viewHolder.itemView.findViewById<TextView>(R.id.user_row_role)
            rol.visibility= View.VISIBLE
        }
    }

    override fun getLayout(): Int {
        return R.layout.row_changing_status_user
    }

}