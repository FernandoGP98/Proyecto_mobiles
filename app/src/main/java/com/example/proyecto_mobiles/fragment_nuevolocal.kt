package com.example.proyecto_mobiles

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_nuevolocal.*
import kotlinx.android.synthetic.main.fragment_nuevolocal.editTextNombreLocal
import kotlinx.android.synthetic.main.fragment_perfil.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_nuevolocal.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_nuevolocal : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewOfLayout: View
    private var selectedFile: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater!!.inflate(R.layout.fragment_nuevolocal, container, false)

        val btn_imagenesB = viewOfLayout.findViewById(R.id.buttonImagenesB) as Button
        val btn_imagenesA = viewOfLayout.findViewById(R.id.buttonImagenesA) as Button
        val btn_LocalGuardar = viewOfLayout.findViewById(R.id.LocalGuardar) as Button
        val btn_LocalBorrador = viewOfLayout.findViewById(R.id.LocalBorrador) as Button

        val IMGLocal1: ImageView = viewOfLayout.findViewById(R.id.IMGLocal1) as ImageView
        val IMGLocal2: ImageView = viewOfLayout.findViewById(R.id.IMGLocal2) as ImageView
        val IMGLocal3: ImageView = viewOfLayout.findViewById(R.id.IMGLocal3) as ImageView

        var IMAGEN1_temp: String = ""                                                                     ///Variables donde se guarda la direccion de las imagenes temporalmente
        var IMAGEN2_temp: String = ""
        var IMAGEN3_temp: String = ""

        var NombreLocal: String = ""
        var DescripcionLocal: String = ""
        var Brr_NombreLocal: String = ""
        var Brr_DescripcionLocal: String = ""

        var IMAGEN1: String
        var IMAGEN2: String
        var IMAGEN3: String

        var Brr_IMAGEN1: String
        var Brr_IMAGEN2: String
        var Brr_IMAGEN3: String

        btn_imagenesB.setOnClickListener {

            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)

            /////////////////////////////////////////////////////////////////////////////////////////////////////


            ////////////////////////////////////////////////////////////////////////////////////////////////////

        }
        btn_imagenesA.setOnClickListener {

            if (IMGLocal1.getDrawable() == null) {
                IMGLocal1.setImageURI(selectedFile)
                IMAGEN1_temp = selectedFile.toString()                                                        //GUARDA IMAGEN 1
                selectedFile = null
            }
            if (IMGLocal2.getDrawable() == null && IMGLocal1.getDrawable() != null) {
                IMGLocal2.setImageURI(selectedFile)
                IMAGEN2_temp = selectedFile.toString()                                                       //GUARDA IMAGEN 2
                selectedFile = null
            }
            if (IMGLocal3.getDrawable() == null && IMGLocal1.getDrawable() != null && IMGLocal2.getDrawable() != null) {
                IMGLocal3.setImageURI(selectedFile)
                IMAGEN3_temp = selectedFile.toString()                                                       //GUARDA IMAGEN 3
            }
        }
        btn_LocalGuardar.setOnClickListener {

            NombreLocal = editTextNombreLocal.text.toString()                                    ////AGREGA LA INFORMACION FINAL A LAS VARIALBLES
            DescripcionLocal = editTextDescripcion.text.toString()
            IMAGEN1 = IMAGEN1_temp
            IMAGEN2 = IMAGEN2_temp
            IMAGEN3 = IMAGEN3_temp

            if (NombreLocal.isEmpty() || DescripcionLocal.isEmpty() || IMGLocal3.getDrawable() == null || IMGLocal1.getDrawable() == null || IMGLocal2.getDrawable() == null ) {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity(), R.style.Alert)
                alertDialog3.setMessage("Faltan Datos")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            } else {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity(), R.style.Alert)
                alertDialog3.setMessage("Local Agregado")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("EXITO")
                alert.show()
            }

            if (IMGLocal3.getDrawable() == null || IMGLocal1.getDrawable() == null || IMGLocal2.getDrawable() == null ) {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity(), R.style.Alert)
                alertDialog3.setMessage("Agregar 3 Imagenes")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            }
        }
        btn_LocalBorrador.setOnClickListener {

            Brr_NombreLocal = editTextNombreLocal.text.toString()                                        ////AGREGA LA INFORMACION PARA EL BORRADOR A LAS VARIALBLES DE BORRADOR
            Brr_DescripcionLocal = editTextDescripcion.text.toString()
            Brr_IMAGEN1 = IMAGEN1_temp
            Brr_IMAGEN2 = IMAGEN2_temp
            Brr_IMAGEN3 = IMAGEN3_temp

            if (Brr_NombreLocal.isEmpty() || Brr_DescripcionLocal.isEmpty() || IMGLocal3.getDrawable() == null || IMGLocal1.getDrawable() == null || IMGLocal2.getDrawable() == null ) {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity(), R.style.Alert)
                alertDialog3.setMessage("Faltan Datos")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            } else {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity(), R.style.Alert)
                alertDialog3.setMessage("Local Agregado")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("EXITO")
                alert.show()
            }

            if (IMGLocal3.getDrawable() == null || IMGLocal1.getDrawable() == null || IMGLocal2.getDrawable() == null ) {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity(), R.style.Alert)
                alertDialog3.setMessage("Agregar 3 Imagenes")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            }
        }


        return viewOfLayout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            selectedFile = data?.data //The uri with the location of the file
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_nuevolocal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_nuevolocal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}