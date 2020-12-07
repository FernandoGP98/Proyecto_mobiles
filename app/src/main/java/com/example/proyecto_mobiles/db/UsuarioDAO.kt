package com.example.proyecto_mobiles.db

import androidx.room.*

@Dao
interface UsuarioDAO {

    @Query("SELECT * FROM usuarioOff WHERE email = :correo")
    fun usuarioGetAll(correo:String):List<UsuarioEntity>?

    @Insert
    fun usuarioRegistrar(usuarioEntity: UsuarioEntity?)

    @Delete
    fun usuarioDelete(usuarioEntity: UsuarioEntity?)

    @Update
    fun usuarioUpdate(usuarioEntity: UsuarioEntity?)
}