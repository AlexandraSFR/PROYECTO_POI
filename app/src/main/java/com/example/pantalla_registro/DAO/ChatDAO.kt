package com.example.pantalla_registro.DAO

import com.example.pantalla_registro.models.Chat
import com.example.pantalla_registro.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatDAO {
    companion object{
        private val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(User::class.java.simpleName)

        fun addChat(chat : Chat): Task<Void> {
            return databaseReference.push().setValue(chat);
        }
        fun update(key: String, hashMap: HashMap<String, Any>): Task<Void> {

            return  databaseReference.child(key).updateChildren(hashMap);
        }
    }
}