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
import kotlinx.android.synthetic.main.activity_comentarios.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recycler_view

class fragment_comentarios : Fragment() {

    companion object {
        fun newInstance() = fragment_comentarios()
    }

    private lateinit var viewModel: FragmentComentariosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comentarios, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentComentariosViewModel::class.java)

        val lista = generateDummyList(50)

        recycler_view.adapter= ComentariosAdapter(lista)
        recycler_view.layoutManager= LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
    }

    private fun generateDummyList(size: Int): List<ComentariosLista>{
        val list = ArrayList<ComentariosLista>()
        for (i in 0 until size){
            val item = ComentariosLista("$i Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.")
            list += item
        }
        return list
    }

}