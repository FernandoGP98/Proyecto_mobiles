package com.example.proyecto_mobiles.session

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.withContext

class Session (val context: Context){
    val SHARED_ID = "id"
    val SHARED_NAME = "session"
    val SHARED_USERNAME = "name"
    val SHARED_MAIL = "mail"
    val SHARED_PASS = "pass"
    val SHARED_FOTO = "foto"
    val SHARED_ROL = "rol"
    val SHARED_RESTAURANTE = "restaurante"
    val SHARED_RESTAURANTEIMG1 = "restauranteimg1"
    val SHARED_RESTAURANTEIMG2 = "restauranteimg2"
    val SHARED_RESTAURANTEIMG3 = "restauranteimg3"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    /*FUNCIONES PARA GUARDAR DATOS DENTRO DE LA SESION*/

    fun saveID(id:Int){
        storage.edit().putInt(SHARED_ID, id).apply()
    }

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

    fun saveRestaurante(restaurante:Int){
        storage.edit().putInt(SHARED_RESTAURANTE, restaurante).apply()
    }

    fun saveRestauranteimg1(restauranteimg1:String){
        storage.edit().putString(SHARED_RESTAURANTEIMG1, restauranteimg1).apply()
    }
    fun saveRestauranteimg2(restauranteimg2:String){
        storage.edit().putString(SHARED_RESTAURANTEIMG2, restauranteimg2).apply()
    }
    fun saveRestauranteimg3(restauranteimg3:String){
        storage.edit().putString(SHARED_RESTAURANTEIMG3, restauranteimg3).apply()
    }

    /*FUNCIONES PARA OBTENER DATOS DENTRO DE LA SESION*/
    fun getID():Int{
        return storage.getInt(SHARED_ID,0)!!
    }

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

    fun getRestaurante():Int{
        return storage.getInt(SHARED_RESTAURANTE,0)!!
    }

    fun getRestauranteimg1():String{
        return storage.getString(SHARED_RESTAURANTEIMG1,"foto vacia")!!
    }
    fun getRestauranteimg2():String{
        return storage.getString(SHARED_RESTAURANTEIMG2,"foto vacia")!!
    }
    fun getRestauranteimg3():String{
        return storage.getString(SHARED_RESTAURANTEIMG3,"foto vacia")!!
    }

    /*LIMPIAR LA SESION DE TODOS SUS DATOS*/
    fun wipe(){
        storage.edit().clear().apply()
    }
}