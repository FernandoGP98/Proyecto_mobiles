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
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_perfil.*


// Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [fragment_perfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_perfil : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewOfLayout: View
    private var selectedFile: Uri? = null
    private lateinit var radioGroup: RadioGroup
    private lateinit var selectedRadioButton: RadioButton

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

        viewOfLayout = inflater!!.inflate(R.layout.fragment_perfil, container, false)

        val nombreL: TextView = viewOfLayout.findViewById(R.id.editTextTextPersonName2) as TextView
        val contra1L: TextView = viewOfLayout.findViewById(R.id.editTextContra1) as TextView
        val contra2L: TextView = viewOfLayout.findViewById(R.id.editTextContra2) as TextView
        val avatarL: ImageView = viewOfLayout.findViewById(R.id.imageView) as ImageView
        radioGroup = viewOfLayout.findViewById(R.id.radioG)


        val btn_names = viewOfLayout.findViewById(R.id.buttonPerfil1) as Button
        val btn_contras = viewOfLayout.findViewById(R.id.buttonPerfil2) as Button
        val btn_imagen = viewOfLayout.findViewById(R.id.buttonIMAGEN) as Button
        val btn_imagenAct = viewOfLayout.findViewById(R.id.buttonIMAGENAct) as Button
        val btn_cuenta = viewOfLayout.findViewById(R.id.buttonCuenta) as Button
        var nuevoNombre: String
        var nuevaContra: String
        var contrasenaFinalNueva: String = "vacio"

        /*.llenado temporal simulando LA BASE DE DATOS.  Agregar variables*/
        nombreL.setText("JUAN")
        contra1L.setText("contra")
        contra2L.setText("contra")
        avatarL.setImageResource(R.drawable.avatar)

        //avatarL.setImageResource(R.drawable.mona)
        /*................................................*/

        btn_names.setOnClickListener {

            var nombreN: String = editTextTextPersonName2.text.toString()                               ////CABIAR NOMBRE

            if (nombreN.isEmpty()) {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity())
                alertDialog3.setMessage("Agrege un Nombre valido")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            } else {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity())
                alertDialog3.setMessage("Nombre Actualizado")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("EXITO")
                alert.show()
                nuevoNombre = nombreN                                                                 ///Variable con el Nuevo Nombre
            }
        }
        btn_contras.setOnClickListener {                                                              ////CAMBIAR CONTRASE;A

            var contra1N: String = editTextContra1.text.toString()
            var contra2N: String = editTextContra2.text.toString()

            if (contra1N.isEmpty() || contra2N.isEmpty()) {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity())
                alertDialog3.setMessage("Faltan Datos")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            }
            if (contra1N != contra2N) {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity())
                alertDialog3.setMessage("La contraseña debe coincidir")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("ERROR")
                alert.show()
            } else {
                val alertDialog3 =
                    AlertDialog.Builder(requireActivity())
                alertDialog3.setMessage("Contraseña Actualizada")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("EXITO")
                alert.show()
                contrasenaFinalNueva = contra1N                                                       ///VARIABLE CON NUEVA CONTRASE;A
            }
            println("contra 1 $contra1N")
            println("contra 1 validacion $contra2N")
            println("contra final $contrasenaFinalNueva")
        }
        btn_imagen.setOnClickListener {                                                               ////SELECCIONAR IMAGEN

            val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)

            println("the value is $selectedFile")

        }
        btn_imagenAct.setOnClickListener {                                                              ///ACTUALIZAR Y SUBIR LA IMAGEN

            avatarL.setImageURI(selectedFile)                                                         ///VARIABLE CON LA DIRECCION URI DE LA IMAGEN NUEVA (selectedFile)

            println("the value is $selectedFile")
        }
        btn_cuenta.setOnClickListener {                                                              ///SELECCIONAR TIPO DE CUENTA

            val selectedRadioButtonId: Int = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                selectedRadioButton = viewOfLayout.findViewById(selectedRadioButtonId)
                val TipoCuenta: String = selectedRadioButton.text.toString()                          /// VARIABLE CON EL TIPO DE CUENTA
                println("the value is $TipoCuenta")

                val alertDialog3 =
                    AlertDialog.Builder(requireActivity())
                alertDialog3.setMessage("Tipo de Cuenta ACtualizada")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = alertDialog3.create()
                alert.setTitle("EXITO")
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
         * @return A new instance of fragment fragment_perfil.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_perfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}