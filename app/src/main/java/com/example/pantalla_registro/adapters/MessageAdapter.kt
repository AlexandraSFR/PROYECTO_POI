package com.example.pantalla_registro.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla_registro.models.Message
import com.example.pantalla_registro.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_group.view.*
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.item_message.view.myMessageLayout
import kotlinx.android.synthetic.main.item_message.view.myMessageTextView
import kotlinx.android.synthetic.main.item_message.view.otherMessageLayout
import kotlinx.android.synthetic.main.item_message.view.othersMessageTextView
import kotlinx.android.synthetic.main.item_message_image.view.*
import java.io.File

class MessageAdapter(private val user: String): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: List<Message> = emptyList()

    fun setData(list: List<Message>){
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_message_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        if(user == message.from){
            holder.itemView.myMessageLayout.visibility = View.VISIBLE
            holder.itemView.otherMessageLayout.visibility = View.GONE
            holder.itemView.myMessageImageLayout.visibility = View.GONE
            holder.itemView.otherMessageImageLayout.visibility = View.GONE


            holder.itemView.myMessageNOImage.text = message.message
            if(message.image != "") {
                holder.itemView.myMessageLayout.visibility = View.GONE
                holder.itemView.myMessageImageLayout.visibility = View.VISIBLE
                holder.itemView.myMessageMessage.text = message.message
                val storageRef = FirebaseStorage.getInstance().reference.child("fotos_mensajes/${message.image}")
                val localfile = File.createTempFile("tempImage","jpg")
                storageRef.getFile(localfile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    holder.itemView.myMessageImage.setImageBitmap(bitmap)
                }
            }

        } else {
            holder.itemView.myMessageLayout.visibility = View.GONE
            holder.itemView.otherMessageLayout.visibility = View.VISIBLE
            holder.itemView.myMessageImageLayout.visibility = View.GONE
            holder.itemView.otherMessageImageLayout.visibility = View.GONE

            holder.itemView.othersMessageNOImage.text = message.message
            if(message.image != ""){
                holder.itemView.otherMessageImageLayout.visibility = View.VISIBLE
                holder.itemView.otherMessageImage.visibility = View.VISIBLE
                holder.itemView.otherMessageMessage.text = message.message
                val storageRef = FirebaseStorage.getInstance().reference.child("fotos_mensajes/${message.image}")
                val localfile = File.createTempFile("tempImage","jpg")
                storageRef.getFile(localfile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    holder.itemView.otherMessageImage.setImageBitmap(bitmap)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}