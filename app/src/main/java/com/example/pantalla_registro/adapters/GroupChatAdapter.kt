package com.example.pantalla_registro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla_registro.models.GroupChat
import com.example.pantalla_registro.R
import com.example.pantalla_registro.models.Chat
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.item_groupchat.view.*
import kotlinx.android.synthetic.main.item_groupchat.view.usersTextView

class GroupChatAdapter(val chatClick: (GroupChat) -> Unit): RecyclerView.Adapter<GroupChatAdapter.ChatViewHolder>()  {
    var groups: List<GroupChat> = emptyList()
    fun setData(list: List<GroupChat>){
        groups = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_groupchat,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.itemView.groupNameText.text = groups[position].Nombre
        holder.itemView.usersTextView.text = "Alumnos de " + groups[position].Nombre;

        holder.itemView.setOnClickListener {
            chatClick(groups[position])
        }
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


}