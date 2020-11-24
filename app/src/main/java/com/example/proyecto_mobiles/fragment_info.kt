package com.example.proyecto_mobiles

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_mobiles.adapter.ComentariosAdapter
import com.example.proyecto_mobiles.model.ComentariosLista
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.fragment_info.*
import kotlin.random.Random


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_info.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_info : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewOfLayout: View
    private lateinit var otraView: View
    private lateinit var carouselView: CarouselView
    var itemList: List<Int> = ArrayList()
    var index:Int = 2

    private val exampleList = generateDummyList(2)
    private val adapter = ComentariosAdapter(exampleList)

    var sampleImages = intArrayOf(R.drawable.img_local1,R.drawable.img_local2,R.drawable.img_local3)  ////ARREGLO DONDE SE GUARDAN LAS 3 IMAGENES A MOSTRAS EN LA VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        fun newInstance() = fragment_info()
    }

    private lateinit var viewModel: FragmentComentariosViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater!!.inflate(R.layout.fragment_info, container, false)
        otraView = inflater!!.inflate(R.layout.activity_comentarios, container, false)

        val localCarousel = viewOfLayout.findViewById(R.id.carousel) as CarouselView
        localCarousel.setPageCount(sampleImages.size);
        localCarousel.setImageListener(imageListener);

        val nombreL: TextView = viewOfLayout.findViewById(R.id.textViewLocal) as TextView
        val descripcionL: TextView = viewOfLayout.findViewById(R.id.textDescripcion) as TextView
        val comentarioLL: EditText = viewOfLayout.findViewById(R.id.editTextTextMultiLineComentario) as EditText

        val buttonValor: Button = viewOfLayout.findViewById(R.id.buttonValorar) as Button
        val buttonComentario: Button = viewOfLayout.findViewById(R.id.buttonComentar) as Button


        /*------------VARIABLES PARA LLENAR LA VIEW------------*/
        var nombreLocal: String = "Restaurante Marciano"
        var descripcionLocal: String = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."

        /*------------VARIABLES QUE SE OBTIENEN------------*/
        var valorLocal: Float
        var valorComentario: Double
        var comentario: String

        nombreL.setText(nombreLocal)
        descripcionL.setText(descripcionLocal)

       val rateL: RatingBar = viewOfLayout.findViewById(R.id.ratingBarLocal) as RatingBar
        val rate2L: RatingBar = viewOfLayout.findViewById(R.id.ratingBar2) as RatingBar

        buttonValor.setOnClickListener {
            valorLocal = rateL.rating                                                                      ///AQUI SE GUARDA LA VALORACION QUE SE DIO
            Toast.makeText(getActivity(), "Valoracion: $valorLocal", Toast.LENGTH_SHORT).show()
            println("the value is $valorLocal")
        }

        buttonComentario.setOnClickListener {

            var comentarioL: String = editTextTextMultiLineComentario.text.toString()
            valorComentario = rate2L.rating.toDouble()

            if (comentarioL.isEmpty() || valorComentario == 0.0 ) {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity())
                alertDialog3.setMessage("Agregar Comentario y Calificaion")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            } else {
                comentarioLL.setText("")
                comentario = comentarioL                                                             ///Variable con el Comentario AQUI SE MANDA EL COMENTARIO A GUARDAR
                //editTextTextMultiLineComentario.text.clear()
                insertItem(otraView, comentario, valorComentario)
                index += 1
            }
        }

        return viewOfLayout
    }

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView) {
            // You can use Glide or Picasso here
            imageView.setImageResource(sampleImages[position])
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentComentariosViewModel::class.java)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
    }

    private fun insertItem(view: View, v1:String, v2:Double) {

        //val index: Int = Random.nextInt(8)

        val newItem = ComentariosLista(v1, v2)

        exampleList.add(index, newItem)
        adapter.notifyItemInserted(index)
    }

    private fun generateDummyList(size: Int): ArrayList<ComentariosLista> {

        val list = ArrayList<ComentariosLista>()

        for (i in 0 until size) {
            val item = ComentariosLista("Comentario $i", 2.0)
            list += item
        }
        return list
    }


}