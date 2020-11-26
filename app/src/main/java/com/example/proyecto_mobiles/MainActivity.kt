package com.example.proyecto_mobiles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_perfil.*

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

            if (Correo == "yo" && Pass == "tu") {
                //AGREGAR A LA SESION
                ses.saveMail(Correo)

                irHome()

            } else if(Correo != ""  && Pass != ""){
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

}