package com.example.pantalla_registro.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.pantalla_registro.R
import com.google.android.material.navigation.NavigationView


class SettingsFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragments: MenuItem;

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings,container,false);
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return true
    }


}