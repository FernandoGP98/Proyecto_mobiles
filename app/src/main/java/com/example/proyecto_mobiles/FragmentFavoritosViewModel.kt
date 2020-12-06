package com.example.proyecto_mobiles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_mobiles.db.FavoritosEntity
import com.example.proyecto_mobiles.db.RoomAppDB
import com.example.proyecto_mobiles.usuarioSesion.Companion.ses

class FragmentFavoritosViewModel(app: Application): AndroidViewModel(app) {

    lateinit var favAll: MutableLiveData<List<FavoritosEntity>>

    init {
        favAll = MutableLiveData()

    }

    fun favoritosGetAllObservers():MutableLiveData<List<FavoritosEntity>>{
        return favAll
    }

    fun favoritosGetAll(){
        val favDAO = RoomAppDB.getAppDatabase( getApplication())?.favoritosDAO()
        val list = favDAO?.favoritosGetAll(ses.getID())

        favAll.postValue(list)
    }

    fun favoritosRegistrar(entity: FavoritosEntity){
        val favDAO= RoomAppDB.getAppDatabase(getApplication())?.favoritosDAO()
        favDAO?.favoritosRegistrar(entity)
        favoritosGetAll()
    }

    fun favoritosEditar(entity: FavoritosEntity){
        val favDAO= RoomAppDB.getAppDatabase(getApplication())?.favoritosDAO()
        favDAO?.favoritosUpdate(entity)
        favoritosGetAll()
    }

    fun favoritosEliminar(entity: FavoritosEntity){
        val favDAO= RoomAppDB.getAppDatabase(getApplication())?.favoritosDAO()
        favDAO?.favoritosDelete(entity)
        favoritosGetAll()
    }

}