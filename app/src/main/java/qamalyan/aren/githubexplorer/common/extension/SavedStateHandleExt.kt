package qamalyan.aren.githubexplorer.common.extension

import androidx.lifecycle.SavedStateHandle

fun <T> SavedStateHandle.getOrThrow(key: String = "args") : T = get<T>(key) ?: error(
    "not found arguments with key [$key] in entry"
)