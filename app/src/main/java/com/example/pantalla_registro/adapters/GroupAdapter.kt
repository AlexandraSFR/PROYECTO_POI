package com.example.pantalla_registro.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla_registro.R
import com.example.pantalla_registro.models.GroupMessage
import com.example.pantalla_registro.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.item_group_image.view.*
import kotlinx.android.synthetic.main.item_message_image.view.*
import java.io.File

class GroupAdapter(private val group : String) : RecyclerView.Adapter<GroupAdapter.GroupChatViewHolder>() {

    private var db = Firebase.firestore
    var Postmessage : List<GroupMessage> = emptyList()

    fun setData(list: List<GroupMessage>){
        Postmessage = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatViewHolder {
        return GroupChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_group_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupChatViewHolder, position: Int) {
        Postmessage[position].image
        if(Postmessage[position].image != ""){
            holder.itemView.ConstraintGroupNoImage.visibility = View.GONE
            holder.itemView.EmailImage.text = Postmessage[position].from
            holder.itemView.FechaImage.text = Postmessage[position].dob.toString()
            holder.itemView.MensajeImage.text = Postmessage[position].message
            val storageRef = FirebaseStorage.getInstance().reference.child("fotos_mensajes/${Postmessage[position].image}")
            val localfile = File.createTempFile("tempImage","jpg")
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                holder.itemView.ImagenImagen.setImageBitmap(bitmap)
            }
            db.collection("users").document(Postmessage[position].from).get().addOnSuccessListener{users ->
                val listChats = users.toObject(User::class.java)
                if (listChats != null) {
                    var pfpic = listChats.pfp
                    if(pfpic.isNotEmpty()){
                        val storageRef = FirebaseStorage.getInstance().reference.child("fotos_usuarios/${pfpic}")
                        val localfile = File.createTempFile("tempImage","jpg")
                        storageRef.getFile(localfile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                            holder.itemView.profilePicSelectImage.setImageBitmap(bitmap)
                        }
                    }
                }


            }
        }
        else{
            holder.itemView.ConstraintGroupImage.visibility = View.GONE
            holder.itemView.Email.text = Postmessage[position].from
            holder.itemView.Fecha.text = Postmessage[position].dob.toString()
            holder.itemView.Mensaje.text = Postmessage[position].message
            db.collection("users").document(Postmessage[position].from).get().addOnSuccessListener{users ->
                val listChats = users.toObject(User::class.java)
                if (listChats != null) {
                    var pfpic = listChats.pfp
                    if(pfpic.isNotEmpty()){
                        val storageRef = FirebaseStorage.getInstance().reference.child("fotos_usuarios/${pfpic}")
                        val localfile = File.createTempFile("tempImage","jpg")
                        storageRef.getFile(localfile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                            holder.itemView.profilePicSelect.setImageBitmap(bitmap)
                        }
                    }
                }


            }
        }


    }

    override fun getItemCount(): Int {
        return Postmessage.size
    }

    class GroupChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
