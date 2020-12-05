package com.example.proyecto_mobiles

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_local_nuevo.*
import kotlinx.android.synthetic.main.activity_local_nuevo.IMGLocal1
import kotlinx.android.synthetic.main.activity_local_nuevo.IMGLocal2
import kotlinx.android.synthetic.main.activity_local_nuevo.LocalGuardar
import kotlinx.android.synthetic.main.activity_local_nuevo.buttonImagenesB
import kotlinx.android.synthetic.main.activity_local_nuevo.editTextDescripcion
import kotlinx.android.synthetic.main.activity_local_nuevo.editTextNombreLocal
import kotlinx.android.synthetic.main.fragment_nuevolocal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import java.sql.Date
import java.text.SimpleDateFormat
import javax.net.ssl.HttpsURLConnection
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory

class acivity_localNuevo : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var loadingView: AlertDialog
    private var imagePicker: ImagePicker = ImagePicker(this)
    private var selectedImage: Bitmap? = null
    private var selectedImage2: Bitmap? = null
    private var imgurUrl: String = ""
    private var imgurUrl2: String = ""

    private val CLIENT_ID = "9438166e60b3732"

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_nuevo)


        mapFragment= supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.loading_dialog)
        loadingView = builder.create()

        var IMAGEN1_temp: String = ""                                                                     ///Variables donde se guarda la direccion de las imagenes temporalmente
        var IMAGEN2_temp: String = ""
        var IMAGEN3_temp: String = ""

        var NombreLocal: String = ""
        var DescripcionLocal: String = ""
        var Brr_NombreLocal: String = ""
        var Brr_DescripcionLocal: String = ""


        buttonImagenesB.setOnClickListener {
            // After API 23 (Marshmallow) and lower Android 10 you need to ask for permission first before save in External Storage(Micro SD)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                imagePicker.askPermissions()
            } else {
                imagePicker.pickImage()
            }
        }

        LocalGuardar.setOnClickListener {

            //selectedImage?.let { it1 -> selectedImage2?.let { it2 -> uploadImageToImgur(it1, it2) } }

            NombreLocal = editTextNombreLocal.text.toString()                                    ////AGREGA LA INFORMACION FINAL A LAS VARIALBLES
            DescripcionLocal = editTextDescripcion.text.toString()
            val userid:Int = usuarioSesion.ses.getID()
            val date = SimpleDateFormat("HH:mm").format(Date((1507457400000 / 1000)))

            //////////////AGREGA LOCAL

            val queue = Volley.newRequestQueue(this)
            val body = JSONObject()
            body.put("nombre", NombreLocal)
            body.put("descripcion", DescripcionLocal)
            body.put("locacion", "lugar")
            body.put("lunes", date)
            body.put("martes", date)
            body.put("miercoles", date)
            body.put("jueves", date)
            body.put("viernes", date)
            body.put("sabado", date)
            body.put("domingo", date)
            body.put("latitud", "0.1231")
            body.put("longitud", "1.123")
            body.put("usuario", userid)

            //load.startLoadingDialog()
            //Handler().postDelayed({load.dismissDialog()}, 6000)
            val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/RestaurantesRegistro",body,{
                    response: JSONObject?->
                val toast = Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_LONG)
                toast.show()
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

            ///////////////////////////

        }

    }



    override fun onMapReady(googleMap: GoogleMap) {
        // Sets the map type to be "hybrid"
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions()
                .position(sydney)
                .title("Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun uploadImageToImgur(image: Bitmap, image2: Bitmap) {
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

        getBase64Image(image2, complete = { base64Image ->
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
                imgurUrl2 = data.getString("link")

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val results: Bitmap? = imagePicker.onActivityResult(requestCode, resultCode, data)
        if (results != null) {
            if (selectedImage == null) {
                selectedImage = results
                IMGLocal1.setImageBitmap(selectedImage)
            }
            if(selectedImage != null)
                selectedImage2 = results
            IMGLocal2.setImageBitmap(selectedImage2)
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