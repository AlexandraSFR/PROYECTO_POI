package com.example.pantalla_registro.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pantalla_registro.R
import com.example.pantalla_registro.Singleton.Sesion
import com.example.pantalla_registro.activities.LoginActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.profile.*


class ProfileFragment: Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragments: MenuItem;
    private lateinit var buttonLogOut: Button;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.profile,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textNombre.text = Sesion.name.toString();
        textCorreo.text = Sesion.email;
        textCarrera.text = Sesion.carrera.toString();
        textUsuario.text = Sesion.user.toString();
        initViews(view);
    }
    private fun initViews(view: View){
        buttonLogOut = view.findViewById(R.id.logOut);
        buttonLogOut.setOnClickListener{ logOut() }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return true
    }

    private fun logOut(){
        Sesion.Logout();
        val intent = Intent(activity, LoginActivity::class.java)
        activity?.startActivity(intent)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        activity?.finish();
    }
}


