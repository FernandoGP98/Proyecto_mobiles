package com.example.proyecto_mobiles

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import kotlinx.android.synthetic.main.activity_usuario_registro.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class usuario_registro : AppCompatActivity() {

    var currentTimestamp:Long = 0
    lateinit var dialogBuilder: AlertDialog.Builder

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

            dialogBuilder = AlertDialog.Builder(this, R.style.Alert)


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

                        dialogBuilder = AlertDialog.Builder(this, R.style.Alert)
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
                        currentTimestamp = System.currentTimeMillis()
                        UsuarioRegistro(correo, contrasenaV1)
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

    private fun UsuarioRegistro(correo:String, contrasenaV1:String){
        val nombre = "usuario" + currentTimestamp.toString()
        val queue = Volley.newRequestQueue(this)
        val body = JSONObject()
        body.put("correo", correo)
        body.put("password", contrasenaV1)
        body.put("nombre", nombre)
        body.put("rol_id", 3)
        //load.startLoadingDialog()
        //Handler().postDelayed({load.dismissDialog()}, 6000)
        val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/UsuarioRegistrar",body,{
            response: JSONObject?->
            //val usArray = response?.getJSONArray("usuario")
            val success = response?.getInt("success")

            if(success==1 ){
                val usuario = response.getJSONObject("usuario")
                ses.saveMail(usuario.getString("email"))
                ses.saveName(usuario.getString("nombre"))
                ses.savePass(usuario.getString("password"))
                ses.saveRol(usuario.getInt("rol_id"))
                ses.saveID(usuario.getInt("id"))
                val usDao=RoomAppDB.getAppDatabase(this)?.usuarioDAO()
                val usEntity = UsuarioEntity(ses.getID(), ses.getName(), ses.getMail(), ses.getPass(), ses.getRol())
                val id = usDao?.usuarioRegistrar(usEntity)
                val toast = Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT)
                toast.show()
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

    /*fun closeKeyboard() {
        val view = this.currentFocus
        if(view != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }*/
}