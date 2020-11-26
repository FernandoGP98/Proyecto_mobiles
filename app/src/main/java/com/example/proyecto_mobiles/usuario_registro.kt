package com.example.proyecto_mobiles

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import kotlinx.android.synthetic.main.activity_usuario_registro.*

class usuario_registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_registro)
        val btnInicarSesion : TextView = findViewById(R.id.txt_iniciaSesion)
        btnInicarSesion.setOnClickListener {
            finish()
        }

        val btn_Registro = findViewById(R.id.btn_registrar) as Button

        var Tcontrasenav1 : TextView = findViewById(R.id.txt_contrasenaRegistro)
        var Tcontrasenav2 : TextView = findViewById(R.id.txt_contasenaConfirmRegistro)
        var contrasenaFinal: String = "noName"                                                        // Variable que guarda la contrase;a


        btn_Registro.setOnClickListener {
            var correo: String = txt_correoRegistro.text.toString()                             // variable que guarda el nombre
            var contrasenaV1: String = txt_contrasenaRegistro.text.toString()
            var contrasenaV2: String = txt_contasenaConfirmRegistro.text.toString()

            val dialogBuilder = AlertDialog.Builder(this, R.style.Alert)


            if (correo.isNullOrEmpty() || contrasenaV1.isNullOrEmpty() || contrasenaV2.isNullOrEmpty()) {
                dialogBuilder.setMessage("Faltan Datos")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = dialogBuilder.create()
                alert.setTitle("ERROR")
                alert.show()
            } else {
                if('@' in correo) {
                    if (contrasenaV1 != contrasenaV2) {

                        val dialogBuilder = AlertDialog.Builder(this, R.style.Alert)
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

                        ses.saveMail(correo)

                        dialogBuilder.setMessage("Registro Completo")
                            .setCancelable(false)
                            .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                                //closeKeyboard()
                                finish()
                                irHome()
                            })
                        val alert = dialogBuilder.create()
                        alert.setTitle("Exito")
                        alert.show()
                    }
                }else{
                    dialogBuilder.setMessage("Ingrese un verdadero correo")
                        .setCancelable(false)
                        .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                            //closeKeyboard()
                            dialog.cancel()
                        })
                    val alert = dialogBuilder.create()
                    alert.setTitle("ERROR")
                    alert.show()
                }
            }

            println("the value is $correo")
            println("the value is $contrasenaV1")
            println("the value is $contrasenaV2")
            }
        }

    fun irHome(){
        val intent = Intent(this, home::class.java)
        startActivity(intent)
    }

    /*fun closeKeyboard() {
        val view = this.currentFocus
        if(view != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }*/
}