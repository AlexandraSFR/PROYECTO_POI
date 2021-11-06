package com.example.pantalla_registro.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.R
import com.example.pantalla_registro.adapters.GroupAdapter
import com.example.pantalla_registro.models.GroupMessage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.messageTextField
import kotlinx.android.synthetic.main.activity_chat.messagesRecylerView
import kotlinx.android.synthetic.main.activity_chat.sendMessageButton
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.activity_list_of_chats.*
import java.util.*
import java.util.jar.Manifest


class GroupChatActivity : AppCompatActivity() {
    private var user = ""
    private var chatId = ""
    private var chatName = ""

    lateinit var  fusedLocationProviderClient: FusedLocationProviderClient;
    lateinit var locationRequest :  com.google.android.gms.location.LocationRequest;

    private var db = Firebase.firestore
    private  var PERMISSION_ID = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("chatName")?.let { chatName = it }
        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }


    }

    private fun initViews(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        messagesRecylerView.layoutManager = LinearLayoutManager(this)
        messagesRecylerView.adapter = GroupAdapter(chatId);
        Group.setOnClickListener { gotoGroups() }

        ImageButton.setOnClickListener{ goToUploadImage()}
        FileButton.setOnClickListener{}
        locationButton.setOnClickListener{getLastLocation()}
        sendMessageButton.setOnClickListener { sendMessage() }
        groupName.setText(chatName)


        val generalChat = db.collection("gchat").document(chatId);
        generalChat.collection("messages").orderBy("dob", Query.Direction.ASCENDING).get().addOnSuccessListener { ChatG ->
            val listMessages = ChatG.toObjects(GroupMessage::class.java)
            (messagesRecylerView.adapter as GroupAdapter).setData(listMessages)
        }

        generalChat.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = it.toObjects(GroupMessage::class.java)
                        (messagesRecylerView.adapter as GroupAdapter).setData(listMessages)
                    }
                }
            }
    }

    private fun goToUploadImage() {
        val intent = Intent(this, UploadImageGroupActivity::class.java)
        intent.putExtra("chatId",chatId)
        intent.putExtra("chatName", chatName)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun showLocation() {
        TODO("Not yet implemented")
    }

    private fun sendMessage(){
        val message = GroupMessage(
            from = user,
            message = messageTextField.text.toString(),
            image = ""

        )

        db.collection("gchat").document(chatId).collection("messages").document().set(message)

        messageTextField.setText("")


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
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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
                Toast.makeText(this,"Habilita el servicio de localizacion para usar esta funcion",Toast.LENGTH_SHORT).show();
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

    private val locationCallback = object :LocationCallback(){
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
        var geoCoder = Geocoder(this,Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,1)

        CityName = Adress.get(0).locality
        return CityName;
    }

    private fun getCountryName(lat:Double,long:Double):String{
        var CountryName = ""
        var geoCoder = Geocoder(this,Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,1)

        CountryName = Adress.get(0).countryName
        return CountryName;
    }

    private fun gotoGroups(){
        val intent = Intent(this, ListOfGroupsActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }
    override fun onPause() {
        super.onPause()
        db.collection("users").document(user).update("status","Offline")

    }

    override fun onResume(){
        super.onResume()
        db.collection("users").document(user).update("status","Online")


    }

}