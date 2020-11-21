package com.example.proyecto_mobiles.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mobiles.R
import com.example.proyecto_mobiles.fragment_comentarios
import com.example.proyecto_mobiles.model.ComentariosLista
import kotlinx.android.synthetic.main.itemslist_comentarios.view.*

class ComentariosAdapter(private val comentariosLista: MutableList<ComentariosLista>): RecyclerView.Adapter<ComentariosAdapter.RecycleHolder>(){

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

        val item = comentariosLista[holder.adapterPosition]

        holder.txtComentario.text = currentItem.comentario
        holder.rateComentario.rating = currentItem.calificion.toFloat()

        holder.deleteIMG.setOnClickListener {

            comentariosLista.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            //fragment_comentarios.prueba.test(item)
        }

    }

    override fun getItemCount()= comentariosLista.size

    class RecycleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtComentario: TextView = itemView.txt_comentario
        val rateComentario: RatingBar = itemView.ratingBar
        val deleteIMG: ImageButton = itemView.imageButton2
    }

}