package qamalyan.aren.data.persistent

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import qamalyan.aren.domain.persistant.ISharedPrefHolder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefDelegate<T>(
    private val defaultValue: T,
    //NOTE: _key parameter is optional. The property name will be used if not specified
    private val _key: String? = null
) {

    private var storedValue: T? = null

    operator fun provideDelegate(
        thisRef: ISharedPrefHolder,
        prop: KProperty<*>
    ): ReadWriteProperty<ISharedPrefHolder, T> {
        val key = _key ?: prop.name

        return object : ReadWriteProperty<ISharedPrefHolder, T> {
            override fun getValue(thisRef: ISharedPrefHolder, property: KProperty<*>): T {
                if (!thisRef.preferences.contains(key)) {
                    setValue(thisRef, property, defaultValue)
                    return defaultValue
                }
                if (storedValue == null) {
                    @Suppress("UNCHECKED_CAST")
                    storedValue = when (defaultValue) {
                        is Int -> thisRef.preferences.getInt(key, defaultValue as Int) as T
                        is Long -> thisRef.preferences.getLong(key, defaultValue as Long) as T
                        is Float -> thisRef.preferences.getFloat(key, defaultValue as Float) as T
                        is String -> thisRef.preferences.getString(key, defaultValue as String) as T
                        is Boolean -> thisRef.preferences.getBoolean(
                            key,
                            defaultValue as Boolean
                        ) as T

                        else -> error("This type can not be stored into Preferences")
                    }
                }
                return storedValue!!
            }

            override fun setValue(thisRef: ISharedPrefHolder, property: KProperty<*>, value: T) {
                with(thisRef.preferences.edit()) {
                    when (value) {
                        is Int -> putInt(key, value)
                        is Long -> putLong(key, value)
                        is Float -> putFloat(key, value)
                        is String -> putString(key, value)
                        is Boolean -> putBoolean(key, value)
                        else -> error("Only primitive types can be stored into Preferences")
                    }
                    apply()
                }
                storedValue = value
            }

        }
    }
}

class PrefObjDelegate<T>(
    private val serializer: KSerializer<T>
) {
    private var storedValue: T? = null

    operator fun provideDelegate(
        thisRef: ISharedPrefHolder,
        prop: KProperty<*>
    ): ReadWriteProperty<ISharedPrefHolder, T?> {
        val key = prop.name
        return object : ReadWriteProperty<ISharedPrefHolder, T?> {
            override fun getValue(thisRef: ISharedPrefHolder, property: KProperty<*>): T? {
                if (storedValue == null) {
                    storedValue = thisRef.preferences.getString(key, null)?.let {
                        Json.decodeFromString(serializer, it)
                    }
                }
                return storedValue
            }

            override fun setValue(thisRef: ISharedPrefHolder, property: KProperty<*>, value: T?) {
                storedValue = value
                with(thisRef.preferences.edit()) {
                    putString(key, value?.let { Json.encodeToString(serializer, it) })
                    apply()
                }
            }
        }
    }
}
