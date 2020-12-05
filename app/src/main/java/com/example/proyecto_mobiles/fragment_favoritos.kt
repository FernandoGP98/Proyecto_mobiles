package com.example.proyecto_mobiles

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_mobiles.adapter.ComentariosAdapter
import com.example.proyecto_mobiles.adapter.RecyclerAdapter
import com.example.proyecto_mobiles.model.ComentariosLista
import com.example.proyecto_mobiles.model.ItemList
import kotlinx.android.synthetic.main.fragment_home.*

class fragment_favoritos : Fragment() {

    var imagenesDB = arrayListOf<String>(                                                                ///ARREGLO CON LAS IMAGENE DE LA BASE DE DATOS

    )
    var imagenesDB2 = arrayListOf<String>(                                                                ///ARREGLO CON LAS IMAGENE DE LA BASE DE DATOS

    )
    var imagenesDB3 = arrayListOf<String>(                                                                ///ARREGLO CON LAS IMAGENE DE LA BASE DE DATOS

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
        fun newInstance() = fragment_favoritos()
    }

    private lateinit var viewModel: FragmentFavoritosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentFavoritosViewModel::class.java)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
    }

    private fun generateDummyList(size: Int): ArrayList<ItemList> {

        LlenaLista(tamano)

        val list = ArrayList<ItemList>()

        for (i in 0 until size) {
            val item = ItemList(imagenesDB[i], imagenesDB2[i], imagenesDB3[i], NombreLocalDB[i], DescripcionLocalDB[i], i)
            list += item
        }
        return list
    }
    private fun LlenaLista(value: Int) {


    }

}