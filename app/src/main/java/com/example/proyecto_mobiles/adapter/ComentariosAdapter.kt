package com.example.proyecto_mobiles.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mobiles.R
import com.example.proyecto_mobiles.model.ComentariosLista
import kotlinx.android.synthetic.main.itemslist_comentarios.view.*
import kotlinx.android.synthetic.main.itemslist_view.view.*

class ComentariosAdapter(private val comentariosLista: List<ComentariosLista>): RecyclerView.Adapter<ComentariosAdapter.RecycleHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.itemslist_comentarios,
            parent, false)
        return RecycleHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecycleHolder, position: Int) {
        val currentItem = comentariosLista[position]

        holder.txtComentario.text = currentItem.comentario
    }

    override fun getItemCount()= comentariosLista.size

    class RecycleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtComentario: TextView = itemView.txt_comentario

    }

}