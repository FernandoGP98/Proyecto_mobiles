package com.example.proyecto_mobiles.session

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.withContext

class Session (val context: Context){
    val SHARED_NAME = "session"
    val SHARED_USERNAME = "name"
    val SHARED_MAIL = "mail"
    val SHARED_PASS = "pass"
    val SHARED_FOTO = "foto"
    val SHARED_ROL = "rol"
    val SHARED_RESTAURANTE = "restaurante"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    /*FUNCIONES PARA GUARDAR DATOS DENTRO DE LA SESION*/
    fun saveName(name:String){
        storage.edit().putString(SHARED_USERNAME, name).apply()
    }

    fun saveMail(mail:String){
        storage.edit().putString(SHARED_MAIL, mail).apply()
    }

    fun savePass(pass:String){
        storage.edit().putString(SHARED_PASS, pass).apply()
    }

    fun saveFoto(foto:String){
        storage.edit().putString(SHARED_FOTO, foto).apply()
    }

    fun saveRol(rol:Int){
        storage.edit().putInt(SHARED_ROL, rol).apply()
    }

    fun saveRestaurante(restaurante:String){
        storage.edit().putString(SHARED_RESTAURANTE, restaurante).apply()
    }

    /*FUNCIONES PARA OBTENER DATOS DENTRO DE LA SESION*/
    fun getName():String{
        return storage.getString(SHARED_USERNAME,"usuario vacio")!!
    }

    fun getMail():String{
        return storage.getString(SHARED_MAIL, "correo vacio")!!
    }

    fun getPass():String{
        return storage.getString(SHARED_PASS,"contraseña vacio")!!
    }

    fun getFoto():String{
        return storage.getString(SHARED_FOTO,"foto vacia")!!
    }

    fun getRol():Int{
        return storage.getInt(SHARED_ROL,0)!!
    }

    fun getRestaurante():String{
        return storage.getString(SHARED_RESTAURANTE, "restaurante vacio")!!
    }

    /*LIMPIAR LA SESION DE TODOS SUS DATOS*/
    fun wipe(){
        storage.edit().clear().apply()
    }
}