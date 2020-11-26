package com.example.proyecto_mobiles.session

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.withContext

class Session (val context: Context){
    val SHARED_NAME = "session"
    val SHARED_USERNAME = "name"
    val SHARED_MAIL = "mail"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    /*FUNCIONES PARA GUARDAR DATOS DENTRO DE LA SESION*/
    fun saveName(name:String){
        storage.edit().putString(SHARED_USERNAME, name).apply()
    }

    fun saveMail(mail:String){
        storage.edit().putString(SHARED_MAIL, mail).apply()
    }

    /*FUNCIONES PARA OBTENER DATOS DENTRO DE LA SESION*/
    fun getName():String{
        return storage.getString(SHARED_USERNAME,"usuario vacio")!!
    }

    fun getMail():String{
        return storage.getString(SHARED_MAIL, "correo vacio")!!
    }

    /*LIMPIAR LA SESION DE TODOS SUS DATOS*/
    fun wipe(){
        storage.edit().clear().apply()
    }
}