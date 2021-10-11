package com.example.pantalla_registro


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.registro_usuario.*


class MainActivity : AppCompatActivity() {
    private val auth = Firebase.auth
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
        buttonRegister.setOnClickListener{ goToRegister() }

    }
    private fun createUser(){
       // val nombre=editTextTextPersonName3.text.toString()
      //  val apellidos=editTextTextPersonName4.text.toString()
       // val usuario=editTextTextPersonName5.text.toString()
        val email = editTextTextEmailAddress.text.toString()
        val password = editTextTextPassword2.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"User created. Logging in...",Toast.LENGTH_LONG).show();
               // checkUser()
            } else {
                task.exception?.let {
                    Toast.makeText(baseContext, it.message, Toast.LENGTH_LONG).show()
                }
            }

        }
    }
    private fun goToLogin(){
        val intent =Intent(this,LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun goToRegister(){
        val intent =Intent(this,MainMenu::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }


}