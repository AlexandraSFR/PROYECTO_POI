package com.example.pantalla_registro.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.DAO.ChatDAO
import com.example.pantalla_registro.R
import com.example.pantalla_registro.Singleton.Sesion
import com.example.pantalla_registro.activities.ChatActivity
import com.example.pantalla_registro.adapters.ChatAdapter
import com.example.pantalla_registro.models.Chat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list_of_chats.*
import kotlinx.android.synthetic.main.activity_list_of_chats.listChatsRecyclerView
import kotlinx.android.synthetic.main.fragment_message.*
import java.util.*


class MessageFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragments: MenuItem;
    private lateinit var newChatButton: Button;
    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_list_of_chats,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view);

    }

    private fun initViews(view: View) {
        newChatButton = view.findViewById(R.id.gotoChats);
        newChatButton.setOnClickListener { newChat() }

        listChatsRecyclerView.layoutManager = LinearLayoutManager(context)
        listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        // val userRef = db.collection("users").document(user)
        val listChats = listOf<com.example.pantalla_registro.models.Chat>()
        databaseReference.child("Chat").addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach() { chat ->
                    val child = chat.child("user1").value.toString();
                    val child2 = chat.child("user2").value.toString();
                    if(child.equals(Sesion.email) || child2.equals(Sesion.email)){
                        val chats = Chat(
                            id = chat.child("id").value.toString(),
                            name = chat.child("name").value.toString(),
                            //user1 = chat.child("user1").value.toString(),
                            //user2 = chat.child("user2").value.toString()
                        )
                         listChats.toMutableList().add(chats);

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        });
        (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)

        /*userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)

                (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
            }

        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error == null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)

                        (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }*/
    }

    private fun chatSelected(chat: Chat){
        val intent = Intent(activity, ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        startActivity(intent)
    }

    private fun newChat() {
        var chatId = "";
        val otherUser = newChatText.text.toString()

        val chat = Chat(
            id = "",
            name = "Chat with $otherUser",
            //user1 = Sesion.email.toString(),
            //user2 = otherUser
        )
        ChatDAO.addChat(chat).addOnSuccessListener {
            databaseReference.child("Chat").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach() { chat ->
                        val child = chat.child("user1").value.toString();
                        val child2 = chat.child("user2").value.toString();
                        if (otherUser.equals(child2) && Sesion.equals(child)) {
                            val idMap: HashMap<String, Any> = HashMap();
                            idMap.put("id", chat.key.toString());
                            ChatDAO.update(chat.key.toString(), idMap);
                            chatId = chat.key.toString();
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            });


            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra("chatId", chatId)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return true
    }
}