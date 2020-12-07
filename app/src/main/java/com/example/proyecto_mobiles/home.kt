package com.example.proyecto_mobiles

import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransitionImpl
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_mobiles.adapter.FavoritosRecycler
import com.example.proyecto_mobiles.adapter.RecyclerAdapter
import com.example.proyecto_mobiles.model.ItemList
import com.example.proyecto_mobiles.usuarioSesion.Companion.checkInternet
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_favoritos_off.*
import kotlinx.android.synthetic.main.nav_header.*
import org.json.JSONObject
import org.w3c.dom.Text

class home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
    lateinit var fragmentHome: fragment_home
    lateinit var fragmentPerfil: fragment_perfil
    lateinit var fragmentUsuarioRegistroDueno: fragment_usuario_registro_dueno
    lateinit var fragmentFavoritos: fragment_favoritos
    lateinit var fragmentFavoritosOff: fragment_favoritos_off
    lateinit var fragmentMislocales: fragment_mislocales
    lateinit var fragmentLocalesPendientes: fragment_locales_pendientes
    lateinit var fragmentNuevoLocal: fragment_nuevolocal
    lateinit var search: SearchView

    var adapterList:RecyclerAdapter? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title= "Principal"

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


        if(checkInternet.isOnline()){
            fragmentHome = fragment_home()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentHome)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            search = findViewById(R.id.sv_BuscarGeneral)
        }else{
            fragmentFavoritosOff = fragment_favoritos_off()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentFavoritosOff)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            search = findViewById(R.id.sv_BuscarGeneral)
        }

        this.sv_BuscarGeneral.setOnQueryTextListener(this)

        /*PARA PODER USAR ELEMENTOS DENTRO DE DEL SIDEBAR HAY QUE INICIALIZAR LA NAVBAR
        * Y ASI ACCEDER A ESOS ELEMENTOS
        * SI NO AL INTENTAR CAMBIAR ALGUNO DE SUS VALORES RESULTARA EN NULLO Y CRASHEA LA APP*/
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHeader = navView.getHeaderView(0)
        val lbl_correo = navHeader.findViewById(R.id.lbl_correo) as TextView
        val lbl_username = navHeader.findViewById(R.id.lbl_username)as TextView
        val lbl_avatar = navHeader.findViewById(R.id.iv_foto)as ImageView
        lbl_correo.text = ses.getMail()
        lbl_username.text = ses.getName()

        var foto = ses.getFoto()
        if(foto==""||foto=="foto vacia"||foto=="null") {
            lbl_avatar.setImageResource(R.drawable.avatar)
        }else{
            Picasso.get().load(ses.getFoto()).into(lbl_avatar)
        }


        navView.setNavigationItemSelectedListener(this)
        var menu:Menu = navView.menu
        var regDUenios:MenuItem = menu.findItem(R.id.nav_registrarDuenio)
        var verPendientes:MenuItem = menu.findItem(R.id.nav_porPublicar)
        var registrarLocal: MenuItem = menu.findItem(R.id.nav_nuevoLocal)
        var misLocales : MenuItem = menu.findItem((R.id.nav_mislocales))
        if(ses.getRol()!=1){
            regDUenios.setVisible(false)
            verPendientes.setVisible(false)
        }
        if(ses.getRol()==3){
            registrarLocal.setVisible(false)
            misLocales.setVisible(false)
        }
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
                    .commit()
                val actionBar = supportActionBar
                actionBar?.title= "Principal"
                search.cambiaVisibility(true)
            }
            R.id.nav_profile ->{
                val intent = Intent (this, perfil_configuracion::class.java)
                startActivity(intent)
                /*fragmentPerfil = fragment_perfil()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentPerfil)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                val actionBar = supportActionBar
                actionBar?.title= "Perfil"
                search.cambiaVisibility(false)*/
            }
            R.id.nav_mislocales->{
                fragmentMislocales = fragment_mislocales()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentMislocales)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                val actionBar = supportActionBar
                actionBar?.title= "Mis Locales"
                search.cambiaVisibility(true)
            }
            R.id.nav_favoritos->{
                fragmentFavoritos = fragment_favoritos()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentFavoritos)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                val actionBar = supportActionBar
                actionBar?.title= "Favoritos"
                search.cambiaVisibility(true)
            }
            R.id.nav_porPublicar ->{
                fragmentLocalesPendientes = fragment_locales_pendientes()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentLocalesPendientes)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                val actionBar = supportActionBar
                actionBar?.title= "Pendientes"
                search.cambiaVisibility(true)
            }
            R.id.nav_registrarDuenio->{
                fragmentUsuarioRegistroDueno = fragment_usuario_registro_dueno()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentUsuarioRegistroDueno)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                val actionBar = supportActionBar
                actionBar?.title= "Registrar dueño"
                search.cambiaVisibility(false)
            }
            R.id.nav_nuevoLocal->{
                val intent = Intent (this, acivity_localNuevo::class.java)
                startActivity(intent)
                /*fragmentNuevoLocal = fragment_nuevolocal()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentNuevoLocal)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
                val actionBar = supportActionBar
                actionBar?.title= "Nuevo Local"
                search.cambiaVisibility(false)*/
            }
            R.id.nav_cerrarSesion->{
                ses.wipe()
                drawer_layout.closeDrawer(GravityCompat.START)
                onBackPressed()
                finish()
            }
            R.id.nav_eliminarCuenta->{
                val alertDialog3 =
                    AlertDialog.Builder(this, R.style.Alert)
                alertDialog3.setMessage("¿Esta seguro que desea eliminar su cuenta?")
                    .setCancelable(false)
                    .setPositiveButton("Si", DialogInterface.OnClickListener{dialog, id->
                        EliminarCuenta()
                        ses.wipe()
                        finish()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("EXITO")
                alert.show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun EliminarCuenta(){
        val queue = Volley.newRequestQueue(this)
        val body = JSONObject()
        body.put("id", ses.getID())
        val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/UsuarioEliminar", body,{
            response: JSONObject?->
            val success=response?.getInt("success")
            if(success==1){
                val toast = Toast.makeText(this, "Se elimino su cuenta", Toast.LENGTH_SHORT)
                toast.show()
            }
        }, {error->
            error.printStackTrace()
            Log.e("Servicio web", "Web", error)
            if(error.toString()=="com.android.volley.ServerError"){
                val toast = Toast.makeText(this, "Error del servidor", Toast.LENGTH_LONG)
                toast.show()
            }
        })
        requ.setRetryPolicy(DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue.add(requ)
    }

    //DELEGATES DE SEARCH VIEW
    override fun onQueryTextSubmit(p0: String?): Boolean {
        //Cuando ya termino de buscar y le da enter
        if (p0 != null){
            Toast.makeText(this,"Submit ${p0}" ,   Toast.LENGTH_SHORT).show()
        }

        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        //Cuando va cambiando el texto
        if (p0 != null){
            //Toast.makeText(this@MainActivity, "TextChange ${p0}",   Toast.LENGTH_SHORT).show()
            if (adapterList != null)  this.adapterList?.filter?.filter(p0)

        }

        return false
    }
}