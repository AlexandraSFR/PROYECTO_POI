package com.example.pantalla_registro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla_registro.R
import com.example.pantalla_registro.models.GroupChat
import com.example.pantalla_registro.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_group_contacts.view.*

class AddUserAdapter(val userClick: (User) -> Unit) : RecyclerView.Adapter<AddUserAdapter.GroupUserViewHolder>() {

    var Users : List<User> = emptyList()
    private var db = Firebase.firestore
    var isclicked : Boolean = false;
    fun setData(list: List<User>){
        Users = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupUserViewHolder {
        return GroupUserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_group_contacts,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupUserViewHolder, position: Int) {


        holder.itemView.Email.text = Users[position].email
        holder.itemView.Username.text = Users[position].user
        holder.itemView.Status.text = Users[position].status


        holder.itemView.setOnClickListener{
            if(Users[position].addGroup==false){
                holder.itemView.addUser.setBackgroundResource(R.drawable.ic_confirmed)
                Users[position].addGroup = true;
            }
            else{
                holder.itemView.addUser.setBackgroundResource(R.drawable.ic_baseline_add_circle_24)
                Users[position].addGroup = false;
            }
        }
    }

    override fun getItemCount(): Int {
        return Users.size
    }

    fun getAddList(groupId: String, groupName:String,userActual :String): MutableList<User> {
        var usersG : MutableList<User> = mutableListOf()

        val grupo = GroupChat(
            Nombre = groupName,
            id = groupId
        )
        for(u in Users){
            if(u.addGroup==true){
                if(userActual!=u.email){
                    db.collection("users").document(u.email).collection("groups").document(groupId).set(grupo)
                }
            }
        }
        return usersG
    }

    class GroupUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


}
