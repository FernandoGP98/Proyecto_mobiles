package com.example.proyecto_mobiles

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_perfil_configuracion.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import org.w3c.dom.Text

class perfil_configuracion : AppCompatActivity() {

    private var selectedFile: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_configuracion)

        val nombreL: TextView = findViewById(R.id.txt_EditPerfilNombre)
        val contra1L: TextView = findViewById(R.id.txt_EditPerfilContra)
        val contra2L: TextView = findViewById(R.id.txt_EditPerfilConfirm)
        val avatarL: ImageView = findViewById(R.id.imageViewEdit)

        val btn_names = findViewById(R.id.btn_EditPerilNombre) as Button
        val btn_contras = findViewById(R.id.btn_EditPerfilContra) as Button
        val btn_imagen = findViewById(R.id.btn_IMAGENEdit) as Button
        val btn_imagenAct = findViewById(R.id.btn_IMAGENActEdit) as Button

        var nuevoNombre: String
        var nuevaContra: String
        var contrasenaFinalNueva: String = "vacio"

        nombreL.setText(usuarioSesion.ses.getName())
        avatarL.setImageResource(R.drawable.avatar)

        btn_names.setOnClickListener{
            var nombreN: String = nombreL.text.toString()
            if(nombreN.isEmpty()){
                val alertDialog3 =
                    AlertDialog.Builder(this, R.style.Alert)
                alertDialog3.setMessage("Agrege un Nombre valido")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            }else{
                val alertDialog3 =
                    AlertDialog.Builder(this, R.style.Alert)
                alertDialog3.setMessage("Nombre Actualizado")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("EXITO")
                alert.show()
                nuevoNombre = nombreN
            }
        }
        btn_contras.setOnClickListener {
            var contra1N: String = contra1L.text.toString()
            var contra2N: String = contra2L.text.toString()

            if(contra1N.isEmpty() || contra2N.isEmpty()){
                val alertDialog3 =
                    AlertDialog.Builder(this, R.style.Alert)
                alertDialog3.setMessage("Faltan Datos")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            }else {
                if (contra1N != contra2N) {
                    val alertDialog3 =
                        AlertDialog.Builder(this, R.style.Alert)
                    alertDialog3.setMessage("La contraseña debe coincidir")
                        .setCancelable(false)
                        .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        })
                    val alert = alertDialog3.create()
                    alert.setTitle("ERROR")
                    alert.show()
                } else {
                    val alertDialog3 =
                        AlertDialog.Builder(this, R.style.Alert)
                    alertDialog3.setMessage("Contraseña Actualizada")
                        .setCancelable(false)
                        .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        })
                    val alert = alertDialog3.create()
                    alert.setTitle("EXITO")
                    alert.show()
                    contrasenaFinalNueva = contra1N
                }
            }
        }

        btn_imagen.setOnClickListener {
            val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)

            println("the value is $selectedFile")
        }
        btn_imagenAct.setOnClickListener {
            avatarL.setImageURI(selectedFile)
            println("the value is $selectedFile")
        }
    }



}