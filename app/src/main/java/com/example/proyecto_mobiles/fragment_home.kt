package com.example.proyecto_mobiles

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_mobiles.adapter.ComentariosAdapter
import com.example.proyecto_mobiles.adapter.RecyclerAdapter
import com.example.proyecto_mobiles.model.ComentariosLista
import com.example.proyecto_mobiles.model.ItemList
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recycler_view
import kotlinx.android.synthetic.main.fragment_info.*

class fragment_home : Fragment(){

    var imagenesDB = arrayListOf<Int>(                                                                ///ARREGLO CON LAS IMAGENE DE LA BASE DE DATOS
        R.drawable.img_local1,
        R.drawable.img_local2,
        R.drawable.img_local3
    )

    var NombreLocalDB = arrayListOf<String>(
        "Restaurante Marciano",
        "Restaurante de Sushi",
        "Restaurante de Tacos"
    )

    var DescripcionLocalDB = arrayListOf<String>(
        "Descripcion del Restaurante Marciano",
        "Descripcion del Restaurante de Sushi",
        "Descripcion del Restaurante de Tacos"
    )

    var tamano:Int = imagenesDB.size

    var imagenes = arrayListOf<Int>()
    var NLocal = arrayListOf<String>()
    var DLocal = arrayListOf<String>()
    private lateinit var viewOfLayout: View
    lateinit var svSearch: androidx.appcompat.widget.SearchView
    private val exampleList = generateDummyList(tamano)
    private val adapter = RecyclerAdapter(exampleList)
    var itemList: List<Int> = ArrayList()

    companion object {
        fun newInstance() = fragment_home()
    }

    private lateinit var viewModel: FragmentHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_home, container, false)

        return viewOfLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentHomeViewModel::class.java)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)

    }

    private fun generateDummyList(size: Int): ArrayList<ItemList> {

        LlenaLista(tamano)

        val list = ArrayList<ItemList>()

        for (i in 0 until size) {
            val item = ItemList(imagenes[i], NombreLocalDB[i], DescripcionLocalDB[i], i)
            list += item
        }
        return list
    }

    private fun LlenaLista(value: Int) {

        imagenes.addAll(imagenesDB)
        NLocal.addAll(NombreLocalDB)
        NLocal.addAll(DescripcionLocalDB)
    }

}