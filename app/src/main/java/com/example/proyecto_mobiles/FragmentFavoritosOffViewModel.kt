package com.example.proyecto_mobiles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_mobiles.db.FavoritosEntity
import com.example.proyecto_mobiles.db.RoomAppDB

class FragmentFavoritosOffViewModel(app: Application) : AndroidViewModel(app) {

    lateinit var allFvaoritos:MutableLiveData<List<FavoritosEntity>>

    init{
        allFvaoritos = MutableLiveData()
    }

    fun getAllFavoritosObservers():MutableLiveData<List<FavoritosEntity>>{
        return allFvaoritos
    }

    fun getAllFavoritos(){
        val favDAO = RoomAppDB.getAppDatabase( getApplication())?.favoritosDAO()
        val list = favDAO?.favoritosGetAll(usuarioSesion.ses.getID())

        allFvaoritos.postValue(list)
    }

    fun insertFavoritoInfo(entity: FavoritosEntity){
        val favDAO= RoomAppDB.getAppDatabase(getApplication())?.favoritosDAO()
        favDAO?.favoritosRegistrar(entity)
        getAllFavoritos()
    }

    fun updateFavorito(entity: FavoritosEntity){
        val favDAO= RoomAppDB.getAppDatabase(getApplication())?.favoritosDAO()
        favDAO?.favoritosUpdate(entity)
        getAllFavoritos()
    }

    fun deleteFavorito(entity: FavoritosEntity){
        val favDAO= RoomAppDB.getAppDatabase(getApplication())?.favoritosDAO()
        favDAO?.favoritosDelete(entity)
        getAllFavoritos()
    }
}