package com.example.proyecto_mobiles

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_mobiles.db.RoomAppDB
import com.example.proyecto_mobiles.db.UsuarioEntity
import com.example.proyecto_mobiles.usuarioSesion.Companion.checkInternet
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistrarse : TextView = findViewById(R.id.txt_Registrate)
        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, usuario_registro::class.java)
            startActivity(intent)
        }

        if(!checkInternet.isOnline()){
            val toast = Toast.makeText(this, "Modo offline", Toast.LENGTH_LONG)
            toast.show()
        }

        checarValoresUsuario()

        val btnIngresar : TextView = findViewById(R.id.btn_ingresar)
        btnIngresar.setOnClickListener {

            /*val intent = Intent(this, home::class.java)
            startActivity(intent)*/


            var Correo: String = editTextTextPersonName.text.toString()
            var Pass: String = editTextTextPassword.text.toString()

            if (Correo == "" || Pass == "") {
                val alertDialog3 =
                    AlertDialog.Builder(this,  R.style.Alert)
                alertDialog3.setMessage("Faltan Datos")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("Acceso denegado")
                alert.show()
            }else if(Correo != ""  && Pass != ""){
                if(checkInternet.isOnline()){
                    usuarioGetByCorreoAndPass(Correo, Pass)
                }else{
                    getUsuarioOff(Correo, Pass)
                }
            }
        }
    }

    fun getUsuarioOff(correo: String, pass: String){
        val usDao = RoomAppDB.getAppDatabase(this)?.usuarioDAO()
        val List = usDao?.usuarioGetAll(correo, pass)
        val sb = StringBuffer()
        if(!List.isNullOrEmpty()) {
            List?.forEach {
                ses.saveName(it.nombre)
                ses.saveMail(it.correo)
                ses.savePass(it.password)
                ses.saveRol(it.rol_id)
                ses.saveID(it.id)
            }
            val toast = Toast.makeText(this, "Bienvenido "+ses.getName(), Toast.LENGTH_LONG)
            toast.show()
            irHome()
        }else{
            val alertDialog3 =
                AlertDialog.Builder(this, R.style.Alert)
            alertDialog3.setMessage("Usuario no encontra, verifique sus datos")
                .setCancelable(false)
                .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
            val alert = alertDialog3.create()
            alert.setTitle("Acceso denegado")
            alert.show()
        }

    }

    fun checarValoresUsuario(){
        if(ses.getMail() != "correo vacio" ){
            irHome()
        }
    }

    fun irHome(){
        val intent = Intent(this, home::class.java)
        startActivity(intent)
    }

    private fun usuarioGetByCorreoAndPass(correo:String, pass:String){
        val queue = Volley.newRequestQueue(this)
        val parametros = JSONObject()
        parametros.put("correo", correo)
        parametros.put("password", pass)
        //load.startLoadingDialog()
        //Handler().postDelayed({load.dismissDialog()}, 6000)
        val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/UsuarioGetByCorreo",parametros,{
                response: JSONObject?->
            val usArray = response?.getJSONArray("usuario")
            val success = response?.getInt("success")

            if(success==1 ){
                val usuario = usArray!!.getJSONObject(0)
                ses.saveMail(usuario.getString("email"))
                ses.saveName(usuario.getString("nombre"))
                ses.savePass(usuario.getString("password"))
                ses.saveRol(usuario.getInt("rol_id"))
                ses.saveID(usuario.getInt("id"))
                ses.saveFoto(usuario.getString("foto"))


                /*BUSCAR EL USUARIO EN LA LISTA OFFLINE, SI NO EXISTE AGREGARLO*/
                val usDao = RoomAppDB.getAppDatabase(this)?.usuarioDAO()
                val List = usDao?.usuarioGetAll(correo, pass)
                val sb = StringBuffer()
                if(List.isNullOrEmpty()) {
                    val usEntity = UsuarioEntity(ses.getID(), ses.getName(), ses.getMail(), ses.getPass(), ses.getRol())
                    val id = usDao?.usuarioRegistrar(usEntity)
                }

                val toast = Toast.makeText(this, "Bienvenido "+usuario.getString("id"), Toast.LENGTH_LONG)
                toast.show()
                irHome()
            }else if(success==0){
                val alertDialog3 =
                    AlertDialog.Builder(this, R.style.Alert)
                alertDialog3.setMessage("Correo o contraseña incorrecto")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("Acceso denegado")
                alert.show()
            }
        }, { error ->
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

}