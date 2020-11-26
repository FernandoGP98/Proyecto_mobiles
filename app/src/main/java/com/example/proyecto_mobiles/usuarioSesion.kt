package com.example.proyecto_mobiles

import android.app.Application
import com.example.proyecto_mobiles.session.Session

class usuarioSesion:Application() {
    companion object{
        lateinit var ses:Session
    }

    override fun onCreate() {
        super.onCreate()
        ses = Session(applicationContext)
    }
}