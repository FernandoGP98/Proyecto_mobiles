package com.example.proyecto_mobiles

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_mobiles.adapter.FavoritosRecycler
import com.example.proyecto_mobiles.db.FavoritosEntity
import kotlinx.android.synthetic.main.fragment_favoritos_off.*

class fragment_favoritos_off : Fragment(), FavoritosRecycler.RowClickListener {

    private lateinit var viewOfLayout: View

    //private val exampleList = generateDummyList()

    companion object {
        fun newInstance() = fragment_favoritos_off()
    }

    private lateinit var viewModel: FragmentFavoritosOffViewModel
    lateinit var recycler: FavoritosRecycler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout= inflater.inflate(R.layout.fragment_favoritos_off, container, false)

        return viewOfLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentFavoritosOffViewModel::class.java)

        //recycler_view.adapter = adapter

    }

    override fun onDeleteClickListener(fav: FavoritosEntity) {
        viewModel.deleteFavorito(fav)
    }

}