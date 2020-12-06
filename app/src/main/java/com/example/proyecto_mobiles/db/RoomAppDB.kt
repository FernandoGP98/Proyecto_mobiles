package com.example.proyecto_mobiles.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoritosEntity::class], version=1)
abstract class RoomAppDB: RoomDatabase() {

    abstract fun favoritosDAO(): FavoritosDAO?

    companion object{
        private var INSTANCE: RoomAppDB?=null

        fun getAppDatabase(context: Context):RoomAppDB?{
            if(INSTANCE==null){
                INSTANCE = Room.databaseBuilder<RoomAppDB>(
                    context.applicationContext, RoomAppDB::class.java, "AppDB")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}