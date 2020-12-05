package com.example.proyecto_mobiles

import android.content.DialogInterface
import android.content.Intent
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
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checarValoresUsuario()

        val btnRegistrarse : TextView = findViewById(R.id.txt_Registrate)
        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, usuario_registro::class.java)
            startActivity(intent)
        }

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
                usuarioGetByCorreoAndPass(Correo, Pass)
            }
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