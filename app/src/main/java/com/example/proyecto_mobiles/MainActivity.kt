package com.example.proyecto_mobiles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistrarse : TextView = findViewById(R.id.txt_Registrate)
        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, usuario_registro::class.java)
            startActivity(intent)
        }

        val btnIngresar : TextView = findViewById(R.id.btn_ingresar)
        btnIngresar.setOnClickListener {
            val intent = Intent(this, home::class.java)
            startActivity(intent)
        }
    }

}