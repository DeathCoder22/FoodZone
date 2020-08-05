package com.atul.foodzone.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context:Context):Boolean{
        val conMan = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo : NetworkInfo?= conMan.activeNetworkInfo
        if(networkInfo != null){
            return networkInfo.isConnected
        }else{
            return false
        }
    }
}