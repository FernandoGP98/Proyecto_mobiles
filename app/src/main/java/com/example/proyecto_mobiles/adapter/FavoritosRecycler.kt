package com.example.proyecto_mobiles.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mobiles.R
import com.example.proyecto_mobiles.db.FavoritosEntity
import com.example.proyecto_mobiles.fragment_info
import com.example.proyecto_mobiles.usuarioSesion
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses
import kotlinx.android.synthetic.main.itemlist_favoffline.view.*
import kotlinx.android.synthetic.main.itemlist_favoritos.view.*
import kotlinx.android.synthetic.main.itemlist_favoritos.view.txt_descripcion
import kotlinx.android.synthetic.main.itemlist_favoritos.view.txt_resturante

class FavoritosRecycler(val listener: RowClickListener): RecyclerView.Adapter<FavoritosRecycler.MyViewHolder>() {

    var items = ArrayList<FavoritosEntity>()

    fun setListData(data: ArrayList<FavoritosEntity>){
        this.items = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritosRecycler.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.itemlist_favoritos, parent, false)
        return MyViewHolder(inflater, listener)
    }

    override fun onBindViewHolder(holder: FavoritosRecycler.MyViewHolder, position: Int) {
       holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(view: View, val listener: RowClickListener): RecyclerView.ViewHolder(view){
        val txtNombre = view.txt_resturante
        val txtDesc = view.txt_descripcion
        val btn_delete = view.btn_eliminar
        fun bind(data: FavoritosEntity){
            txtNombre.text = data.nombre
            txtDesc.text=data.descripcion

            btn_delete.setOnClickListener{
                listener.onDeleteClickListener(data)
            }

            itemView.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    val itemahora = data.id
                    ses.saveRestaurante(itemahora)
                    val activity=v!!.context as AppCompatActivity
                    val fragmentInfo= fragment_info()
                    activity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentInfo)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit()
                }
            })

        }
    }

    interface RowClickListener{
        fun onDeleteClickListener(fav: FavoritosEntity)
    }

}