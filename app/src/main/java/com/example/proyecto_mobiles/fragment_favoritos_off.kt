package com.example.proyecto_mobiles

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_mobiles.adapter.FavoritosRecycler
import com.example.proyecto_mobiles.adapter.RecyclerAdapter
import com.example.proyecto_mobiles.db.FavoritosEntity
import com.example.proyecto_mobiles.db.RoomAppDB
import com.example.proyecto_mobiles.model.ItemList
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import kotlinx.android.synthetic.main.activity_favoritos.*
import kotlinx.android.synthetic.main.fragment_favoritos.*
import kotlinx.android.synthetic.main.fragment_favoritos_off.*
import kotlinx.android.synthetic.main.fragment_favoritos_off.recycler_view
import kotlinx.android.synthetic.main.fragment_home.*

class fragment_favoritos_off : Fragment(){

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
    private val adapter = RecyclerAdapter(exampleList)
    var itemList: List<Int> = ArrayList()

    //private val exampleList = generateDummyList()

    companion object {
        fun newInstance() = fragment_favoritos_off()
    }

    private lateinit var viewModel: FragmentFavoritosOffViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout= inflater.inflate(R.layout.fragment_favoritos_off, container, false)

        return viewOfLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        favGetOff()

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentFavoritosOffViewModel::class.java)

        /*recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)*/

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

    private fun favGetOff(){
        val favDao = RoomAppDB.getAppDatabase(requireActivity())?.favoritosDAO()
        val List = favDao?.favoritosGetAll(ses.getID())
        val sb = StringBuffer()
        if(!List.isNullOrEmpty()) {
            exampleList.clear()
            NombreLocalDB.clear()
            DescripcionLocalDB.clear()
            List?.forEach {
                val nombre = it.nombre
                var descripcion= it.descripcion+", Latitud: "+it.altitud+", Longitud: "+it.longitud
                val rid = it.id
                val img = "img1"
                val img2 = "img2"
                val img3 = "img3"
                NombreLocalDB.add(nombre)
                DescripcionLocalDB.add(descripcion)
                imagenesDB.add(img)
                imagenesDB2.add(img2)
                imagenesDB3.add(img3)
                Ridd.add(rid.toInt())
            }
            var i =0
            List?.forEach {
                val newItem =
                    ItemList(
                        imagenesDB[i],
                        imagenesDB2[i],
                        imagenesDB3[i],
                        NombreLocalDB[i],
                        DescripcionLocalDB[i],
                        Ridd[i]
                    )
                exampleList.add(i, newItem)

                adapter.notifyDataSetChanged()
                i++
            }
        }
    }

}