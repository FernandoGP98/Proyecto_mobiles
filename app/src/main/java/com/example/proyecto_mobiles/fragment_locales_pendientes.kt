package com.example.proyecto_mobiles

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.proyecto_mobiles.adapter.RecyclerAdapter3
import com.example.proyecto_mobiles.model.ItemList
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject

class fragment_locales_pendientes : Fragment() {

    var imagenesDB = arrayListOf<String>(                                                                ///ARREGLO CON LAS IMAGENE DE LA BASE DE DATOS

    )
    var imagenesDB2 = arrayListOf<String>(                                                                ///ARREGLO CON LAS IMAGENE DE LA BASE DE DATOS

    )
    var imagenesDB3 = arrayListOf<String>(                                                                ///ARREGLO CON LAS IMAGENE DE LA BASE DE DATOS

    )


    var NombreLocalDB = arrayListOf<String>(

    )

    var DescripcionLocalDB = arrayListOf<String>(

    )

    var Ridd = arrayListOf<Int>(

    )

    var arrayT = arrayListOf<String>()

    lateinit var temp:String

    var tamano:Int = imagenesDB.size

    var RestauanteID:Int = 0

    var compruebaLista = false;

    var imagenes = arrayListOf<Int>()
    var NLocal = arrayListOf<String>()
    var DLocal = arrayListOf<String>()
    private lateinit var viewOfLayout: View
    lateinit var svSearch: androidx.appcompat.widget.SearchView
    private val exampleList = generateDummyList(tamano)
    private val adapter = RecyclerAdapter3(exampleList)
    var itemList: List<Int> = ArrayList()

    companion object {
        fun newInstance() = fragment_locales_pendientes()
    }

    private lateinit var viewModel: FragmentLocalesPendientesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_locales_pendientes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        usuarioGetByCorreoAndPass()
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentLocalesPendientesViewModel::class.java)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
    }

    private fun generateDummyList(size: Int): ArrayList<ItemList> {

        val list = ArrayList<ItemList>()

        for (i in 0 until size) {
            val item = ItemList(imagenesDB[i], imagenesDB2[i], imagenesDB3[i], NombreLocalDB[i], DescripcionLocalDB[i], i)
            list += item
        }
        return list
    }

    private fun LlenaLista(value: Int) {

    }

    private fun usuarioGetByCorreoAndPass() {

        val queue = Volley.newRequestQueue(getActivity())
        val parametros = JSONObject()
        //load.startLoadingDialog()
        //Handler().postDelayed({load.dismissDialog()}, 6000)
        val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/RestaurantesGetAllPendientes",parametros,{
                response: JSONObject?->
            val usArray = response?.getJSONArray("restaurantes")
            val success = response?.getInt("success")

            exampleList.clear()
            NombreLocalDB.clear()
            DescripcionLocalDB.clear()

            for (i in 0..(usArray!!.length() - 1)) {
                val item = usArray!!.getJSONObject(i)
                val nombre = item.getString("nombre")
                val descripc = item.getString("descripcion")
                val calific = item.getString("calificacion")
                val Rid = item.getString("id")
                val img = item.getString("img1")
                val img2 = item.getString("img2")
                val img3 = item.getString("img3")

                NombreLocalDB.add(nombre)
                DescripcionLocalDB.add(descripc)
                imagenesDB.add(img)
                imagenesDB2.add(img2)
                imagenesDB3.add(img3)
                Ridd.add(Rid.toInt())
            }

            for (i in 0..(usArray!!.length() - 1)) {
                val newItem =
                    ItemList(imagenesDB[i], imagenesDB2[i], imagenesDB3[i], NombreLocalDB[i], DescripcionLocalDB[i], Ridd[i])
                exampleList.add(i, newItem)

                adapter.notifyDataSetChanged()

            }

            if(success==1 ){
                val toast = Toast.makeText(getActivity(), "cargo Lista", Toast.LENGTH_LONG)
                toast.show()
            }else if(success==0){
                val toast = Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG)
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

}