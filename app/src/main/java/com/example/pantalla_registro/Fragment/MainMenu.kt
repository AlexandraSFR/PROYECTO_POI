package com.example.pantalla_registro.Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pantalla_registro.*
import com.google.android.material.navigation.NavigationView

class MainMenu : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var tolbar: Toolbar;
    private lateinit var drawer: DrawerLayout;
    private lateinit var toggle: ActionBarDrawerToggle;
    private lateinit var navView: NavigationView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tolbar = findViewById(R.id.toolbar);
        setSupportActionBar(tolbar);

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawer= findViewById(R.id.drawer_layout);

        toggle = ActionBarDrawerToggle(this,drawer,tolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initViews()
        lateinit var fragment: GroupFragment;
        fragment = GroupFragment()
        if(savedInstanceState ==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        }

    }


    private fun initViews(){

    }

    override fun onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed();
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        lateinit var fragment: GroupFragment;
        lateinit var fragment1: MessageFragment;
        lateinit var fragment2: ProfileFragment;
        lateinit var fragment3: SettingsFragment;
        lateinit var fragment4: AddGroupFragment;
        fragment = GroupFragment()
        fragment1 = MessageFragment()
        fragment2 = ProfileFragment()
        fragment3 = SettingsFragment()
        fragment4 = AddGroupFragment()
        when(item.itemId){
            R.id.nav_groups ->{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
            R.id.nav_message ->{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment1).commit();

            }
            R.id.nav_profile ->{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment2).commit();
            }

            R.id.nav_settings ->{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment3).commit();
            }
            R.id.nav_add ->{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment4).commit();
            }


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}




