package com.example.proyecto_mobiles.adapter

import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mobiles.R
import com.example.proyecto_mobiles.db.FavoritosEntity
import com.example.proyecto_mobiles.fragment_info
import com.example.proyecto_mobiles.model.ItemList
import com.example.proyecto_mobiles.usuarioSesion
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.itemslist_view.view.*
import java.lang.reflect.Array.get

class RecyclerAdapter(private val itemsList: List<ItemList>) : RecyclerView.Adapter<RecyclerAdapter.RecycleHolder>(),
    Filterable {

    public var mItems: List<ItemList> = itemsList

    public var itemahora:String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.itemslist_view,
        parent, false)
        return RecycleHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecycleHolder, position: Int) {
        val currentItem = itemsList[position]

        Picasso.get().load(currentItem.imagen).into(holder.imgItem)
        holder.txtRestaurante.text=currentItem.restaurante
        holder.txtDescripcion.text=currentItem.descripcion
        holder.itemView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                val itemahora = currentItem.id
                val imagenahora1 = currentItem.imagen
                val imagenahora2 = currentItem.imagen2
                val imagenahora3 = currentItem.imagen3
                usuarioSesion.ses.saveRestaurante(itemahora)
                usuarioSesion.ses.saveRestauranteimg1(imagenahora1)
                usuarioSesion.ses.saveRestauranteimg2(imagenahora2)
                usuarioSesion.ses.saveRestauranteimg3(imagenahora3)
                val activity=v!!.context as AppCompatActivity
                val fragmentInfo=fragment_info()
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentInfo)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

    class RecycleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.iv_restaurante
        val txtRestaurante: TextView = itemView.txt_resturante
        val txtDescripcion: TextView = itemView.txt_descripcion
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun getItem(p0: Int): ItemList? {
        return mItems.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return mItems.get(p0).id.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mItems = filterResults.values as List<ItemList>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    itemsList
                else
                    itemsList.filter {
                        it.restaurante.toLowerCase().contains(queryString) ||
                                it.descripcion.toLowerCase().contains(queryString)

                    }
                return filterResults
            }
        }
    }
}