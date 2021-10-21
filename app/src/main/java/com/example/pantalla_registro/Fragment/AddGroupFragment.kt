package com.example.pantalla_registro.Fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.pantalla_registro.R
import com.google.android.material.navigation.NavigationView


class AddGroupFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragments: MenuItem;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_group,container,false);
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return true
    }
}