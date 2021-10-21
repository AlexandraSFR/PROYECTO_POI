package com.example.pantalla_registro.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pantalla_registro.R
import com.google.android.material.navigation.NavigationView


class GroupFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragments: MenuItem;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_list_of_groups,container,false);
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return true
    }
}