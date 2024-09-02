package com.ifsha.iguruassignment.home

import com.ifsha.iguruassignment.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.ifsha.iguruassignment.network.User

class UserAdapter(
    val context: Context,
    var list: List<User>
): Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View): ViewHolder(itemView) {

        val picture: ImageView = itemView.findViewById(R.id.avatarImageView)
        val fullName: TextView = itemView.findViewById(R.id.nameTextView)
        val emailId: TextView = itemView.findViewById(R.id.emailTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_list_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(data: List<User>) {
        list = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = list[position]

        holder.emailId.text = user.email
        holder.fullName.text = "${user.first_name} ${user.last_name}"

        Glide.with(context)
            .load(user.avatar)
            .centerCrop()
            .into(holder.picture)
    }
}