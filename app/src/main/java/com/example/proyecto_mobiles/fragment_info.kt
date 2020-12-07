package com.example.proyecto_mobiles

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_mobiles.adapter.ComentariosAdapter
import com.example.proyecto_mobiles.db.FavoritosEntity
import com.example.proyecto_mobiles.db.RoomAppDB
import com.example.proyecto_mobiles.model.ComentariosLista
import com.example.proyecto_mobiles.model.ItemList
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.fragment_info.*
import org.json.JSONObject
import android.os.Handler;
import kotlin.random.Random


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_info.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_info : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewOfLayout: View
    private lateinit var otraView: View
    private lateinit var carouselView: CarouselView
    var itemList: List<Int> = ArrayList()
    var index:Int = 0

    var nombreLocal: String = ""
    var descripcionLocal: String = ""
    var calificacionBBB: String = ""
    var ResID:Int = 0
    var imagenLocal1: String = ""
    var imagenLocal2: String = ""
    var imagenLocal3: String = ""
    var espera = false
    var averageFav:Double = 0.0
    var x:Double = 0.0
    var y:Double = 0.0

    var CalificacionesDB = arrayListOf<Double>(
    )

    var textoDB = arrayListOf<String>(  )                                                              ///ARREGLO CON LAS IMAGENE DE LA BASE DE DATOS

    var calificacionDB = arrayListOf<Double>(  )

    var tamano:Int = textoDB.size

    private val exampleList = generateDummyList(tamano)
    private val adapter = ComentariosAdapter(exampleList)

    var sampleImages = arrayListOf<String>( )

    var nombreRestaurante:Int = usuarioSesion.ses.getRestaurante()

      ////ARREGLO DONDE SE GUARDAN LAS 3 IMAGENES A MOSTRAS EN LA VIEW

    lateinit var googleMap: GoogleMap

    lateinit var mapView: MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        fun newInstance() = fragment_info()
    }

    private lateinit var viewModel: FragmentComentariosViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater!!.inflate(R.layout.fragment_info, container, false)
        otraView = inflater!!.inflate(R.layout.activity_comentarios, container, false)


        val nombreL: TextView = viewOfLayout.findViewById(R.id.textViewLocal) as TextView
        val descripcionL: TextView = viewOfLayout.findViewById(R.id.textDescripcion) as TextView
        val comentarioLL: EditText = viewOfLayout.findViewById(R.id.editTextTextMultiLineComentario) as EditText

        val buttonComentario: Button = viewOfLayout.findViewById(R.id.buttonComentar) as Button
        val buttonPalFav: Button = viewOfLayout.findViewById(R.id.buttonFav) as Button

        val rateL: RatingBar = viewOfLayout.findViewById(R.id.ratingBarLocal) as RatingBar
        val rate2L: RatingBar = viewOfLayout.findViewById(R.id.ratingBar2) as RatingBar



