package qamalyan.aren.domain.extension

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

@SuppressLint("MissingPermission")
fun Context.hasNetworkConnection(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork);
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true;
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true;
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true;
                }
            }
        }
    } else {
        @Suppress("deprecation")
        try {
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo;
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true;
            }
        } catch (_: Exception) {
        }
    }
    return false
}