package com.example.proyecto_mobiles.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarioOff")
class UsuarioEntity (
    @PrimaryKey(autoGenerate = false)@ColumnInfo(name = "id")val id:Int=0 ,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "email") val correo: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "rol_id") val rol_id: Int
)