//////////////////////////////////////////////////////////
        val queue2 = Volley.newRequestQueue(getActivity())
        val parametros2 = JSONObject()
        parametros2.put("restaurante_id", usuarioSesion.ses.getRestaurante())
        //load.startLoadingDialog()
        //Handler().postDelayed({load.dismissDialog()}, 6000)
        val requ2 = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/ComentarioGetByRestauranteId",parametros2,{
                response: JSONObject?->
            val usArray = response?.getJSONArray("comentarios")
            val success = response?.getInt("success")

            for (i in 0..(usArray!!.length() - 1)) {
                val item = usArray!!.getJSONObject(i)

                calificacionBBB = item.getString("calificacion")

                calificacionDB.add(calificacionBBB.toDouble())

            }
        }, { error ->
            error.printStackTrace()
            Log.e("Servicio web", "Web", error)
            if(error.toString()=="com.android.volley.ServerError"){
                val toast = Toast.makeText(getActivity(), "Error del servidor", Toast.LENGTH_LONG)
                toast.show()
            }
        })
        requ2.setRetryPolicy(DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue2.add(requ2)

        ////////////////////////////////////////////////

        val queue = Volley.newRequestQueue(getActivity())
        val parametros = JSONObject()
        //load.startLoadingDialog()
        //Handler().postDelayed({load.dismissDialog()}, 6000)
        val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/RestaurantesGetAllPublicados",parametros,{
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
                    x = item.getString("latitud").toDouble()
                    y = item.getString("longitud").toDouble()
                    //calificacionBBB = item.getString("calificacion")
                    ResID = item.getString("id").toInt()
                    nombreL.setText(nombreLocal)
                    descripcionL.setText(descripcionLocal)
                    val simpleArray = calificacionDB
                    averageFav = simpleArray.average()
                    rateL.setRating(averageFav.toFloat())

                }
                espera = true
            }

            if(success==0){
                val toast = Toast.makeText(getActivity(), "No hay restaurantes", Toast.LENGTH_LONG)
                toast.show()
            }
        }, { error ->
            error.printStackTrace()
            Log.e("Servicio web", "Web", error)
            if(error.toString()=="com.android.volley.ServerError"){
                val toast = Toast.makeText(getActivity(), "Error del servidor", Toast.LENGTH_LONG)
                toast.show()
            }
        })
        requ.setRetryPolicy(DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue.add(requ)

        ////////////////////////////////////////////////////
        val localCarousel = viewOfLayout.findViewById(R.id.carousel) as CarouselView
        ///////////////////////////////

        val imggg1:String = usuarioSesion.ses.getRestauranteimg1()
        val imggg2:String = usuarioSesion.ses.getRestauranteimg2()
        val imggg3:String = usuarioSesion.ses.getRestauranteimg3()

            sampleImages.add(imggg1)
            sampleImages.add(imggg2)
            sampleImages.add(imggg3)

            localCarousel.setPageCount(sampleImages.size);
            localCarousel.setImageListener(imageListener);


        /*------------VARIABLES PARA LLENAR LA VIEW------------*/


        /*------------VARIABLES QUE SE OBTIENEN------------*/
        var valorLocal: Float
        var valorComentario: Double
        var comentario: String

        buttonPalFav.setOnClickListener {

            val userid2:Int = usuarioSesion.ses.getID()

            val queue = Volley.newRequestQueue(getActivity())
            val body = JSONObject()
            body.put("restaurante_id", ResID)
            body.put("usuario_id", userid2)
            //load.startLoadingDialog()
            //Handler().postDelayed({load.dismissDialog()}, 6000)
            val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/FavoritoRegistrar",body,{
                    response: JSONObject?->
                val success = response?.getInt("success")

                if(success==1 ){
                    val favDao= RoomAppDB.getAppDatabase(requireActivity())?.favoritosDAO()
                    val favEntity = FavoritosEntity(0, nombreL.text.toString(), descripcionL.text.toString(),
                        x.toString(),y.toString(), ses.getID())
                    val id = favDao?.favoritosRegistrar(favEntity)
                    val toast = Toast.makeText(getActivity(), "Se guardo como favorito", Toast.LENGTH_LONG)
                    toast.show()
                }else if(success==0){
                    val favDao = RoomAppDB.getAppDatabase(requireActivity())?.favoritosDAO()
                    val fav = favDao?.favoritosGetById(ses.getID())

                    val sb = StringBuffer()
                    if(!fav.isNullOrEmpty()) {
                        fav?.forEach {
                            if(it.id==ResID){
                                favDao.favoritosDelete(it)
                            }
                        }
                    }


                    val toast = Toast.makeText(getActivity(), "Eliminado como favorito", Toast.LENGTH_LONG)
                    toast.show()
                }
            }, { error ->
                error.printStackTrace()
                Log.e("Servicio web", "Web", error)
                if(error.toString()=="com.android.volley.ServerError"){
                    val toast = Toast.makeText(getActivity(), "Error del servidor", Toast.LENGTH_LONG)
                    toast.show()
                }
            })
            requ.setRetryPolicy(DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
            queue.add(requ)
        }

        buttonComentario.setOnClickListener {

            var comentarioL: String = editTextTextMultiLineComentario.text.toString()
            valorComentario = rate2L.rating.toDouble()

            if (comentarioL.isEmpty() || valorComentario == 0.0 ) {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity())
                alertDialog3.setMessage("Agregar Comentario y Calificaion")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            } else {
                comentarioLL.setText("")
                comentario = comentarioL                                                             ///Variable con el Comentario AQUI SE MANDA EL COMENTARIO A GUARDAR
                //editTextTextMultiLineComentario.text.clear()

                ///////////////////////////////////////
                val userid:Int = usuarioSesion.ses.getID()

                val queue = Volley.newRequestQueue(getActivity())
                val body = JSONObject()
                body.put("texto", comentario)
                body.put("calificacion", valorComentario)
                body.put("restaurante_id", ResID)
                body.put("usuario_id", userid)
                //load.startLoadingDialog()
                //Handler().postDelayed({load.dismissDialog()}, 6000)
                val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/ComentarioRegistro",body,{
                        response: JSONObject?->

                }, { error ->
                    error.printStackTrace()
                    Log.e("Servicio web", "Web", error)
                    if(error.toString()=="com.android.volley.ServerError"){
                        val toast = Toast.makeText(getActivity(), "Error del servidor", Toast.LENGTH_LONG)
                        toast.show()
                    }
                })
                requ.setRetryPolicy(DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
                queue.add(requ)

                /////////////////////////////////////////////

                insertItem(otraView, comentario, valorComentario)
                index += 1
            }
        }

        return viewOfLayout
    }

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView) {
            // You can use Glide or Picasso here
            Picasso.get().load(sampleImages[position]).into(imageView)



            /*var mapFragment: SupportMapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)*/
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        traemelosComentarios()

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentComentariosViewModel::class.java)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)


    }

    private fun insertItem(view: View, v1:String, v2:Double) {

        //val index: Int = Random.nextInt(8)

        val newItem = ComentariosLista(v1, v2)

        exampleList.add(index, newItem)
        adapter.notifyItemInserted(index)
    }

    private fun generateDummyList(size: Int): ArrayList<ComentariosLista> {

        val list = ArrayList<ComentariosLista>()

        for (i in 0 until 2) {
            val item = ComentariosLista("nada", 5.0)
            list += item
        }

        return list
    }

    private fun traemelosComentarios(){

        exampleList.clear()
        textoDB.clear()
        calificacionDB.clear()

        val queue = Volley.newRequestQueue(getActivity())
        val parametros = JSONObject()
        parametros.put("restaurante_id", nombreRestaurante)
        //load.startLoadingDialog()
        //Handler().postDelayed({load.dismissDialog()}, 6000)
        val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/ComentarioGetByRestauranteId",parametros,{
                response: JSONObject?->
            val usArray = response?.getJSONArray("comentarios")
            val success = response?.getInt("success")

            for (i in 0..(usArray!!.length() - 1)) {
                val item = usArray!!.getJSONObject(i)
                val texto = item.getString("texto")
                val descripc = item.getString("calificacion")
                textoDB.add(texto)
                calificacionDB.add(descripc.toDouble())
            }

            for (i in 0..(usArray!!.length() - 1)) {
                val newItem =
                    ComentariosLista(textoDB[i], calificacionDB[i])
                exampleList.add(i, newItem)

                adapter.notifyDataSetChanged()

            }

        }, { error ->
            error.printStackTrace()
            Log.e("Servicio web", "Web", error)
            if(error.toString()=="com.android.volley.ServerError"){
                val toast = Toast.makeText(getActivity(), "Error del servidor", Toast.LENGTH_LONG)
                toast.show()
            }
        })
        requ.setRetryPolicy(DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue.add(requ)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.map)
        mapView?.onCreate(null)
        mapView?.onResume()
        mapView?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {

        val handler = Handler()
        handler.postDelayed({

            val map = googleMap

            val pos = LatLng(x,y)

            map?.addMarker(MarkerOptions().position(pos).title(nombreLocal))
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
        }, 1000)



}


}