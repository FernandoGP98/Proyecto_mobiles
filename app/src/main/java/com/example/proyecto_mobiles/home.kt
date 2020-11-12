package com.example.proyecto_mobiles

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransitionImpl
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mobiles.adapter.RecyclerAdapter
import com.example.proyecto_mobiles.model.ItemList
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_home.*

class home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var fragmentHome: fragment_home
    lateinit var fragmentPerfil: fragment_perfil
    lateinit var fragmentComentarios: fragment_comentarios
    lateinit var fragmentNuevoLocal: fragment_nuevolocal
    lateinit var search: androidx.appcompat.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title= ""

        var toggle: ActionBarDrawerToggle = object: ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_draw_open,
            R.string.navigation_draw_close
        ){

        }
        toggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        fragmentHome = fragment_home()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragmentHome)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
        search = findViewById(R.id.sv_BuscarGeneral)
    }

    fun View.cambiaVisibility(visible: Boolean) {
        if (visible) {
            visibility = View.VISIBLE
        } else {
            visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.nav_home ->{
                fragmentHome = fragment_home()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentHome)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                search.cambiaVisibility(true)
            }
            R.id.nav_profile ->{
                fragmentPerfil = fragment_perfil()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentPerfil)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                search.cambiaVisibility(false)
            }
            R.id.nav_comment ->{
                fragmentComentarios = fragment_comentarios()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentComentarios)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                search.cambiaVisibility(true)
            }
            R.id.nav_nuevoLocal->{
                fragmentNuevoLocal = fragment_nuevolocal()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentNuevoLocal)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                search.cambiaVisibility(false)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}