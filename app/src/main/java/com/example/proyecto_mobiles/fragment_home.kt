package com.example.proyecto_mobiles

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_mobiles.adapter.RecyclerAdapter
import com.example.proyecto_mobiles.model.ItemList
import kotlinx.android.synthetic.main.fragment_home.*

class fragment_home : Fragment() {

    companion object {
        fun newInstance() = fragment_home()
    }

    private lateinit var viewModel: FragmentHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentHomeViewModel::class.java)

        val lista = generateDummyList(50)

        recycler_view.adapter= RecyclerAdapter(lista)
        recycler_view.layoutManager= LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
    }

    private fun generateDummyList(size: Int): List<ItemList>{
        val list = ArrayList<ItemList>()
        for (i in 0 until size){
            val item = ItemList(R.drawable.ic_android_black_24dp, "Restaurante $i", "Descripcion $i")
            list += item
        }
        return list
    }

}