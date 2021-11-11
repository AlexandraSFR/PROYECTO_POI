package com.example.pantalla_registro.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.pantalla_registro.R
import com.example.pantalla_registro.models.GroupMessage
import com.example.pantalla_registro.models.Message
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_upload_image.*
import kotlinx.android.synthetic.main.registro.*
import java.text.SimpleDateFormat
import java.util.*

class UploadImageGroupActivity : AppCompatActivity() {

    private var user = ""
    private var chatId = ""
    private var chatName = ""

    private var db = Firebase.firestore

    lateinit var ImageUri : Uri
    private var isSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("chatName")?.let { chatName = it }
        intent.getStringExtra("user")?.let { user = it }

        if(chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }
    }

    private fun initViews() {
        isSelected = false
        goBack.setOnClickListener{GoBack()}
        ImagenImagen.setOnClickListener{selectImage()}
        Upload.setOnClickListener{UploadMessage()}
    }

    private fun UploadMessage() {
        if(isSelected == true){
            var imagen = uploadImage()
            val message = GroupMessage(
                from = user,
                message = ImagenMensaje.text.toString(),
                image = imagen

            )

            db.collection("gchat").document(chatId).collection("messages").document().set(message)
            val intent = Intent(this, GroupChatActivity::class.java)
            intent.putExtra("chatId",chatId)
            intent.putExtra("chatName", chatName)
            intent.putExtra("user", user)
            startActivity(intent)
        }
        else{
            Toast.makeText(this@UploadImageGroupActivity,"Ingrese una imagen para mandar", Toast.LENGTH_SHORT).show()
        }


    }

    private fun uploadImage():String {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Subiendo foto ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)

        val storageReference = FirebaseStorage.getInstance().reference.child("fotos_mensajes/${fileName}")

        storageReference.putFile(ImageUri).addOnSuccessListener {
            ImagenImagen.setImageURI(null)
            Toast.makeText(this@UploadImageGroupActivity,"Foto subida con exito", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            if(progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this@UploadImageGroupActivity,it.message, Toast.LENGTH_SHORT).show()
        }
        //user does not have permision to access this objetc
        isSelected = false;
        return fileName;
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
        isSelected = true;

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            ImagenImagen.setImageURI(ImageUri)
            isSelected = true;
        }
    }

    private fun GoBack() {
        val intent = Intent(this, GroupChatActivity::class.java)
        intent.putExtra("chatId",chatId)
        intent.putExtra("chatName", chatName)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        db.collection("users").document(user).update("status","Offline")

    }

    override fun onResume(){
        super.onResume()
        db.collection("users").document(user).update("status","Online")


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, GroupChatActivity::class.java)
        intent.putExtra("chatId",chatId)
        intent.putExtra("chatName", chatName)
        intent.putExtra("user", user)
        startActivity(intent)
    }


}