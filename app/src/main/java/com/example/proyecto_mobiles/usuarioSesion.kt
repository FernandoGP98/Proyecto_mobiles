package com.example.proyecto_mobiles

import android.app.Application
import com.example.proyecto_mobiles.Internet.Internet
import com.example.proyecto_mobiles.session.Session

class usuarioSesion:Application() {
    companion object{
        lateinit var ses:Session
        lateinit var checkInternet: Internet
    }

    override fun onCreate() {
        super.onCreate()
        ses = Session(applicationContext)
        checkInternet = Internet(applicationContext)
    }
}