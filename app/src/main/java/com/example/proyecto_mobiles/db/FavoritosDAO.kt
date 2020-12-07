package com.example.proyecto_mobiles.db

import androidx.room.*

@Dao
interface FavoritosDAO {

    @Query("SELECT * FROM favoritosOff WHERE usuario_id = :usId")
    fun favoritosGetAll(usId:Int):List<FavoritosEntity>?

    @Query("SELECT * FROM favoritosOff WHERE id = :rId")
    fun favoritosGetById(rId:Int):List<FavoritosEntity>?

    @Insert
    fun favoritosRegistrar(favorito: FavoritosEntity?)

    @Delete
    fun favoritosDelete(favorito: FavoritosEntity?)

    @Update
    fun favoritosUpdate(favorito: FavoritosEntity?)

}