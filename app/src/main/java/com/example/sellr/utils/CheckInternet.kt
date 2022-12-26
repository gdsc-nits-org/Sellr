@file:Suppress("DEPRECATION")

package com.example.sellr.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object CheckInternet {
    @JvmStatic
    fun isConnectedToInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.allNetworkInfo
        for (networkInfo in info) {
            if (networkInfo.state == NetworkInfo.State.CONNECTED) return false
        }
        return true
    }
}

