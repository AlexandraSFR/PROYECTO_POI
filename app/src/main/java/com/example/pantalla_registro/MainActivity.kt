package com.example.pantalla_registro


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.pantalla_registro.DAO.UserDAO
import com.example.pantalla_registro.Fragment.MainMenu
import com.example.pantalla_registro.activities.LoginActivity
import com.example.pantalla_registro.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.registro_usuario.*


class MainActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var buttonLogin: Button;
    private lateinit var buttonRegister: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        

        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_usuario)
        val spinner =findViewById<Spinner>(R.id.spinner2)
        //val lista= listOf("LMAD", "LA", "LM", "LF", "LCC", "LSTI")
        val lista = resources.getStringArray(R.array.Carreras)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)
        spinner.adapter =adaptador
        initViews()
       // registrar.setOnClickListener { createUser() }


    }
    private fun initViews(){
        buttonLogin = findViewById(R.id.cancelar);
        buttonLogin.setOnClickListener{ goToLogin() }
        buttonRegister = findViewById(R.id.registrar);
        buttonRegister.setOnClickListener{ createUser() }

    }
    private fun createUser(){

        val nombre=editTextTextPersonName3.text.toString()
        val apellidos=editTextTextPersonName4.text.toString()
        val usuario=editTextTextPersonName5.text.toString()
        val email = editTextTextEmailAddress.text.toString()
        val password = editTextTextPassword2.text.toString()
        val carrera = spinner2.selectedItem.toString()

        val user = User("",nombre,apellidos,email,password,carrera,usuario);

        UserDAO.addUser(user).addOnSuccessListener{
            databaseReference.child("User").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach(){usuario->
                        val child = usuario.child("email").value.toString();
                        if(email.equals(child) ){
                            val idMap : HashMap<String,Any> = HashMap();
                            idMap.put("id",usuario.key.toString());
                            UserDAO.update(usuario.key.toString(),idMap);
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            });

        };
        val intent =Intent(this, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
    private fun goToLogin(){
        val intent =Intent(this, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun goToRegister(){
        val intent =Intent(this, MainMenu::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun checkUser(){
        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this, MainMenu::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)

            finish()
        }
    }


}