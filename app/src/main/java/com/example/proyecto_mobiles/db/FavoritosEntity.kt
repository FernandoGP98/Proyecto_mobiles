package com.example.proyecto_mobiles.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritosOff")
data class FavoritosEntity (
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id")val id:Int=0 ,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "descripcion")val descripcion:String,
    @ColumnInfo(name = "altitud") val altitud:String,
    @ColumnInfo(name = "longitud") val longitud:String,
    @ColumnInfo(name = "usuario_id") val usuario_id:Int
)