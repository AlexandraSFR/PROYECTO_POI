package com.example.pantalla_registro.activities

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.R
import com.example.pantalla_registro.Singleton.Sesion
import com.example.pantalla_registro.adapters.AddUserAdapter
import com.example.pantalla_registro.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File

import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class ProfileActivity : AppCompatActivity() {

    private var user = ""
    private var db = Firebase.firestore

    var pfpic: String ="";
    private val iv = "bVQzNFNhRkQ1Njc4UUFaWa=="

    private val secretKey = "tK5UTui+DPh(lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ="

    private val salt = "QWlGNHNhMTJTQWZ2bGhpV3U="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }


    }

    private fun initViews(){

        var encrypt : Boolean = false;
        var password: String = "";

        db.collection("users").document(user).get().addOnSuccessListener{users ->
            val listChats = users.toObject(User::class.java)
            if (listChats != null) {
                textNombre.setText(listChats.name)
                textCarrera.setText(listChats.carrera)
                textCorreo.setText(listChats.email)
                textUsuario.setText(listChats.user)
                encrypt = listChats.encrypted
                password = listChats.password
                pfpic = listChats.pfp
                if(pfpic.isNotEmpty()){
                    val storageRef = FirebaseStorage.getInstance().reference.child("fotos_usuarios/${pfpic}")
                    val localfile = File.createTempFile("tempImage","jpg")
                    storageRef.getFile(localfile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        profilePicSelect.setImageBitmap(bitmap)
                    }
                        .addOnFailureListener{
                            Toast.makeText(this,"Fallo al recibir la imagen",Toast.LENGTH_SHORT).show()
                        }
                }
            }


        }


        logOut.setOnClickListener { LogOut() }
        goChat.setOnClickListener { gotoChats() }
        goGroup.setOnClickListener { gotoGroups() }
        if(encrypt == true){
            Encriptar.setText("Desencriptar Contraseña")
        }else{
            Encriptar.setText("Encriptar Contraseña")
        }

        Encriptar.setOnClickListener{
            if(encrypt == true){
                password = decrypt(password)!!
                db.collection("users").document(user).update("encrypted",false)
                Encriptar.setText("Encriptar Contraseña")
                encrypt = false;
            }
            else{
                password = encrypt(password)!!
                db.collection("users").document(user).update("encrypted",true)
                Encriptar.setText("Desencriptar Contraseña")
                encrypt = true;
            }
            db.collection("users").document(user).update("password",password)
        }




    }

    fun encrypt(strToEncrypt: String) :  String?
    {
        try
        {
            val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec)
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
            return android.util.Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), android.util.Base64.DEFAULT)
        }
        catch (e: Exception)
        {
            println("Error while encrypting: $e")
        }
        return null
    }

    fun decrypt(strToDecrypt : String) : String? {
        try
        {

            val ivParameterSpec =  IvParameterSpec(android.util.Base64.decode(iv, android.util.Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")

            val spec =  PBEKeySpec(secretKey.toCharArray(), android.util.Base64.decode(salt, android.util.Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec);
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return  String(cipher.doFinal(android.util.Base64.decode(strToDecrypt, android.util.Base64.DEFAULT)))
        }
        catch (e : Exception) {
            println("Error while decrypting: $e");
        }
        return null
    }

    override fun onPause() {
        super.onPause()
        db.collection("users").document(user).update("status","Offline")

    }


    override fun onResume(){
        super.onResume()
        db.collection("users").document(user).update("status","Online")


    }
    private fun LogOut(){

        FirebaseAuth.getInstance().signOut()
        db.collection("users").document(user).update("status","Offline")
        Sesion.Logout()
        val intent = Intent(this,LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
    private  fun gotoGroups(){
        val intent = Intent(this, ListOfGroupsActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }

    private  fun gotoChats(){
        val intent = Intent(this, ListOfChatsActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }


}