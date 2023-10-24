package qamalyan.aren.data.persistent

import android.content.SharedPreferences
import qamalyan.aren.domain.persistant.ISharedPrefHolder
import qamalyan.aren.domain.persistant.PrefManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefManagerImpl @Inject constructor(
    preferences: SharedPreferences
) : PrefManager, ISharedPrefHolder {

    override val preferences: SharedPreferences by lazy { preferences }

    override fun clearData() {

    }
}