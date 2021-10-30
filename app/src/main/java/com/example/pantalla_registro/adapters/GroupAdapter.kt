package com.example.pantalla_registro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla_registro.R
import com.example.pantalla_registro.models.GroupMessage
import kotlinx.android.synthetic.main.item_group.view.*

class GroupAdapter(private val group : String) : RecyclerView.Adapter<GroupAdapter.GroupChatViewHolder>() {

    var Postmessage : List<GroupMessage> = emptyList()

    fun setData(list: List<GroupMessage>){
        Postmessage = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatViewHolder {
        return GroupChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_group,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupChatViewHolder, position: Int) {

        holder.itemView.Email.text = Postmessage[position].from
        holder.itemView.Fecha.text = Postmessage[position].dob.toString()
        holder.itemView.Mensaje.text = Postmessage[position].message

    }

    override fun getItemCount(): Int {
        return Postmessage.size
    }

    class GroupChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
