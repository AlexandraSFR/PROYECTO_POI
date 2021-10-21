package com.example.pantalla_registro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.Button
import kotlinx.android.synthetic.main.login_form.*
import android.widget.Toast
import com.example.pantalla_registro.activities.ListOfChatsActivity

class RegisterActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private lateinit var buttonRegister: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_form)
        initViews()

        iniciar_sesion.setOnClickListener { loginUser() }
        //registrarse.setOnClickListener { createUser() }

        //checkUser()
    }
    private fun initViews(){
        buttonRegister = findViewById(R.id.registrarse);
        buttonRegister.setOnClickListener{ goToRegistrarse() }
    }

    private fun checkUser(){
        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this, ListOfChatsActivity::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)

            finish()
        }
    }

    private fun createUser(){
        /*val email = emailText.text.toString()
        val password = passwordText.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"User created. Logging in...",Toast.LENGTH_LONG).show();
                checkUser()
            } else {
                task.exception?.let {
                    Toast.makeText(baseContext, it.message, Toast.LENGTH_LONG).show()
                }
            }

        }*/
    }
    private fun goToRegistrarse(){
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
    private fun loginUser(){
        val email = correo.text.toString()
        val password = contra.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    checkUser()
                } else {
                    task.exception?.let {
                        Toast.makeText(baseContext, it.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
        else{
            Toast.makeText(applicationContext,"Favor de llenar los campos",Toast.LENGTH_LONG).show();
        }

    }
}