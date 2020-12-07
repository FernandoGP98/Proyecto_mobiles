package com.example.proyecto_mobiles.Internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

class Internet(private val context: Context){

    fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    /*val toast = Toast.makeText(this, "NetworkCapabilities.TRANSPORT_CELLULAR", Toast.LENGTH_LONG)
                    toast.show()*/
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    /*val toast = Toast.makeText(this, "NetworkCapabilities.TRANSPORT_WIFI", Toast.LENGTH_LONG)
                    toast.show()*/
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    /*val toast = Toast.makeText(this, "NetworkCapabilities.TRANSPORT_ETHERNET", Toast.LENGTH_LONG)
                    toast.show()*/
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


}