package com.example.proyecto_mobiles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_perfil.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistrarse : TextView = findViewById(R.id.txt_Registrate)
        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, usuario_registro::class.java)
            startActivity(intent)
        }

        val btnIngresar : TextView = findViewById(R.id.btn_ingresar)
        btnIngresar.setOnClickListener {

            val intent = Intent(this, home::class.java)
            startActivity(intent)

            /*
            var Correo: String = editTextTextPersonName.text.toString()
            var Pass: String = editTextTextPassword.text.toString()

            if (Correo == "yo" && Pass == "tu") {
                val intent = Intent(this, home::class.java)
                startActivity(intent)
            } else {
                val alertDialog3 =
                    AlertDialog.Builder(this)
                alertDialog3.setMessage("Contraseña o Usuario Incorrecto")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            }
            if (Correo == "" || Pass == "") {
                val alertDialog3 =
                    AlertDialog.Builder(this)
                alertDialog3.setMessage("Faltan Datos")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            }*/
        }
    }

}