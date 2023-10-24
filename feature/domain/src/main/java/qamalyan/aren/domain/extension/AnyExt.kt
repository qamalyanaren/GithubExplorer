package qamalyan.aren.domain.extension

import android.util.Log

private const val LOG_KEY = "GithubExplorerDebugLog"

fun Any?.log(prefix: String = "", divider: String = ":", appendix: String = "") {
    Log.d(
        LOG_KEY,
        prefix + " ${if (prefix.isNotBlank()) divider else ""} " + this.toString() + "${if (appendix.isNotBlank()) divider else ""} $appendix"
    )
}

fun Any?.println(prefix: String = "", divider: String = ":", appendix: String = "") {
    println(prefix + " ${if (prefix.isNotBlank()) divider else ""} " + this.toString() + "${if (appendix.isNotBlank()) divider else ""} $appendix")
}

fun Iterable<*>.iterateLog(prefix: String = "", divider: String = ":", appendix: String = "") {
    this.forEachIndexed { index, any ->
        any.log("$prefix index : $index", divider, appendix)
    }
}