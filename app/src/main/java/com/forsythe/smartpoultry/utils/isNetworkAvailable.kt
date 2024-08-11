package com.forsythe.smartpoultry.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun Context.isNetworkAvailable() : Boolean{
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    return when{
        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> true
        //networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        //networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false

    }
}