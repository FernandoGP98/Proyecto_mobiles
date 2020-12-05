package com.example.proyecto_mobiles

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_usuario_registro_dueno.view.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_usuario_registro_dueno.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_usuario_registro_dueno : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var viewOfLayout: View
    lateinit var dialogBuilder: AlertDialog.Builder
    var currentTimestamp:Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout= inflater.inflate(R.layout.fragment_usuario_registro_dueno, container, false)
        var btn_registrar = viewOfLayout.findViewById(R.id.btn_registrarDuenio) as Button
        var Tcorreo:TextView=viewOfLayout.findViewById(R.id.txt_correoRegistroDuenio)
        var Tcontrasenav1 : TextView = viewOfLayout.findViewById(R.id.txt_contrasenaRegistroDuenio)
        var Tcontrasenav2 : TextView = viewOfLayout.findViewById(R.id.txt_contrasenaConfirmRegistroDuenio)
        var contrasenaFinal: String = "noName"

        btn_registrar.setOnClickListener{
            var correo: String = viewOfLayout.txt_correoRegistroDuenio.text.toString()
            var contrasenaV1:String=viewOfLayout.txt_contrasenaRegistroDuenio.text.toString()
            var contrasenaV2:String=viewOfLayout.txt_contrasenaConfirmRegistroDuenio.text.toString()

            dialogBuilder = AlertDialog.Builder(requireActivity(), R.style.Alert)

            if(correo.isEmpty() || contrasenaV1.isEmpty() || contrasenaV2.isEmpty()){
                dialogBuilder.setMessage("Faltan datos")
                    .setCancelable(false)
                    .setNegativeButton("OK", DialogInterface.OnClickListener{dialog, id->
                        dialog.cancel()
                    })
                val alert = dialogBuilder.create()
                alert.setTitle("ERROR")
                alert.show()
            }else{
                if('@' in correo){
                    if(contrasenaV1!=contrasenaV2){

                        dialogBuilder.setMessage("La contraseña debe coincidir")
                            .setCancelable(false)
                            .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })
                        val alert = dialogBuilder.create()
                        alert.setTitle("ERROR")
                        alert.show()
                        Tcontrasenav1.setText("")
                        Tcontrasenav2.setText("")
                    }else{
                        contrasenaFinal=contrasenaV1
                        currentTimestamp=System.currentTimeMillis()
                        DueñoRegistro(correo, contrasenaFinal)
                        dialogBuilder.setMessage("Registro Completo")
                            .setCancelable(false)
                            .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                                Tcorreo.setText("")
                                Tcontrasenav1.setText("")
                                Tcontrasenav2.setText("")
                                dialog.cancel()
                            })
                        val alert = dialogBuilder.create()
                        alert.setTitle("Exito")
                        alert.show()
                    }
                }else{
                    dialogBuilder.setMessage("Ingrese un verdadero correo")
                        .setCancelable(false)
                        .setNegativeButton("OK", DialogInterface.OnClickListener { dialog, id ->
                            //closeKeyboard()
                            dialog.cancel()
                        })
                    val alert = dialogBuilder.create()
                    alert.setTitle("ERROR")
                    alert.show()
                }
            }

        }
        return viewOfLayout
    }

    private fun DueñoRegistro(correo:String, contrasenaFinal:String){
        val nombre = "usuario" + currentTimestamp.toString()
        val queue = Volley.newRequestQueue(requireActivity())
        val body = JSONObject()
        body.put("correo", correo)
        body.put("password", contrasenaFinal)
        body.put("nombre", nombre)
        body.put("rol_id", 2)
        val requ = JsonObjectRequest(Request.Method.POST, "https://restaurantespia.herokuapp.com/UsuarioRegistrar",body,{
            response: JSONObject?->
            val success = response?.getInt("success")
            if(success==1){
                val usuario = response.getJSONObject("usuario")
                val toast = Toast.makeText(requireActivity(), "Registro Exitoso", Toast.LENGTH_SHORT)
                toast.show()
            }

        },{error->
            error.printStackTrace()
            Log.e("Servicio web", "Web", error)
            if(error.toString()=="com.android.volley.ServerError"){
                val toast = Toast.makeText(requireActivity(), "Error del servidor", Toast.LENGTH_LONG)
                toast.show()
            }
        })
        requ.setRetryPolicy(DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue.add(requ)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_usuario_registro_dueno.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_usuario_registro_dueno().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}