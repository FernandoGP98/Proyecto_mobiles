package com.example.proyecto_mobiles.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mobiles.R
import com.example.proyecto_mobiles.fragment_info
import com.example.proyecto_mobiles.model.ItemList

import kotlinx.android.synthetic.main.itemslist_view.view.*

class RecyclerAdapter(private val itemsList: List<ItemList>) : RecyclerView.Adapter<RecyclerAdapter.RecycleHolder>(),
    Filterable {

    public var mItems: List<ItemList> = itemsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.itemslist_view,
        parent, false)
        return RecycleHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecycleHolder, position: Int) {
        val currentItem = itemsList[position]

        holder.imgItem.setImageResource(currentItem.imagen)
        holder.txtRestaurante.text=currentItem.restaurante
        holder.txtDescripcion.text=currentItem.descripcion
        holder.itemView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
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

    //override fun getItemCount() = itemsList.size

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