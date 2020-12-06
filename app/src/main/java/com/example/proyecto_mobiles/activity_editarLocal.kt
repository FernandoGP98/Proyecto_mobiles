package com.example.proyecto_mobiles

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.core.app.ActivityCompat

import androidx.appcompat.app.AppCompatActivity

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

import kotlinx.android.synthetic.main.activity_info.*

import kotlinx.android.synthetic.main.activity_local_nuevo.*
import kotlinx.android.synthetic.main.activity_local_nuevo.LocalGuardar
import kotlinx.android.synthetic.main.activity_local_nuevo.LocalBorrador
import kotlinx.android.synthetic.main.activity_local_nuevo.editTextDescripcion
import kotlinx.android.synthetic.main.activity_local_nuevo.editTextNombreLocal
import kotlinx.android.synthetic.main.activity_perfil_configuracion.*
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
import javax.net.ssl.HttpsURLConnection
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.example.proyecto_mobiles.model.ItemList
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_editar_local.*
import kotlinx.android.synthetic.main.activity_local_nuevo.unaimagen1
import kotlinx.android.synthetic.main.activity_local_nuevo.unaimagen2
import kotlinx.android.synthetic.main.activity_local_nuevo.unaimagen3

class acivity_editarLocal : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var loadingView: AlertDialog
    private var imagePicker: ImagePicker = ImagePicker(this)
    private var imagePicker2: ImagePicker2 = ImagePicker2(this)
    private var imagePicker3: ImagePicker3 = ImagePicker3(this)
    private var selectedImage: Bitmap? = null
    private var selectedImage2: Bitmap? = null
    private var selectedImage3: Bitmap? = null
    private var imgurUrl: String = usuarioSesion.ses.getRestauranteimg1()
    private var imgurUrl2: String = usuarioSesion.ses.getRestauranteimg2()
    private var imgurUrl3: String = usuarioSesion.ses.getRestauranteimg3()
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    var nombreLocal: String = ""
    var descripcionLocal: String = ""
    var calificacionBBB: String = ""
    var ResID:Int = 0
    var imagenLocal1: String = ""
    var imagenLocal2: String = ""
    var imagenLocal3: String = ""
    var espera = false
    var continua = false
    var averageFav:Double = 0.0

    var nombreRestaurante:Int = usuarioSesion.ses.getRestaurante()

    var checa1 = false
    var checa2 = false
    var checa3 = false

    private var idNuevo: Int? = null

    private val CLIENT_ID = "9438166e60b3732"

    /*lateinit var mapFragment: SupportMapFragment*/
    lateinit var googleMap: GoogleMap
    lateinit var client : FusedLocationProviderClient
    lateinit var locationrequest : LocationRequest
    lateinit var locationcallback : LocationCallback
    lateinit var fragmentMislocales: fragment_mislocales
    lateinit var dialogBuilder: AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_local)

        val nombreL:TextView = findViewById<TextView>(R.id.editTextNombreLocal)
        val descripcionL:TextView = findViewById<TextView>(R.id.editTextDescripcion)


        var mapFragment: SupportMapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        client = LocationServices.getFusedLocationProviderClient(this)

        locationrequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationcallback = object : LocationCallback(){
            override fun onLocationResult(locationresult: LocationResult?){
                locationresult?: return
                for(location in locationresult.locations){
                    Toast.makeText(this@acivity_editarLocal , "locacion: " +  location.latitude.toString()+ ", "+ location.longitude.toString(), Toast.LENGTH_LONG ).show()
                }
            }
        }
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.loading_dialog)
        loadingView = builder.create()

        var IMAGEN1_temp: String =
            ""                                                                     ///Variables donde se guarda la direccion de las imagenes temporalmente
        var IMAGEN2_temp: String = ""
        var IMAGEN3_temp: String = ""

        var NombreLocal: String = ""
        var DescripcionLocal: String = ""
        var Brr_NombreLocal: String = ""
        var Brr_DescripcionLocal: String = ""

        val btn_imagenes1 = findViewById<Button>(R.id.buttonImagenes1)
        val btn_imagenes2 = findViewById<Button>(R.id.buttonImagenes2)
        val btn_imagenes3 = findViewById<Button>(R.id.buttonImagenes3)
        val imagenes1 = findViewById<ImageView>(R.id.unaimagen1)
        val imagenes2 = findViewById<ImageView>(R.id.unaimagen2)
        val imagenes3 = findViewById<ImageView>(R.id.unaimagen3)

        Picasso.get().load(usuarioSesion.ses.getRestauranteimg1()).into(imagenes1)
        Picasso.get().load(usuarioSesion.ses.getRestauranteimg2()).into(imagenes2)
        Picasso.get().load(usuarioSesion.ses.getRestauranteimg3()).into(imagenes3)

        //////////////////////////////////////////////////////////////////////

        val queue = Volley.newRequestQueue(this)
        val parametros = JSONObject()
        parametros.put("id", usuarioSesion.ses.getID())
        //load.startLoadingDialog()
        //Handler().postDelayed({load.dismissDialog()}, 6000)
        val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/RestaurantesGetByUsuario",parametros,{
                response: JSONObject?->
            val usArray = response?.getJSONArray("restaurantes")
            val success = response?.getInt("success")

            for (i in 0..(usArray!!.length() - 1)) {
                val item = usArray!!.getJSONObject(i)
                if(nombreRestaurante == item.getInt("id")) {

                    nombreLocal = item.getString("nombre")
                    descripcionLocal = item.getString("descripcion")
                    imagenLocal1 = item.getString("img1")
                    imagenLocal2 = item.getString("img2")
                    imagenLocal3 = item.getString("img3")
                    ResID = item.getString("id").toInt()
                    nombreL.setText(nombreLocal)
                    descripcionL.setText(descripcionLocal)

                }
            }

            if(success==0){
                val toast = Toast.makeText(this, "error", Toast.LENGTH_LONG)
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

        ////////////////////////////////////////////////////////////////////



        btn_imagenes1.setOnClickListener {
            // After API 23 (Marshmallow) and lower Android 10 you need to ask for permission first before save in External Storage(Micro SD)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                imagePicker.askPermissions()
            } else {
                imagePicker.pickImage()
            }
        }
        btn_imagenes2.setOnClickListener {
            // After API 23 (Marshmallow) and lower Android 10 you need to ask for permission first before save in External Storage(Micro SD)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                imagePicker2.askPermissions()
            } else {
                imagePicker2.pickImage()
            }
        }
        btn_imagenes3.setOnClickListener {
            // After API 23 (Marshmallow) and lower Android 10 you need to ask for permission first before save in External Storage(Micro SD)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                imagePicker3.askPermissions()
            } else {
                imagePicker3.pickImage()
            }
        }

        LocalGuardar.setOnClickListener {


            selectedImage?.let { it1 ->
                selectedImage2?.let { it2 ->
                    selectedImage3?.let { it3 ->
                        uploadImageToImgur(
                            it1,
                            it2, it3
                        )
                    }
                }
            }

            Thread.sleep(6_000)

            NombreLocal =
                editTextNombreLocal.text.toString()                                    ////AGREGA LA INFORMACION FINAL A LAS VARIALBLES
            DescripcionLocal = editTextDescripcion.text.toString()
            val userid: Int = usuarioSesion.ses.getID()

            //////////////AGREGA LOCAL

            val queue = Volley.newRequestQueue(this)
            val body = JSONObject()
            body.put("id", ResID)
            body.put("nombre", NombreLocal)
            body.put("descripcion", DescripcionLocal)
            body.put("usuario_id", userid)
            body.put("latitud", lat)
            body.put("longitud", lon)
            body.put("estado", 1)
            body.put("img1", imgurUrl)
            body.put("img2", imgurUrl2)
            body.put("img3", imgurUrl3)

            //load.startLoadingDialog()
            //Handler().postDelayed({load.dismissDialog()}, 6000)
            val requ = JsonObjectRequest(
                Request.Method.POST,
                "https://restaurantespia.herokuapp.com/RestauranteUpdateById",
                body,
                { response: JSONObject? ->
                    val success = response?.getInt("success")

                    val toast = Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_LONG)
                    toast.show()
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

            ////////////////////////////////

        }

        LocalBorrador.setOnClickListener {

            selectedImage?.let { it1 ->
                selectedImage2?.let { it2 ->
                    selectedImage3?.let { it3 ->
                        uploadImageToImgur(
                            it1,
                            it2, it3
                        )
                    }
                }
            }

            Thread.sleep(6_000)

            NombreLocal =
                editTextNombreLocal.text.toString()                                    ////AGREGA LA INFORMACION FINAL A LAS VARIALBLES
            DescripcionLocal = editTextDescripcion.text.toString()
            val userid: Int = usuarioSesion.ses.getID()

            //////////////AGREGA LOCAL

            val queue = Volley.newRequestQueue(this)
            val body = JSONObject()
            body.put("id", ResID)
            body.put("nombre", NombreLocal)
            body.put("descripcion", DescripcionLocal)
            body.put("usuario_id", userid)
            body.put("latitud", lat)
            body.put("longitud", lon)
            body.put("estado", 0)
            body.put("img1", imgurUrl)
            body.put("img2", imgurUrl2)
            body.put("img3", imgurUrl3)

            //load.startLoadingDialog()
            //Handler().postDelayed({load.dismissDialog()}, 6000)
            val requ = JsonObjectRequest(
                Request.Method.POST,
                "https://restaurantespia.herokuapp.com/RestauranteUpdateById",
                body,
                { response: JSONObject? ->
                    val success = response?.getInt("success")

                    val toast = Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_LONG)
                    toast.show()
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

        LocalBorrar.setOnClickListener {

            val alertDialog3 =
                AlertDialog.Builder(this, R.style.Alert)
            alertDialog3.setMessage("¿Esta seguro que desea eliminar su cuenta?")
                .setCancelable(false)
                .setPositiveButton("Si", DialogInterface.OnClickListener{dialog, id->
                    EliminarLocal()
                    finish()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
            val alert = alertDialog3.create()
            alert.setTitle("EXITO")
            alert.show()


        }

    }

    private fun EliminarLocal(){
        val queue2 = Volley.newRequestQueue(this)
        val parametros2 = JSONObject()
        parametros2.put("id", ResID)
        //load.startLoadingDialog()
        //Handler().postDelayed({load.dismissDialog()}, 6000)
        val requ2 = JsonObjectRequest(
            Request.Method.POST,
            "https://restaurantespia.herokuapp.com/RestaurantesDeleteById",
            parametros2,
            { response: JSONObject? ->
                val usArray = response?.getJSONArray("restaurantes")
                val success = response?.getInt("success")

                if (success == 1) {

                    fragmentMislocales = fragment_mislocales()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentMislocales)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit()

                    val toast = Toast.makeText(this, "c borro", Toast.LENGTH_LONG)
                    toast.show()
                } else if (success == 0) {
                    val toast = Toast.makeText(this, "error ", Toast.LENGTH_LONG)
                    toast.show()
                }
            },
            { error ->
                error.printStackTrace()
                Log.e("Servicio web", "Web", error)
                if (error.toString() == "com.android.volley.ServerError") {
                    val toast =
                        Toast.makeText(this, "Error del servidor", Toast.LENGTH_LONG)
                    toast.show()
                }
            })
        requ2.setRetryPolicy(
            DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        queue2.add(requ2)
    }

/*
    override fun onMapReady(googleMap: GoogleMap) {
        // Sets the map type to be "hybrid"
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Sydney")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }*/


    override fun onMapReady(googleMap: GoogleMap?) {

        val map = googleMap

        if(checklocationpermission()){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            client.lastLocation.addOnCompleteListener{
                /*client.requestLocationUpdates(locationrequest,locationcallback, Looper.getMainLooper())
                val latitude = it.result?.latitude
                val longitude = it.result?.longitude*/

                lat = it.result?.latitude!!
                lon = it.result?.longitude!!

                val pos = LatLng(lat!!,lon!!)

                map?.addMarker(MarkerOptions().position(pos).title("hola"))
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))

            }
        }
    }

    fun checklocationpermission() : Boolean{
        var state = false
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                state = true
            } else{
                ActivityCompat.requestPermissions( this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            }
        }else state = true

        return state
    }

    private fun uploadImageToImgur(image: Bitmap, image2: Bitmap, image3: Bitmap) {
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
                Log.d("TAG", "Link is : ${data.getString("link")}")
                imgurUrl2 = data.getString("link")

            }
        })
        //////////////////////////////////////

        getBase64Image(image3, complete = { base64Image ->
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
                Log.d("TAG", "Link is : ${data.getString("link")}")
                imgurUrl3 = data.getString("link")

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            1000 -> {
                super.onActivityResult(requestCode, resultCode, data)
                val results: Bitmap? =
                    imagePicker.onActivityResult(requestCode, resultCode, data)
                if (results != null) {
                    selectedImage = results
                    unaimagen1.setImageBitmap(selectedImage)
                }
            }
            1001 -> {
                super.onActivityResult(requestCode, resultCode, data)
                val results: Bitmap? =
                    imagePicker2.onActivityResult(requestCode, resultCode, data)
                if (results != null) {
                    selectedImage2 = results
                    unaimagen2.setImageBitmap(selectedImage2)
                }
            }
            1002 -> {
                super.onActivityResult(requestCode, resultCode, data)
                val results: Bitmap? =
                    imagePicker3.onActivityResult(requestCode, resultCode, data)
                if (results != null) {
                    selectedImage3 = results
                    unaimagen3.setImageBitmap(selectedImage3)
                }
            }
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