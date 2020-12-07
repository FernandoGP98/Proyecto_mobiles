package com.example.proyecto_mobiles

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_local_nuevo.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_perfil_configuracion.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class perfil_configuracion : AppCompatActivity() {

    private var imagePicker: ImagePicker = ImagePicker(this)
    private lateinit var selectedImage: Bitmap
    private var imgurUrl: String = ""
    private lateinit var loadingView: AlertDialog

    private var selectedFile: Uri? = null

    private val CLIENT_ID = "9438166e60b3732"

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

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.loading_dialog)
        loadingView = builder.create()

        var nuevoNombre: String
        var nuevaContra: String
        var contrasenaFinalNueva: String = "vacio"

        nombreL.setText(ses.getName())

        var foto = ses.getFoto()
        if(foto==""||foto=="foto vacia"||foto=="null") {
            avatarL.setImageResource(R.drawable.avatar)
        }else{
            Picasso.get().load(ses.getFoto()).into(avatarL)
        }

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

                nuevoNombre = nombreN

                val queue = Volley.newRequestQueue(this)
                val body = JSONObject()
                body.put("nombre", nuevoNombre)
                body.put("id", ses.getID())

                val requ = JsonObjectRequest(
                    Request.Method.POST,
                    "https://restaurantespia.herokuapp.com/UsuarioUpdateNombre",
                    body,
                    { response: JSONObject? ->
                        val success = response?.getInt("success")
                        if(success==1) {
                            val usuario = response.getJSONObject("usuario")
                            ses.saveName(usuario.getString("nombre"))

                            val toast = Toast.makeText(this, "Nombre Actualizado", Toast.LENGTH_LONG)
                            toast.show()
                        }else{
                            val toast = Toast.makeText(this, "Error de actualizacion", Toast.LENGTH_LONG)
                            toast.show()
                        }

                    },
                    { error ->
                        error.printStackTrace()
                        Log.e("Servicio web", "Web", error)
                        if (error.toString() == "com.android.volley.ServerError") {
                            val toast = Toast.makeText(this, "Error del servidor", Toast.LENGTH_LONG)
                            toast.show()
                        }
                    })
                requ.setRetryPolicy(
                    DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
                )
                queue.add(requ)
            }
        }
        btn_contras.setOnClickListener {
            //Picasso.get().load(imgurUrl).into(iv_foto)
            //uploadImageToImgur(selectedImage)
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
                    contrasenaFinalNueva = contra1N

                    val queue = Volley.newRequestQueue(this)
                    val body = JSONObject()
                    body.put("password", contrasenaFinalNueva)
                    body.put("id", ses.getID())

                    val requ = JsonObjectRequest(
                        Request.Method.POST,
                        "https://restaurantespia.herokuapp.com/UsuarioUpdatePass",
                        body,
                        { response: JSONObject? ->
                            val success = response?.getInt("success")
                            if(success==1) {
                                val usuario = response.getJSONObject("usuario")
                                ses.savePass(usuario.getString("password"))

                                val toast = Toast.makeText(this, "Contraseña Actualizado", Toast.LENGTH_LONG)
                                toast.show()
                            }else{
                                val toast = Toast.makeText(this, "Error de actualizacion", Toast.LENGTH_LONG)
                                toast.show()
                            }

                        },
                        { error ->
                            error.printStackTrace()
                            Log.e("Servicio web", "Web", error)
                            if (error.toString() == "com.android.volley.ServerError") {
                                val toast = Toast.makeText(this, "Error del servidor", Toast.LENGTH_LONG)
                                toast.show()
                            }
                        })
                    requ.setRetryPolicy(
                        DefaultRetryPolicy(
                            5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
                    )
                    queue.add(requ)
                }
            }
        }

        btn_imagen.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                imagePicker.askPermissions()
            } else {
                imagePicker.pickImage()
            }
        }
        btn_imagenAct.setOnClickListener {

            //Picasso.get().load(imgurUrl).into(iv_foto)
            uploadImageToImgur(selectedImage)

            Thread.sleep(6_000)

            val queue = Volley.newRequestQueue(this)
            val body = JSONObject()
            body.put("id", ses.getID())
            body.put("foto", imgurUrl)


            //load.startLoadingDialog()
            //Handler().postDelayed({load.dismissDialog()}, 6000)
            val requ = JsonObjectRequest(
                Request.Method.POST,
                "https://restaurantespia.herokuapp.com/UsuarioUpdateFoto",
                body,
                { response: JSONObject? ->
                    val success = response?.getInt("success")
                    if(success==1) {
                        val usuario = response.getJSONObject("usuario")
                        ses.saveFoto(usuario.getString("foto"))

                        val toast = Toast.makeText(this, "Avatar Actualizado", Toast.LENGTH_LONG)
                        toast.show()
                    }else{
                        val toast = Toast.makeText(this, "Error de actualizacion", Toast.LENGTH_LONG)
                        toast.show()
                    }

                },
                { error ->
                    error.printStackTrace()
                    Log.e("Servicio web", "Web", error)
                    if (error.toString() == "com.android.volley.ServerError") {
                        val toast = Toast.makeText(this, "Error del servidor", Toast.LENGTH_LONG)
                        toast.show()
                    }
                })
            requ.setRetryPolicy(
                DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )
            queue.add(requ)


        }

    }

    private fun uploadImageToImgur(image: Bitmap) {
        loadingView.show()
        getBase64Image(image, complete = { base64Image ->
            GlobalScope.launch(Dispatchers.Default) {
                val url = URL("https://api.imgur.com/3/image")

                val boundary = "Boundary-${System.currentTimeMillis()}"

                val httpsURLConnection =
                    withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
                httpsURLConnection.setRequestProperty("Authorization", "Client-ID $CLIENT_ID")
                httpsURLConnection.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=$boundary"
                )

                httpsURLConnection.requestMethod = "POST"
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true

                var body = ""
                body += "--$boundary\r\n"
                body += "Content-Disposition:form-data; name=\"image\""
                body += "\r\n\r\n$base64Image\r\n"
                body += "--$boundary--\r\n"


                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                withContext(Dispatchers.IO) {
                    outputStreamWriter.write(body)
                    outputStreamWriter.flush()
                }
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                val jsonObject = JSONTokener(response).nextValue() as JSONObject
                val data = jsonObject.getJSONObject("data")
                loadingView.dismiss()
                Log.d("TAG", "Link is : ${data.getString("link")}")
                imgurUrl = data.getString("link")

            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

                super.onActivityResult(requestCode, resultCode, data)
                val results: Bitmap? =
                    imagePicker.onActivityResult(requestCode, resultCode, data)
                if (results != null) {
                    selectedImage = results
                    imageViewEdit.setImageBitmap(selectedImage)
                    /*Picasso.get().load(imgurUrl).into(iv_foto)
                    uploadImageToImgur(selectedImage)*/
                }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun getBase64Image(image: Bitmap, complete: (String) -> Unit) {
        GlobalScope.launch {
            val outputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val b = outputStream.toByteArray()
            complete(Base64.encodeToString(b, Base64.DEFAULT))
        }
    }

    override fun onBackPressed() {



    }


}