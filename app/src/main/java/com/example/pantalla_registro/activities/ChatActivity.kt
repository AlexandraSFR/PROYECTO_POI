package com.example.pantalla_registro.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.adapters.MessageAdapter
import com.example.pantalla_registro.R
import com.example.pantalla_registro.models.Message
import com.example.pantalla_registro.models.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_list_of_chats.*
import kotlinx.android.synthetic.main.item_group_contacts.*
import kotlinx.android.synthetic.main.registro.*
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    private var chatId = ""
    private var user = ""
    private var user1 = ""
    private var user2 = ""

    private var db = Firebase.firestore

    lateinit var  fusedLocationProviderClient: FusedLocationProviderClient;
    lateinit var locationRequest :  com.google.android.gms.location.LocationRequest;
    private  var PERMISSION_ID = 1;

    lateinit var ImageUri : Uri
    private var isSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }
        intent.getStringExtra("user1")?.let { user1 = it }
        intent.getStringExtra("user2")?.let { user2 = it }


        if(chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }
    }

    private fun initViews(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if(user1==user){
            Name.setText(user2);

            db.collection("users").document(user2).get().addOnSuccessListener {
                val stat = it.toObject(User::class.java)
                if (stat != null) {
                    StatusChat.setText(stat.status)
                    if(stat.status=="Online"){
                        StatusChat.setTextColor((Color.GREEN))
                    }

                    if(stat.status=="Offline"){
                        StatusChat.setTextColor((Color.RED))
                    }
                }
            }

        }
        else{
            Name.setText(user1);
            db.collection("users").document(user1).get().addOnSuccessListener {
                val stat = it.toObject(User::class.java)
                if (stat != null) {
                    StatusChat.setText(stat.status)
                    if(stat.status=="Online"){
                        StatusChat.setTextColor((Color.GREEN))
                    }

                    if(stat.status=="Offline"){
                        StatusChat.setTextColor((Color.RED))
                    }
                }
            }
        }

        messagesRecylerView.layoutManager = LinearLayoutManager(this)
        messagesRecylerView.adapter = MessageAdapter(user)

        sendMessageButton.setOnClickListener { sendMessage() }
        Chats.setOnClickListener{gotoChats()}
        LocationButton.setOnClickListener{ getLastLocation()}
        imageButtonChat.setOnClickListener{goToUploadImage()}
        fileButtonChat.setOnClickListener{}

        val chatRef = db.collection("chats").document(chatId)

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                (messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
            }

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        (messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
                    }
                }
            }
    }

    private fun goToUploadImage() {
        val intent = Intent(this, UploadImageChatActivity::class.java)
        intent.putExtra("chatId",chatId)
        intent.putExtra("user", user)
        intent.putExtra("user1",user1)
        intent.putExtra("user2",user2)
        startActivity(intent)
    }



    private fun sendMessage(){
        val message = Message(
            message = messageTextField.text.toString(),
            from = user,
            image = ""
        )

        if(message.message.isNotEmpty()) {

            db.collection("chats").document(chatId).collection("messages").document().set(message)

            messageTextField.setText("")
        }
        else{
            Toast.makeText(applicationContext,"Ingrese un mensaje para mandar",Toast.LENGTH_SHORT).show();
        }


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            profilePic.setImageURI(ImageUri)
            isSelected = true;
        }
    }

    private  fun gotoChats(){
        val intent = Intent(this, ListOfChatsActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }

    private fun CheckPermission():Boolean{
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true;

        }
        return false;
    }

    private fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("App","Tienes permiso")

            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        if(CheckPermission()){

            if(isLocationEnabled()){

                fusedLocationProviderClient.lastLocation.addOnCompleteListener{ task->
                    var location: Location? = task.result;
                    if(location == null){
                        getNewLocation()
                    }
                    else{
                        var localizacion : String;
                        localizacion = "Ubicación actual: \nLatitud: " + location.latitude +
                                " Longitud: " + location.longitude +
                                "\nCiudad: " + getCityName(location.latitude,location.longitude) +
                                "Pais:" + getCountryName(location.latitude,location.longitude);

                        messageTextField.setText(localizacion);
                    }
                }
            }else{
                Toast.makeText(this,"Habilita el servicio de localizacion para usar esta funcion",
                    Toast.LENGTH_SHORT).show();
            }
        }else{
            RequestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation(){
        locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.priority =  com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
        locationRequest.interval = 0;
        locationRequest.fastestInterval = 0;
        locationRequest.numUpdates = 2

        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation = p0.lastLocation;

            var localizacion = "Ubicación actual: \nLatitud: " + lastLocation.latitude +
                    " Longitud: " + lastLocation.longitude +
                    "\nCiudad: " + getCityName(lastLocation.latitude,lastLocation.longitude) +
                    " Pais:" + getCountryName(lastLocation.latitude,lastLocation.longitude);
            messageTextField.setText(localizacion);
        }
    }

    private fun getCityName(lat:Double,long:Double):String{
        var CityName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,1)

        CityName = Adress.get(0).locality
        return CityName;
    }

    private fun getCountryName(lat:Double,long:Double):String{
        var CountryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,1)

        CountryName = Adress.get(0).countryName
        return CountryName;
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
        val intent = Intent(this, ListOfChatsActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }
}