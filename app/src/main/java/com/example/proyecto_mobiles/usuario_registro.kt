package com.example.proyecto_mobiles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_usuario_registro.*

class usuario_registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_registro)
        val btnInicarSesion : TextView = findViewById(R.id.txt_iniciaSesion)
        btnInicarSesion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val btn_Registro = findViewById(R.id.btn_registrar) as Button

        var Tcontrasenav1 : TextView = findViewById(R.id.editTextTextPassword4)
        var Tcontrasenav2 : TextView = findViewById(R.id.editTextTextPassword5)
        var contrasenaFinal: String = "noName"                                                        // Variable que guarda la contrase;a


        btn_Registro.setOnClickListener {
            var nombre: String = editTextTextPersonName3.text.toString()                             // variable que guarda el nombre
            var contrasenaV1: String = editTextTextPassword4.text.toString()
            var contrasenaV2: String = editTextTextPassword5.text.toString()

            if (contrasenaV1 != contrasenaV2) {

                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("La contraseña debe coincidir")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = dialogBuilder.create()
                alert.setTitle("ERROR")
                alert.show()

                    Tcontrasenav1.setText("")
                    Tcontrasenav2.setText("")
                } else {
                contrasenaFinal = contrasenaV1
                println("the value is $contrasenaFinal")
            }

            if (nombre.isNullOrEmpty() || contrasenaV1.isNullOrEmpty() || contrasenaV2.isNullOrEmpty()) {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Faltan Datos")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = dialogBuilder.create()
                alert.setTitle("ERROR")
                alert.show()
            }

            if (nombre.isNotEmpty() && contrasenaFinal.isNotEmpty()) {

                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Registro Completo")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    })
                val alert = dialogBuilder.create()
                alert.setTitle("ERROR")
                alert.show()
            }

            println("the value is $nombre")
            println("the value is $contrasenaV1")
            println("the value is $contrasenaV2")
            }
        }
}