package qamalyan.aren.githubexplorer.common.extension

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Parcelable> Bundle?.parcelableOrThrow(key: String): T =
    this?.run {
        parcelable(key) ?: error("arguments with key[$key] not found in entry $this")
    } ?: error("arguments not initialized")

inline fun <reified T : Serializable> Bundle?.serializableOrThrow(key: String): T =
    this?.run {
        serializable(key) ?: error("arguments with key[$key] not found in entry $this")
    } ?: error("arguments not initialized")